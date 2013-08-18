package org.javapickling.common;

import org.javapickling.core.*;

import java.io.IOException;
import java.util.List;

public class HousePickler<PF> extends ObjectPickler<House, PF> {

    private final Pickler<List<Person>, PF> personsPickler;

    public HousePickler(PicklerCore<PF> core) {
        super(core);
        this.personsPickler = core.list(core.object(Person.class));
    }

    @Override
    public PF pickle(House house, PF pf) throws IOException {
        final FieldPickler<PF> mp = core.map().pickler(pf);
        mp.field("volume", house.volume, core.dbl());
        mp.field("occupants", house.occupants, personsPickler);
        return mp.pickle(pf);
    }

    @Override
    public House unpickle(PF pf) throws IOException {
        final FieldUnpickler<PF> mu = core.map().unpickler(pf);
        final double volume = mu.field("volume", pf, core.dbl());
        final List<Person> occupants = mu.field("occupants", pf, personsPickler);
        return new House(volume, occupants);
    }
}
