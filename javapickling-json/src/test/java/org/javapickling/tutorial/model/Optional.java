package org.javapickling.tutorial.model;

import org.javapickling.core.DefaultPickler;
import org.javapickling.tutorial.picklers.OptionalPickler;

import java.util.NoSuchElementException;

@DefaultPickler(OptionalPickler.class)
public class Optional<T> {
    private final T value;

    public Optional(T value) {
        this.value = value;
    }

    public T orElse(T alt) {
        return value == null ? alt : value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public T getValue() {
        if (value == null) {
            throw new NoSuchElementException("Optional value is not present");
        } else {
            return value;
        }
    }
}
