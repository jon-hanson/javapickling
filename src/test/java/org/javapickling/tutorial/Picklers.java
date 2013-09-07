package org.javapickling.tutorial;

import org.javapickling.core.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class Picklers {
    static public class DatePickler<PF> extends PicklerBase<Date, PF> {

        private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        public DatePickler(PicklerCore<PF> core) {
            super(core);
        }

        @Override
        public PF pickle(Date date, PF target) throws Exception {
            return string_p().pickle(df.format(date), target);
        }

        @Override
        public Date unpickle(PF source) throws Exception {
            return df.parse(string_p().unpickle(source));
        }
    }

    static public class PersonPickler<PF> extends PicklerBase<Model.Person, PF> {

        private final Field<String, PF> name = field("name", string_p());
        private final Field<Boolean, PF> isFemale = field("isFemale", boolean_p());
        private final Field<Date, PF> dateOfBirth = field("dateOfBirth", object_p(Date.class));

        public PersonPickler(PicklerCore<PF> core) {
            super(core);
        }

        @Override
        public PF pickle(Model.Person person, PF target) throws Exception {
            final FieldPickler<PF> mp = object_map().pickler(target);
            mp.field(name,          person.name);
            mp.field(isFemale,      person.isFemale);
            mp.field(dateOfBirth,   person.dateOfBirth);
            return mp.pickle(target);
        }

        @Override
        public Model.Person unpickle(PF source) throws Exception {
            final FieldUnpickler<PF> mu = object_map().unpickler(source);
            return new Model.Person(
                    mu.field(name),
                    mu.field(isFemale),
                    mu.field(dateOfBirth)
                );
        }
    }

    static public class OptionalPickler<PF, T> extends PicklerBase<Model.Optional<T>, PF> {

        private final Pickler<T, PF> valPickler;

        public OptionalPickler(PicklerCore<PF> core, Pickler<T, PF> valPickler) {
            super(core);
            this.valPickler = nullable(valPickler);
        }

        @Override
        public PF pickle(Model.Optional<T> optional, PF target) throws Exception {
            return valPickler.pickle(optional.orElse(null), target);
        }

        @Override
        public Model.Optional<T> unpickle(PF source) throws Exception {
            return new Model.Optional<T>(valPickler.unpickle(source));
        }
    }

    static public class TeamPickler<PF> extends PicklerBase<Model.Team, PF> {

        private final Field<Model.Team.Role, PF> role = field("role", enum_p(Model.Team.Role.class));
        private final Pickler<Model.Optional<Model.Person>, PF> optPersonPickler = generic_p(Model.Optional.class, object_p(Model.Person.class));
        private final Field<Model.Optional<Model.Person>, PF> leader = field("leader", optPersonPickler);
        private final Field<Map<Model.Team.Role, Set<Model.Person>>, PF> members =
                field(
                    "members",
                    map_p(
                        enum_p(Model.Team.Role.class),
                        set_p(
                            object_p(Model.Person.class),
                            TreeSet.class),
                        TreeMap.class));

        public TeamPickler(PicklerCore<PF> core) {
            super(core);
        }

        @Override
        public PF pickle(Model.Team team, PF target) throws Exception {
            final FieldPickler<PF> mp = object_map().pickler(target);
            mp.field(leader,    team.leader);
            mp.field(members,   team.members);
            return mp.pickle(target);
        }

        @Override
        public Model.Team unpickle(PF source) throws Exception {
            final FieldUnpickler<PF> mu = object_map().unpickler(source);
            return new Model.Team(
                    mu.field(leader),
                    mu.field(members)
                );
        }
    }
}
