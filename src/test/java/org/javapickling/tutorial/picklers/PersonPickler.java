package org.javapickling.tutorial.picklers;

import org.javapickling.core.*;
import org.javapickling.tutorial.model.Person;

import java.util.Date;

public class PersonPickler<PF> extends PicklerBase<Person, PF> {

    private final Field<String, PF> name = field("name", string_p());
    private final Field<Boolean, PF> isFemale = field("isFemale", boolean_p());
    private final Field<Date, PF> dateOfBirth = field("dateOfBirth", object_p(Date.class));

    public PersonPickler(PicklerCore<PF> core) {
        super(core);
    }

    @Override
    public PF pickle(Person person, PF target) throws Exception {
        final FieldPickler<PF> mp = object_map().pickler(target);
        mp.field(name,          person.name);
        mp.field(isFemale,      person.isFemale);
        mp.field(dateOfBirth,   person.dateOfBirth);
        return mp.pickle(target);
    }

    @Override
    public Person unpickle(PF source) throws Exception {
        final FieldUnpickler<PF> mu = object_map().unpickler(source);
        return new Person(
                mu.field(name),
                mu.field(isFemale),
                mu.field(dateOfBirth)
            );
    }
}
