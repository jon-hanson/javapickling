package org.javapickling.core;

public interface FieldUnpickler<PF> {

    <T> T field(String name, Pickler<T, PF> pickler) throws Exception;
    <T> T field(Field<T, PF> field) throws Exception;
}
