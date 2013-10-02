package org.javapickling.core;

/**
 * ObjectPickler is an intermediate interface for pickling objects.
 * In this context objects are heterogeneous maps with a String key type.
 * @param <PF> the pickle format.
 */
public interface ObjectPickler<PF> {

    /**
     * Provide a pseudo-pickler for pickling the fields comprising an object.
     * @param pf
     */
    FieldPickler<PF> pickler(PF pf);

    /**
     * Provide a pseudo-pickler for unpickling the fields comprising an object.
     * @param pf
     */
    FieldUnpickler<PF> unpickler(PF pf);
}
