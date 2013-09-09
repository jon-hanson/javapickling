package org.javapickling.tutorial.model;

import org.javapickling.core.DefaultPickler;
import org.javapickling.tutorial.picklers.PersonPickler;

import java.util.Date;

@DefaultPickler(PersonPickler.class)
public class Person implements Comparable<Person> {
    public final String name;
    public final boolean isFemale;
    public final Date dateOfBirth;

    public Person(String name, boolean isFemale, Date dateOfBirth) {
        this.name = name;
        this.isFemale = isFemale;
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public int compareTo(Person rhs) {
        return name.compareTo(rhs.name);
    }

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs)
            return true;
        if (rhs == null || getClass() != rhs.getClass())
            return false;

        Person person = (Person) rhs;

        if (name != null ? !name.equals(person.name) : person.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
