package org.javapickling.common;

import org.javapickling.core.*;

import java.io.IOException;
import java.util.List;

public class HousePickler<PF> extends ObjectPickler<House, PF> {

    private final Pickler<List<Object>, PF> personsPickler;

    public HousePickler(PicklerCore<PF> core) {
        super(core);
        this.personsPickler = core.list_p(core.object_p());
    }

    @Override
    public PF pickle(House house, PF pf) throws IOException {
        final FieldPickler<PF> mp = core.object_map().pickler(pf);
        mp.field("type", house.type, core.enum_p(House.Type.class, House.Type.values()));
        mp.field("volume", house.volume, core.double_p());
        mp.field("occupants", house.occupants, personsPickler);
        return mp.pickle(pf);
    }

    @Override
    public House unpickle(PF pf) throws IOException {
        final FieldUnpickler<PF> mu = core.object_map().unpickler(pf);
        final House.Type type = mu.field("type", pf, core.enum_p(House.Type.class, House.Type.values()));
        final double volume = mu.field("volume", pf, core.double_p());
        final List<Object> occupants = mu.field("occupants", pf, personsPickler);
        return new House(type, volume, occupants);
    }
}
