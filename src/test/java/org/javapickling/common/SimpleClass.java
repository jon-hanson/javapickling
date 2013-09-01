package org.javapickling.common;

import org.javapickling.core.*;

import java.io.IOException;

public class SimpleClass {
    public static final class Pickler<PF> extends PicklerBase<SimpleClass, PF> {

        private final Field<String, PF> nameF = field("name", string_p());
        private final Field<Integer, PF> idF = field("id", integer_p());

        protected Pickler(PicklerCore<PF> core) {
            super(core);
        }

        @Override
        public PF pickle(SimpleClass value, PF target) throws IOException {
            final FieldPickler<PF> mp = core.object_map().pickler(target);
            mp.field(nameF, value.name);
            return mp.pickle(target);
        }

        @Override
        public SimpleClass unpickle(PF source) throws IOException {
            final FieldUnpickler<PF> mu = core.object_map().unpickler(source);
            return new SimpleClass(
                mu.field(nameF),
                mu.field(idF)
            );
        }
    }

    public final String name;
    public final int id;

    public SimpleClass(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
