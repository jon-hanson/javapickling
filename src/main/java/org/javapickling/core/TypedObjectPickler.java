package org.javapickling.core;

import java.io.IOException;

public class TypedObjectPickler<PF> extends ObjectPickler<Object, PF> {

    protected final Pickler<ObjectType, PF> typePickler;
    protected final ObjectPickler<Object, PF> objectWithTypePickler;

    public TypedObjectPickler(PicklerCore<PF> core) {
        super(core);
        this.typePickler = core.enum_p(ObjectType.class, ObjectType.values());
        this.objectWithTypePickler = new ObjectPickler<Object, PF>(core) {

            @Override
            public PF pickle(Object obj, PF target) throws IOException {
                final FieldPickler<PF> mp = core.object_map().pickler(target);
                mp.string_f("class", obj.getClass().getName());
                mp.field("object", obj, core.object_p((Class<Object>) obj.getClass()));
                return mp.pickle(target);
            }

            @Override
            public Object unpickle(PF source) throws IOException {
                final FieldUnpickler<PF> mu = core.object_map().unpickler(source);
                try {
                    final String clazzName = mu.string_f("class", source);
                    final Class<?> clazz = Class.forName(clazzName);
                    return mu.field("object", source, core.object_p(clazz));
                } catch (ClassNotFoundException ex) {
                    throw new PicklerException("Could not construct a TypedObject", ex);
                }
            }
        };
    }

    @Override
    public PF pickle(Object obj, PF target) throws IOException {
        final ObjectType type = ObjectType.ofObject(obj);
        final FieldPickler<PF> mp = core.object_map().pickler(target);
        mp.field("type", type, typePickler);
        switch (type) {
            case NULL:
                mp.field("value", null, core.null_p());
                break;
            case BOOLEAN:
                mp.field("value", (Boolean)obj, core.boolean_p());
                break;
            case BYTE:
                mp.field("value", (Byte)obj, core.byte_p());
                break;
            case CHAR:
                mp.field("value", (Character)obj, core.char_p());
                break;
            case INTEGER:
                mp.field("value", (Integer)obj, core.integer_p());
                break;
            case SHORT:
                mp.field("value", (Short)obj, core.short_p());
                break;
            case LONG:
                mp.field("value", (Long)obj, core.long_p());
                break;
            case FLOAT:
                mp.field("value", (Float)obj, core.float_p());
                break;
            case DOUBLE:
                mp.field("value", (Double)obj, core.double_p());
                break;
            case STRING:
                mp.field("value", (String)obj, core.string_p());
                break;
            case OBJECT:
                mp.field("value", obj, objectWithTypePickler);
                break;
            case BOOLEAN_ARRAY:
                mp.field("value", (Boolean[])obj, core.array_p(core.boolean_p(), Boolean.class));
                break;
            case BYTE_ARRAY:
                mp.field("value", (Byte[])obj, core.array_p(core.byte_p(), Byte.class));
                break;
            case CHAR_ARRAY:
                mp.field("value", (Character[])obj, core.array_p(core.char_p(), Character.class));
                break;
            case INTEGER_ARRAY:
                mp.field("value", (Integer[])obj, core.array_p(core.integer_p(), Integer.class));
                break;
            case SHORT_ARRAY:
                mp.field("value", (Short[])obj, core.array_p(core.short_p(), Short.class));
                break;
            case LONG_ARRAY:
                mp.field("value", (Long[])obj, core.array_p(core.long_p(), Long.class));
                break;
            case FLOAT_ARRAY:
                mp.field("value", (Float[])obj, core.array_p(core.float_p(), Float.class));
                break;
            case DOUBLE_ARRAY:
                mp.field("value", (Double[])obj, core.array_p(core.double_p(), Double.class));
                break;
            case STRING_ARRAY:
                mp.field("value", (String[])obj, core.array_p(core.string_p(), String.class));
                break;
            case OBJECT_ARRAY:
                mp.field("value", (Object[])obj, core.array_p(objectWithTypePickler, Object.class));
                break;
        }

        return mp.pickle(target);
    }

    @Override
    public Object unpickle(PF source) throws IOException {
        final FieldUnpickler<PF> mu = core.object_map().unpickler(source);
        final ObjectType type = mu.field("type", source, typePickler);
        switch (type) {
            case NULL:
                return mu.field("value", source, core.null_p());
            case BOOLEAN:
                return mu.field("value", source, core.boolean_p());
            case BYTE:
                return mu.field("value", source, core.byte_p());
            case CHAR:
                return mu.field("value", source, core.char_p());
            case INTEGER:
                return mu.field("value", source, core.integer_p());
            case SHORT:
                return mu.field("value", source, core.short_p());
            case LONG:
                return mu.field("value", source, core.long_p());
            case FLOAT:
                return mu.field("value", source, core.float_p());
            case DOUBLE:
                return mu.field("value", source, core.double_p());
            case STRING:
                return mu.field("value", source, core.string_p());
            case OBJECT:
                return mu.field("value", source, objectWithTypePickler);
            case BOOLEAN_ARRAY:
                return mu.field("value", source, core.array_p(core.boolean_p(), Boolean.class));
            case BYTE_ARRAY:
                return mu.field("value", source, core.array_p(core.byte_p(), Byte.class));
            case CHAR_ARRAY:
                return mu.field("value", source, core.array_p(core.char_p(), Character.class));
            case INTEGER_ARRAY:
                return mu.field("value", source, core.array_p(core.integer_p(), Integer.class));
            case SHORT_ARRAY:
                return mu.field("value", source, core.array_p(core.short_p(), Short.class));
            case LONG_ARRAY:
                return mu.field("value", source, core.array_p(core.long_p(), Long.class));
            case FLOAT_ARRAY:
                return mu.field("value", source, core.array_p(core.float_p(), Float.class));
            case DOUBLE_ARRAY:
                return mu.field("value", source, core.array_p(core.double_p(), Double.class));
            case STRING_ARRAY:
                return mu.field("value", source, core.array_p(core.string_p(), String.class));
            case OBJECT_ARRAY:
                return mu.field("value", source, core.array_p(objectWithTypePickler, Object.class));
            default:
                throw new PicklerException("Unexpected Type enum value - " + type);
        }
    }
}
