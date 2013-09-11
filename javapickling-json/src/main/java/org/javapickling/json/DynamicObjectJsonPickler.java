package org.javapickling.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.javapickling.core.DynamicObjectPickler;
import org.javapickling.core.MetaType;
import org.javapickling.core.PicklerCore;

public class DynamicObjectJsonPickler<T> extends DynamicObjectPickler<JsonNode, T> {

    public DynamicObjectJsonPickler(PicklerCore<JsonNode> core) {
        super(core);
    }

    @Override
    protected OptimalResult<JsonNode> optimalPickle(MetaType metaType, T obj, JsonNode target) throws Exception {
        switch(metaType.typeKind) {
            case NULL:
                return OptimalResult.success(null_p().pickle(obj, target));
            case BOOLEAN:
                return OptimalResult.success(boolean_p().pickle((Boolean)obj, target));
            case STRING:
                return OptimalResult.success(string_p().pickle((String)obj, target));
            case DOUBLE:
                return OptimalResult.success(double_p().pickle((Double)obj, target));
            default:
                return OptimalResult.failure();
        }
    }

    @Override
    protected OptimalResult<T> optimalUnpickle(JsonNode source) throws Exception {
        if (source instanceof NullNode) {
            return OptimalResult.success(null_p().unpickle(source));
        } else if (source instanceof BooleanNode) {
            return OptimalResult.success(boolean_p().unpickle(source));
        } else if (source instanceof TextNode) {
            return OptimalResult.success(string_p().unpickle(source));
        } else if (NumericNode.class.isAssignableFrom(source.getClass())) {
            return OptimalResult.success(double_p().unpickle(source));
        } else {
            return OptimalResult.failure();
        }
    }
}
