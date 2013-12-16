package org.javapickling.core;

/**
 * A pickler for objects where the static type is unknown.
 * In this case the pickler must encode the objects type along with the value.
 * @param <PF>
 * @param <T>
 */
public class DynamicObjectPickler<T, PF> extends PicklerBase<T, PF> {

    public static class OptimalResult<S> {
        public static <U> OptimalResult<U> failure() {
            return (OptimalResult<U>)failure;
        }

        public static <U, V> OptimalResult<V> success(U value) {
            return new OptimalResult<V>(true, (V)value);
        }

        private static final OptimalResult<?> failure = new OptimalResult<Object>(false, null);

        final boolean success;
        final S value;

        public OptimalResult(boolean success, S value) {
            this.success = success;
            this.value = value;
        }
    }

    protected static final String VALUE_NAME = "@value";

    protected Field<String, PF> typeField = field("@type", string_p());
    protected Field<String, PF> clazzField = field("@class", string_p());

    public DynamicObjectPickler(PicklerCore<PF> core, Class<? super T> clazz) {
        super(core, clazz);
    }

    @Override
    public PF pickle(T obj, PF target) throws Exception {

        final MetaType metaType = MetaType.ofObject(obj);

        final OptimalResult<PF> optimalResult = optimalPickle(metaType, obj, target);
        if (optimalResult.success) {
            return optimalResult.value;
        }

        final FieldPickler<PF> fp = object_map().pickler(target);
        fp.field(typeField, metaType.name());
        if (metaType.typeKind == MetaType.TypeKind.ENUM || metaType.typeKind == MetaType.TypeKind.OBJECT) {
            fp.field(clazzField, core.classToName(metaType.clazz));
        }

        if (metaType.typeKind != MetaType.TypeKind.NULL) {
            fp.field(VALUE_NAME, obj, metaType.pickler(core));
        }

        return fp.pickle(target);
    }

    @Override
    public T unpickle(PF source) throws Exception {

        final OptimalResult<T> optimalResult = optimalUnpickle(source);
        if (optimalResult.success) {
            return optimalResult.value;
        }

        final FieldUnpickler<PF> fu = object_map().unpickler(source);

        MetaType metaType = MetaType.ofName(fu.field(typeField));

        if (metaType.typeKind == MetaType.TypeKind.ENUM || metaType.typeKind == MetaType.TypeKind.OBJECT) {
            try {
                final Class clazz = core.nameToClass(fu.field(clazzField));
                metaType = new MetaType(metaType.typeKind, clazz, metaType.arrayDepth);
            } catch (ClassNotFoundException ex) {
                throw new PicklerException("Can not construct class", ex);
            }
        }

        if (metaType.typeKind != MetaType.TypeKind.NULL) {
            return (T)fu.field(VALUE_NAME, metaType.pickler(core));
        } else {
            return null;
        }
    }

    protected OptimalResult<PF> optimalPickle(MetaType metaType, T obj, PF target) throws Exception {
        return OptimalResult.failure();
    }

    protected OptimalResult<T> optimalUnpickle(PF source) throws Exception {
        return OptimalResult.failure();
    }
}
