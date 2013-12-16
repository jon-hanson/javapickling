package org.javapickling.tutorial.picklers;

import org.javapickling.core.*;
import org.javapickling.tutorial.model.Person;

import java.util.Date;

public class PersonPickler<PF> extends PicklerBase<Person, PF> {

    private final Field<String, PF> name = field("name", string_p());
    private final Field<Boolean, PF> isFemale = field("isFemale", boolean_p());
    private final Field<Date, PF> dateOfBirth = field("dateOfBirth", object_p(Date.class));

    public PersonPickler(PicklerCore<PF> core) {
        super(core, Person.class);
    }

    @Override
    public PF pickle(Person person, PF target) throws Exception {
        final FieldPickler<PF> fp = object_map().pickler(target);
        fp.field(name,          person.name);
        fp.field(isFemale,      person.isFemale);
        fp.field(dateOfBirth,   person.dateOfBirth);
        return fp.pickle(target);
    }

    @Override
    public Person unpickle(PF source) throws Exception {
        final FieldUnpickler<PF> fu = object_map().unpickler(source);
        return new Person(
                fu.field(name),
                fu.field(isFemale),
                fu.field(dateOfBirth)
            );
    }
}
