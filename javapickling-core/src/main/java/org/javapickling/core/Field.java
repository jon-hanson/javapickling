package org.javapickling.core;

/**
 * A field name and pickler wrapper.
 * @param <T> the type of object to be pickled.
 * @param <PF> the pickle format.
 */
public class Field<T, PF> {

    /**
     * The name of the field.
     */
    public final String name;

    /**
     * The pickler for the field.
     */
    public final Pickler<T, PF> pickler;

    public Field(String name, Pickler<T, PF> pickler) {
        this.name = name;
        this.pickler = pickler;
    }
}
