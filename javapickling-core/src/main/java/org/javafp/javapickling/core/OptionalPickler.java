package org.javafp.javapickling.core;

import com.google.common.base.Optional;

public class OptionalPickler<PF, T> extends PicklerBase<Optional<T>, PF> {

    private final Pickler<T, PF> valuePickler;

    public OptionalPickler(PicklerCore<PF> core, Pickler<T, PF> valuePickler) {
        super(core, Optional.class);
        this.valuePickler = valuePickler;
    }

    @Override
    public PF pickle(Optional<T> optional, PF target) throws Exception {
        final PF target2 = boolean_p().pickle(optional.isPresent(), target);
        if (optional.isPresent()) {
            return valuePickler.pickle(optional.get(), target2);
        } else {
            return target2;
        }
    }

    @Override
    public Optional<T> unpickle(PF source) throws Exception {
        if (boolean_p().unpickle(source)) {
            return Optional.of(valuePickler.unpickle(source));
        } else {
            return Optional.absent();
        }
    }
}
