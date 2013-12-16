package org.javapickling.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import org.javapickling.core.Pickler;
import org.javapickling.core.PicklerBase;
import org.javapickling.core.PicklerCore;

public class OptionalPickler<T> extends PicklerBase<Optional<T>, JsonNode> {

    private final Pickler<T, JsonNode> valuePickler;

    public OptionalPickler(PicklerCore<JsonNode> core, Pickler<T, JsonNode> valuePickler) {
        super(core, Optional.class);
        this.valuePickler = valuePickler;
    }

    @Override
    public JsonNode pickle(Optional<T> optional, JsonNode target) throws Exception {
        if (optional.isPresent()) {
            return valuePickler.pickle(optional.get(), target);
        } else {
            return ((JsonNodePicklerCore)core).nodeFactory.nullNode();
        }
    }

    @Override
    public Optional<T> unpickle(JsonNode source) throws Exception {
        if (source != null && !source.isNull()) {
            return Optional.of(valuePickler.unpickle(source));
        } else {
            return Optional.absent();
        }
    }
}
