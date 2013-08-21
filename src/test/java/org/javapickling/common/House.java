package org.javapickling.common;

import java.io.Serializable;
import java.util.List;

public class House implements Serializable {
    public enum Type {
        DETACHED,
        SEMI_DETACHED,
        FLAT
    }

    public final Type type;
    public final double volume;
    public final List<Object> occupants;

    public House(Type type, double volume, List<Object> occupants) {
        this.type = type;
        this.volume = volume;
        this.occupants = occupants;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof House))
            return false;

        final House rhs = (House)obj;

        if (!type.equals(rhs.type))
            return false;
        if (Math.abs(volume - rhs.volume) > 0.0000001)
            return false;

        if (occupants.size() != rhs.occupants.size())
            return false;

        for (int i = 0; i < occupants.size(); ++i) {
            if (occupants.get(i).equals(rhs.occupants.get(i))) {
                return false;
            }
        }

        return true;
    }
}
