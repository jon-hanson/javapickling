package org.javapickling.core;

import java.io.IOException;

public interface FieldUnpickler<PF> {

    <T> T field(String name, PF pf, Pickler<T, PF> pickler) throws IOException;

    Boolean boolean_f(String name, PF pf) throws IOException;
    Character char_f(String name, PF pf) throws IOException;
    String string_f(String name, PF pf) throws IOException;
    Integer integer_f(String name, PF pf) throws IOException;
    Long long_f(String name, PF pf) throws IOException;
    Double double_f(String name, PF pf) throws IOException;
}
