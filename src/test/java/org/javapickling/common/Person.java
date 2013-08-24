package org.javapickling.common;

import java.io.Serializable;
import java.util.List;

public class Person<T> implements Serializable {

    public final String name;
    public final Integer age;
    public final boolean female;
    public final List<Long> longs;
    public final T test;
    public final Object obj;

    public Person(String name, Integer age, boolean female, List<Long> longs, T test, Object obj) {
        this.name = name;
        this.age = age;
        this.female = female;
        this.longs = longs;
        this.test = test;
        this.obj = obj;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person))
            return false;

        final Person rhs = (Person)obj;

        if (!name.equals(rhs.name))
            return false;
        if (!age.equals(rhs.age))
            return false;
        if (female != rhs.female)
            return false;

        if (longs.size() != rhs.longs.size())
            return false;

        for (int i = 0; i < longs.size(); ++i) {
            if (longs.get(i).equals(rhs.longs.get(i))) {
                return false;
            }
        }

        if (test != rhs.test)
            return false;

        return obj.equals(rhs.obj);
    }
}
