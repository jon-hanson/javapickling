package org.javafp.javapickling.common;

import org.javafp.javapickling.core.DefaultPickler;

import java.io.Serializable;

@DefaultPickler(ComplexClassPickler.GenericPickler.class)
public final class Generic<T extends Interface> implements Serializable {

    public final T value;

    public Generic(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Generic<T> rhs = (Generic<T>)obj;
        return value.equals(rhs.value);
    }

    @Override
    public String toString() {
        return "Generic{" +
                "value=" + value +
                '}';
    }
}
