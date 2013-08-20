package org.javapickling.core;

import java.io.IOException;

public interface FieldPickler<PF> {

    <T> void field(String name, T value, Pickler<T, PF> pickler) throws IOException;

    void boolean_f(String name, Boolean value) throws IOException;
    void char_f(String name, Character value) throws IOException;
    void string_f(String name, String value) throws IOException;
    void integer_f(String name, Integer value) throws IOException;
    void long_f(String name, Long value) throws IOException;
    void double_f(String name, Double value) throws IOException;

    PF pickle(PF pf) throws IOException;
}
