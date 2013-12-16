package org.javapickling.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import org.javapickling.core.DynamicObjectPickler;
import org.javapickling.core.MetaType;
import org.javapickling.core.PicklerCore;

import java.util.List;

public class DynamicObjectJsonNodePickler<T> extends DynamicObjectPickler<T, JsonNode> {

    public DynamicObjectJsonNodePickler(PicklerCore<JsonNode> core, Class<? super T> clazz) {
        super(core, clazz);
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
            case LIST:
                return OptimalResult.success(list_p(d_object_p()).pickle((List)obj, target));
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
        } else if (source instanceof ArrayNode) {
            return OptimalResult.success(list_p(d_object_p()).unpickle(source));
        } else {
            return OptimalResult.failure();
        }
    }
}
