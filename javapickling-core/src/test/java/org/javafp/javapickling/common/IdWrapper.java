package org.javafp.javapickling.common;

import org.javafp.javapickling.core.DefaultPickler;

import java.io.Serializable;

@DefaultPickler(ComplexClassPickler.IdWrapperPickler.class)
public final class IdWrapper implements Interface, Serializable {

    public final String id;

    public IdWrapper(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final IdWrapper rhs = (IdWrapper)obj;
        return id.equals(rhs.id);
    }

    @Override
    public String toString() {
        return "IdWrapper{" +
                "id='" + id + '\'' +
                '}';
    }
}
