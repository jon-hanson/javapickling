package org.javapickling.core;

import java.io.IOException;

public class TypedObjectPickler<PF> extends ObjectPickler<Object, PF> {

    protected final Pickler<TypedObject.Type, PF> typePickler;

    private final ObjectPickler<Object, PF> objectWithTypePickler;

    public TypedObjectPickler(PicklerCore<PF> core) {
        super(core);
        this.typePickler = core.enum_p(TypedObject.Type.class);
        this.objectWithTypePickler = new ObjectPickler<Object, PF>(core) {

            @Override
            public PF pickle(Object obj, PF target) throws IOException {
                final FieldPickler<PF> mp = core.object_map().pickler(target);
                mp.string_f("class", obj.getClass().getName());
                mp.field("object", obj, core.object_p((Class<Object>)obj.getClass()));
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
        final TypedObject typedObject = new TypedObject(obj);
        final FieldPickler<PF> mp = core.object_map().pickler(target);
        mp.field("type", typedObject.type, typePickler);
        switch (typedObject.type) {
            case NULL:
                throw new RuntimeException("");
            case BOOLEAN:
                mp.field("value", (Boolean)typedObject.value, core.boolean_p());
                break;
            case BYTE:
                mp.field("value", (Byte)typedObject.value, core.byte_p());
                break;
            case CHAR:
                mp.field("value", (Character)typedObject.value, core.char_p());
                break;
            case INTEGER:
                mp.field("value", (Integer)typedObject.value, core.integer_p());
                break;
            case SHORT:
                mp.field("value", (Short)typedObject.value, core.short_p());
                break;
            case LONG:
                mp.field("value", (Long)typedObject.value, core.long_p());
                break;
            case FLOAT:
                mp.field("value", (Float)typedObject.value, core.float_p());
                break;
            case DOUBLE:
                mp.field("value", (Double)typedObject.value, core.double_p());
                break;
            case STRING:
                mp.field("value", (String)typedObject.value, core.string_p());
                break;
            case OBJECT:
                mp.field("value", typedObject.value, objectWithTypePickler);
                break;
            case BOOLEAN_ARRAY:
                mp.field("value", (Boolean[])typedObject.value, core.array_p(core.boolean_p()));
                break;
            case BYTE_ARRAY:
                mp.field("value", (Byte[])typedObject.value, core.array_p(core.byte_p()));
                break;
            case CHAR_ARRAY:
                mp.field("value", (Character[])typedObject.value, core.array_p(core.char_p()));
                break;
            case INTEGER_ARRAY:
                mp.field("value", (Integer[])typedObject.value, core.array_p(core.integer_p()));
                break;
            case SHORT_ARRAY:
                mp.field("value", (Short[])typedObject.value, core.array_p(core.short_p()));
                break;
            case LONG_ARRAY:
                mp.field("value", (Long[])typedObject.value, core.array_p(core.long_p()));
                break;
            case FLOAT_ARRAY:
                mp.field("value", (Float[])typedObject.value, core.array_p(core.float_p()));
                break;
            case DOUBLE_ARRAY:
                mp.field("value", (Double[])typedObject.value, core.array_p(core.double_p()));
                break;
            case STRING_ARRAY:
                mp.field("value", (String[])typedObject.value, core.array_p(core.string_p()));
                break;
            case OBJECT_ARRAY:
                mp.field("value", (Object[])typedObject.value, core.array_p(objectWithTypePickler));
                break;
        }

        return mp.pickle(target);
    }

    @Override
    public Object unpickle(PF source) throws IOException {
        final TypedObject.Type type = typePickler.unpickle(source);
        switch (type) {
            case NULL:
                return new TypedObject();
            case BOOLEAN:
                return core.boolean_p().unpickle(source);
            case BYTE:
                return core.byte_p().unpickle(source);
            case CHAR:
                return core.char_p().unpickle(source);
            case INTEGER:
                return core.integer_p().unpickle(source);
            case SHORT:
                return core.short_p().unpickle(source);
            case LONG:
                return core.long_p().unpickle(source);
            case FLOAT:
                return core.float_p().unpickle(source);
            case DOUBLE:
                return core.double_p().unpickle(source);
            case STRING:
                return core.string_p().unpickle(source);
            case OBJECT:
                return objectWithTypePickler.unpickle(source);
            case BOOLEAN_ARRAY:
                return core.array_p(core.boolean_p()).unpickle(source);
            case BYTE_ARRAY:
                return core.array_p(core.byte_p()).unpickle(source);
            case CHAR_ARRAY:
                return core.array_p(core.char_p()).unpickle(source);
            case INTEGER_ARRAY:
                return core.array_p(core.integer_p()).unpickle(source);
            case SHORT_ARRAY:
                return core.array_p(core.short_p()).unpickle(source);
            case LONG_ARRAY:
                return core.array_p(core.long_p()).unpickle(source);
            case FLOAT_ARRAY:
                return core.array_p(core.float_p()).unpickle(source);
            case DOUBLE_ARRAY:
                return core.array_p(core.double_p()).unpickle(source);
            case STRING_ARRAY:
                return core.array_p(core.string_p()).unpickle(source);
            case OBJECT_ARRAY:
                return core.array_p(objectWithTypePickler).unpickle(source);
            default:
                throw new PicklerException("Unexpected Type enum value - " + type);
        }
    }
}
