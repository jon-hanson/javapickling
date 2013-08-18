package org.javapickling.common;

import org.javapickling.core.*;

import java.io.IOException;
import java.util.List;

public class PersonPickler<PF> extends ObjectPickler<Person, PF> {

    private final Pickler<List<Long>, PF> lngListPickler = core.list(core.lng());

    public PersonPickler(PicklerCore<PF> core) {
        super(core);
    }

    @Override
    public PF pickle(Person person, PF pf) throws IOException {
        final FieldPickler<PF> mp = core.map().pickler(pf);
        mp.str("name", person.name);
        mp.integer("age", person.age);
        mp.bool("female", person.female);
        mp.field("longs", person.longs, lngListPickler);
        return mp.pickle(pf);
    }

    @Override
    public Person unpickle(PF pf) throws IOException {
        final FieldUnpickler<PF> mu = core.map().unpickler(pf);
        final String name = mu.str("name", pf);
        final Integer age = mu.integer("age", pf);
        final boolean female = mu.bool("female", pf);
        final List<Long> longs = mu.field("longs", pf, lngListPickler);
        return new Person(name, age, female, longs);
    }
}
