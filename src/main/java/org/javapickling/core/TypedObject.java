package org.javapickling.core;

public class TypedObject<T> {

    public final Class<T> clazz;
    public final ObjectType type;
    public final T value;

    public TypedObject() {
        this(null);
    }

    public TypedObject(T value) {
        if (value == null) {
            this.clazz = null;
            this.type = ObjectType.NULL;
            this.value = null;
        } else {
            this.clazz = (Class<T>)value.getClass();
            this.type = ObjectType.ofClass(clazz);
            this.value = value;
        }
    }

    public TypedObject(ObjectType type, T value) {
        this.clazz = (Class<T>)value.getClass();
        this.type = type;
        this.value = value;
    }

    public boolean isArray() {
        return clazz != null && clazz.isArray();
    }
}
