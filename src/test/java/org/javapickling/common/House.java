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
}
