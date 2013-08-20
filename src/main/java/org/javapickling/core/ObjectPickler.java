package org.javapickling.core;

public abstract class ObjectPickler<T, PF> implements Pickler<T, PF>{

    protected final PicklerCore<PF> core;

    protected ObjectPickler(PicklerCore<PF> core) {
        this.core = core;
    }
}
