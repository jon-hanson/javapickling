package org.javapickling.core;

public interface FieldPickler<PF> {

    <T> void field(String name, T value, Pickler<T, PF> pickler) throws Exception;
    <T> void field(Field<T, PF> field, T value) throws Exception;

    PF pickle(PF pf) throws Exception;
}
