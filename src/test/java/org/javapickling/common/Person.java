package org.javapickling.common;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable {

    public final String name;
    public final Integer age;
    public final boolean female;
    public final List<Long> longs;

    public Person(String name, Integer age, boolean female, List<Long> longs) {
        this.name = name;
        this.age = age;
        this.female = female;
        this.longs = longs;
    }
}
