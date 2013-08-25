package org.javapickling.core;

import java.io.IOException;

public class DynamicObjectPickler<PF> extends PicklerBase<Object, PF> {

    private final String TYPE_NAME = "type";
    private final String CLASS_NAME = "class";
    private final String VALUE_NAME = "value";

    public DynamicObjectPickler(PicklerCore<PF> core) {
        super(core);
    }

    @Override
    public PF pickle(Object obj, PF target) throws IOException {

        final MetaType metaType = MetaType.ofObject(obj);

        final FieldPickler<PF> mp = core.object_map().pickler(target);
        mp.string_f(TYPE_NAME, metaType.name());
        if (metaType.type == MetaType.Type.ENUM || metaType.type == MetaType.Type.OBJECT) {
            mp.string_f(CLASS_NAME, metaType.clazz.getName());
        }

        if (metaType.type != MetaType.Type.NULL) {
            mp.field(VALUE_NAME, obj, metaType.pickler(core));
        }

        return mp.pickle(target);
    }

    @Override
    public Object unpickle(PF source) throws IOException {
        final FieldUnpickler<PF> mu = core.object_map().unpickler(source);
        final MetaType metaType = MetaType.ofName(mu.string_f(TYPE_NAME));
        if (metaType.type == MetaType.Type.ENUM || metaType.type == MetaType.Type.OBJECT) {
            try {
                metaType.clazz = Class.forName(mu.string_f(CLASS_NAME));
            } catch (ClassNotFoundException ex) {
                throw new PicklerException("Can not construct class", ex);
            }
        }

        if (metaType.type != MetaType.Type.NULL) {
            return mu.field(VALUE_NAME, metaType.pickler(core));
        } else {
            return null;
        }
    }
}
