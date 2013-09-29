package org.javapickling.core;

/**
 * ObjectPickler is an intermediate interface for pickling objects.
 * In this context objects are heterogeneous maps where the key type is String.
 * @param <PF>
 */
public interface ObjectPickler<PF> {

    /**
     * Provide a pseudo-pickler for pickling the fields comprising a map.
     * @param pf
     */
    FieldPickler<PF> pickler(PF pf);

    /**
     * Provide a pseudo-pickler for unpickling the fields comprising a map.
     * @param pf
     */
    FieldUnpickler<PF> unpickler(PF pf);
}
