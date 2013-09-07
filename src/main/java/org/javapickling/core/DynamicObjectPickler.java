package org.javapickling.core;

public class DynamicObjectPickler<PF, T> extends PicklerBase<T, PF> {

    private static final String VALUE_NAME = "value";

    private Field<String, PF> type = field("type", string_p());
    private Field<String, PF> clazz = field("class", string_p());

    public DynamicObjectPickler(PicklerCore<PF> core) {
        super(core);
    }

    @Override
    public PF pickle(T obj, PF target) throws Exception {

        final MetaType metaType = MetaType.ofObject(obj);

        final FieldPickler<PF> mp = core.object_map().pickler(target);
        mp.field(type, metaType.name());
        if (metaType.type == MetaType.Type.ENUM || metaType.type == MetaType.Type.OBJECT) {
            mp.field(clazz, metaType.clazz.getName());
        }

        if (metaType.type != MetaType.Type.NULL) {
            mp.field(VALUE_NAME, obj, metaType.pickler(core));
        }

        return mp.pickle(target);
    }

    @Override
    public T unpickle(PF source) throws Exception {

        final FieldUnpickler<PF> mu = core.object_map().unpickler(source);

        final MetaType metaType = MetaType.ofName(mu.field(type));

        if (metaType.type == MetaType.Type.ENUM || metaType.type == MetaType.Type.OBJECT) {
            try {
                metaType.clazz = Class.forName(mu.field(clazz));
            } catch (ClassNotFoundException ex) {
                throw new PicklerException("Can not construct class", ex);
            }
        }

        if (metaType.type != MetaType.Type.NULL) {
            return (T)mu.field(VALUE_NAME, metaType.pickler(core));
        } else {
            return null;
        }
    }
}
