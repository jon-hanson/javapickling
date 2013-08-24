package org.javapickling.common;

import org.javapickling.core.*;

import java.io.IOException;
import java.util.List;

public class PersonPickler<T, PF> extends PicklerBase<Person<T>, PF> {

    private final Pickler<List<Long>, PF> lngListPickler = core.list_p(core.long_p());

    public PersonPickler(PicklerCore<PF> core) {
        super(core);
    }

    @Override
    public PF pickle(Person<T> person, PF target) throws IOException {
        final FieldPickler<PF> mp = core.object_map().pickler(target);
        mp.string_f("name", person.name);
        mp.integer_f("age", person.age);
        mp.boolean_f("female", person.female);
        mp.field("longs", person.longs, lngListPickler);
        mp.field("test", person.test, core.object_p());
        return mp.pickle(target);
    }

    @Override
    public Person<T> unpickle(PF source) throws IOException {
        final FieldUnpickler<PF> mu = core.object_map().unpickler(source);
        final String name = mu.string_f("name", source);
        final Integer age = mu.integer_f("age", source);
        final boolean female = mu.boolean_f("female", source);
        final List<Long> longs = mu.field("longs", source, lngListPickler);
        final T test = (T)mu.field("test", source, core.object_p());
        return new Person<T>(name, age, female, longs, test);
    }
}
