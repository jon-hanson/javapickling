package org.javapickling.core;

/**
 * A field description.
 * @param <T> The field type
 * @param <PF> Pickler format
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

    protected Field(String name, Pickler<T, PF> pickler) {
        this.name = name;
        this.pickler = pickler;
    }
}
