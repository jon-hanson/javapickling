package org.javafp.javapickling.tutorial.picklers;

import org.javafp.javapickling.core.*;
import org.javafp.javapickling.tutorial.model.Optional;
import org.javafp.javapickling.tutorial.model.Person;
import org.javafp.javapickling.tutorial.model.Team;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class TeamPickler<PF> extends PicklerBase<Team, PF> {

    private final Pickler<Optional<Person>, PF> optPersonPickler = generic_p(Optional.class, object_p(Person.class));
    private final Field<Optional<Person>, PF> leader = field("leader", optPersonPickler);
    private final Field<Map<Team.Role, Set<Person>>, PF> members =
            field(
                "members",
                map_p(
                    enum_p(Team.Role.class),
                    set_p(
                        object_p(Person.class),
                        TreeSet.class),
                    TreeMap.class));

    public TeamPickler(PicklerCore<PF> core) {
        super(core, Team.class);
    }

    @Override
    public PF pickle(Team team, PF target) throws Exception {
        final FieldPickler<PF> fp = object_map().pickler(target);
        fp.field(leader,    team.leader);
        fp.field(members,   team.members);
        return fp.pickle(target);
    }

    @Override
    public Team unpickle(PF source) throws Exception {
        final FieldUnpickler<PF> fu = object_map().unpickler(source);
        return new Team(
                fu.field(leader),
                fu.field(members)
            );
    }
}
