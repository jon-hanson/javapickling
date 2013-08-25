package org.javapickling.core;

public class Field<T, PF> {

    public static <T, PF> Field<T, PF> create(String name, Pickler<T, PF> pickler) {
        return new Field<T, PF>(name, pickler);
    }

    public final String name;
    public final Pickler<T, PF> pickler;

    public Field(String name, Pickler<T, PF> pickler) {
        this.name = name;
        this.pickler = pickler;
    }
}
