package org.javapickling.core;

import java.io.IOException;

public interface FieldUnpickler<PF> {

    <T> T field(String name, Pickler<T, PF> pickler) throws IOException;
    <T> T field(Field<T, PF> field) throws IOException;

    Boolean boolean_f(String name) throws IOException;
    Byte byte_f(String name) throws IOException;
    Character char_f(String name) throws IOException;
    Short short_f(String name) throws IOException;
    Long long_f(String name) throws IOException;
    Integer integer_f(String name) throws IOException;
    Float float_f(String name) throws IOException;
    Double double_f(String name) throws IOException;
    <T extends Enum<T>> T enum_f(String name, Class<T> clazz) throws IOException;
    String string_f(String name) throws IOException;
}
