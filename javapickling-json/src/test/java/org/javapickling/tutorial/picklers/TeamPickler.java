package org.javapickling.tutorial.picklers;

import org.javapickling.core.*;
import org.javapickling.tutorial.model.Optional;
import org.javapickling.tutorial.model.Person;
import org.javapickling.tutorial.model.Team;

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
        super(core);
    }

    @Override
    public PF pickle(Team team, PF target) throws Exception {
        final FieldPickler<PF> mp = object_map().pickler(target);
        mp.field(leader,    team.leader);
        mp.field(members,   team.members);
        return mp.pickle(target);
    }

    @Override
    public Team unpickle(PF source) throws Exception {
        final FieldUnpickler<PF> mu = object_map().unpickler(source);
        return new Team(
                mu.field(leader),
                mu.field(members)
            );
    }
}
