package org.javapickling.core;

/**
 * A pickler for objects where the static type is unknown.
 * In this case the pickler must encode the objects type along with the value.
 * @param <PF>
 * @param <T>
 */
public class DynamicObjectPickler<PF, T> extends PicklerBase<T, PF> {

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

    protected static final String VALUE_NAME = "value";

    protected Field<String, PF> typeField = field("type", string_p());
    protected Field<String, PF> clazzField = field("class", string_p());

    public DynamicObjectPickler(PicklerCore<PF> core) {
        super(core);
    }

    @Override
    public PF pickle(T obj, PF target) throws Exception {

        final MetaType metaType = MetaType.ofObject(obj);

        final OptimalResult<PF> optimalResult = optimalPickle(metaType, obj, target);
        if (optimalResult.success) {
            return optimalResult.value;
        }

        final FieldPickler<PF> mp = core.object_map().pickler(target);
        mp.field(typeField, metaType.name());
        if (metaType.typeKind == MetaType.TypeKind.ENUM || metaType.typeKind == MetaType.TypeKind.OBJECT) {
            mp.field(clazzField, metaType.clazz.getName());
        }

        if (metaType.typeKind != MetaType.TypeKind.NULL) {
            mp.field(VALUE_NAME, obj, metaType.pickler(core));
        }

        return mp.pickle(target);
    }

    @Override
    public T unpickle(PF source) throws Exception {

        final OptimalResult<T> optimalResult = optimalUnpickle(source);
        if (optimalResult.success) {
            return optimalResult.value;
        }

        final FieldUnpickler<PF> mu = core.object_map().unpickler(source);

        MetaType metaType = MetaType.ofName(mu.field(typeField));

        if (metaType.typeKind == MetaType.TypeKind.ENUM || metaType.typeKind == MetaType.TypeKind.OBJECT) {
            try {
                final Class clazz = Class.forName(mu.field(clazzField));
                metaType = new MetaType(metaType.typeKind, clazz, metaType.arrayDepth);
            } catch (ClassNotFoundException ex) {
                throw new PicklerException("Can not construct class", ex);
            }
        }

        if (metaType.typeKind != MetaType.TypeKind.NULL) {
            return (T)mu.field(VALUE_NAME, metaType.pickler(core));
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
