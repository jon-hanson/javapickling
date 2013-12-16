package org.javapickling.xml;

import com.google.common.base.Optional;
import org.javapickling.core.Pickler;
import org.javapickling.core.PicklerBase;
import org.javapickling.core.PicklerCore;
import org.w3c.dom.Node;

public class OptionalPickler<T> extends PicklerBase<Optional<T>, Node> {

    private final Pickler<T, Node> valuePickler;

    public OptionalPickler(PicklerCore<Node> core, Pickler<T, Node> valuePickler) {
        super(core, Optional.class);
        this.valuePickler = valuePickler;
    }

    @Override
    public Node pickle(Optional<T> optional, Node target) throws Exception {
        if (optional.isPresent()) {
            return valuePickler.pickle(optional.get(), target);
        } else {
            return null;
        }
    }

    @Override
    public Optional<T> unpickle(Node source) throws Exception {
        if (source != null) {
            return Optional.of(valuePickler.unpickle(source));
        } else {
            return Optional.absent();
        }
    }
}
