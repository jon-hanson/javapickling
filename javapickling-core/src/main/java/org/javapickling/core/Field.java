package org.javapickling.core;

public class Field<T, PF> {

    public final String name;
    public final Pickler<T, PF> pickler;

    protected Field(String name, Pickler<T, PF> pickler) {
        this.name = name;
        this.pickler = pickler;
    }
}
