package org.javapickling.core;

import java.io.IOException;

public interface FieldPickler<PF> {

    <T> void field(String name, T value, Pickler<T, PF> pickler) throws IOException;

    void bool(String name, Boolean value) throws IOException;
    void chr(String name, Character value) throws IOException;
    void str(String name, String value) throws IOException;
    void integer(String name, Integer value) throws IOException;
    void lng(String name, Long value) throws IOException;
    void dbl(String name, Double value) throws IOException;

    PF pickle(PF pf) throws IOException;
}
