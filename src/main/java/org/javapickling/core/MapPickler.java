package org.javapickling.core;

/**
 * MapPickler is an intermediate interface for pickling heterogeneous maps.
 * @param <PF>
 */
public interface MapPickler<PF> {

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
