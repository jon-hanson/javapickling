package org.javapickling.core;

public abstract class ObjectPickler<T, PF> implements Pickler<T, PF>{

    protected PicklerCore<PF> core;

    protected ObjectPickler() {
    }

    protected ObjectPickler(PicklerCore<PF> core) {
        this.core = core;
    }

    public void setCore(PicklerCore<PF> core) {
        this.core = core;
    }
}
