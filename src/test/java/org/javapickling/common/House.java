package org.javapickling.common;

import java.io.Serializable;
import java.util.List;

public class House implements Serializable {
    public final double volume;
    public final List<Person> occupants;

    public House(double volume, List<Person> occupants) {
        this.volume = volume;
        this.occupants = occupants;
    }
}
