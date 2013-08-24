package org.javapickling.core;

/**
 * Base class for picklers
 * @param <T>
 * @param <PF>
 */
public abstract class PicklerBase<T, PF> implements Pickler<T, PF>{

    protected final PicklerCore<PF> core;

    protected PicklerBase(PicklerCore<PF> core) {
        this.core = core;
    }
}
