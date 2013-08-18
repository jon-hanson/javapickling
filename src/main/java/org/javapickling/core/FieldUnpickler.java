package org.javapickling.core;

import java.io.IOException;

public interface FieldUnpickler<PF> {

    <T> T field(String name, PF pf, Pickler<T, PF> pickler) throws IOException;

    Boolean bool(String name, PF pf) throws IOException;
    Character chr(String name, PF pf) throws IOException;
    String str(String name, PF pf) throws IOException;
    Integer integer(String name, PF pf) throws IOException;
    Long lng(String name, PF pf) throws IOException;
    Double dbl(String name, PF pf) throws IOException;
}
