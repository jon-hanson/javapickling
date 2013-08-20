package org.javapickling.core;

import java.io.IOException;

public class TypedObjectPickler<PF> extends ObjectPickler<TypedObject, PF> {

    @Override
    public PF pickle(TypedObject typedObject, PF target) throws IOException {
        // TODO:
        return null;
    }

    @Override
    public TypedObject unpickle(PF source) throws IOException {
        // TODO:
        return null;
    }
}
