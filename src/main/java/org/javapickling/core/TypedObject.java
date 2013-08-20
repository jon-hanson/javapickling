package org.javapickling.core;

import com.google.common.collect.Maps;

import java.util.Map;

public class TypedObject<T> {

    public enum Type {
        NULL,
        BOOLEAN,
        BYTE,
        CHAR,
        INTEGER,
        SHORT,
        LONG,
        FLOAT,
        DOUBLE,
        STRING,
        OBJECT,
        BOOLEAN_ARRAY,
        BYTE_ARRAY,
        CHAR_ARRAY,
        INTEGER_ARRAY,
        SHORT_ARRAY,
        LONG_ARRAY,
        FLOAT_ARRAY,
        DOUBLE_ARRAY,
        STRING_ARRAY,
        OBJECT_ARRAY;

        private static final Map<String, Type> typeRegistry = Maps.newTreeMap();

        private static void register(Class<?> clazz, Type type) {
            typeRegistry.put(clazz.getName(), type);
        }

        static {
            register(Boolean.class, BOOLEAN);
            register(Byte.class, BYTE);
            register(Character.class, CHAR);
            register(Integer.class, INTEGER);
            register(Short.class, SHORT);
            register(Long.class, LONG);
            register(Float.class, FLOAT);
            register(Double.class, DOUBLE);
            register(String.class, STRING);
            register(Object.class, OBJECT);

            register(Boolean[].class, BOOLEAN_ARRAY);
            register(Byte[].class, BYTE_ARRAY);
            register(Character[].class, CHAR_ARRAY);
            register(Integer[].class, INTEGER_ARRAY);
            register(Short[].class, SHORT_ARRAY);
            register(Long[].class, LONG_ARRAY);
            register(Float[].class, FLOAT_ARRAY);
            register(Double[].class, DOUBLE_ARRAY);
            register(String[].class, STRING_ARRAY);
            register(Object[].class, OBJECT_ARRAY);
        }

        public static Type ofClass(Class<?> clazz) {
            if (clazz == null) {
                return NULL;
            } else {
                Type type = typeRegistry.get(clazz.getName());
                if (type != null) {
                    return type;
                } else {
                    return clazz.isArray() ? OBJECT_ARRAY : OBJECT;
                }
            }
        }
    }

    public final Class<T> clazz;
    public final Type type;
    public final T value;

    public TypedObject() {
        this(null);
    }

    public TypedObject(T value) {
        if (value == null) {
            this.clazz = null;
            this.type = Type.NULL;
            this.value = null;
        } else {
            this.clazz = (Class<T>)value.getClass();
            this.type = Type.ofClass(clazz);
            this.value = value;
        }
    }

    public TypedObject(Type type, T value) {
        this.clazz = (Class<T>)value.getClass();
        this.type = type;
        this.value = value;
    }

    public boolean isArray() {
        return clazz != null && clazz.isArray();
    }
}
