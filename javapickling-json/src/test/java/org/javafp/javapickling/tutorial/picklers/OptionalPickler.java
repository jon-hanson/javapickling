package org.javafp.javapickling.tutorial.picklers;

import org.javafp.javapickling.core.Pickler;
import org.javafp.javapickling.core.PicklerBase;
import org.javafp.javapickling.core.PicklerCore;
import org.javafp.javapickling.tutorial.model.Optional;

public class OptionalPickler<PF, T> extends PicklerBase<Optional<T>, PF> {

    private final Pickler<T, PF> valPickler;

    public OptionalPickler(PicklerCore<PF> core, Pickler<T, PF> valPickler) {
        super(core, Optional.class);
        this.valPickler = valPickler;
    }

    @Override
    public PF pickle(Optional<T> optional, PF target) throws Exception {
        return valPickler.pickle(optional.orElse(null), target);
    }

    @Override
    public Optional<T> unpickle(PF source) throws Exception {
        return new Optional<T>(valPickler.unpickle(source));
    }
}
