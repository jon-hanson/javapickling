package org.javapickling.byteio;

import com.google.common.base.Optional;
import org.javapickling.core.Pickler;
import org.javapickling.core.PicklerBase;
import org.javapickling.core.PicklerCore;

public class OptionalPickler<T> extends PicklerBase<Optional<T>, ByteIO> {

    private final Pickler<T, ByteIO> valuePickler;

    public OptionalPickler(PicklerCore<ByteIO> core, Pickler<T, ByteIO> valuePickler) {
        super(core, Optional.class);
        this.valuePickler = valuePickler;
    }

    @Override
    public ByteIO pickle(Optional<T> optional, ByteIO target) throws Exception {
        final ByteIO target2 = boolean_p().pickle(optional.isPresent(), target);
        if (optional.isPresent()) {
            return valuePickler.pickle(optional.get(), target2);
        } else {
            return target2;
        }
    }

    @Override
    public Optional<T> unpickle(ByteIO source) throws Exception {
        if (boolean_p().unpickle(source)) {
            return Optional.of(valuePickler.unpickle(source));
        } else {
            return Optional.absent();
        }
    }
}
