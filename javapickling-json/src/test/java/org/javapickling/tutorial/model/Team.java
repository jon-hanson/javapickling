package org.javapickling.tutorial.model;

import org.javapickling.core.DefaultPickler;
import org.javapickling.tutorial.picklers.TeamPickler;

import java.util.Map;
import java.util.Set;

@DefaultPickler(TeamPickler.class)
public class Team {
    public enum Role {ANALYST, DEVELOPER, TESTER};

    public final Optional<Person> leader;
    public final Map<Role, Set<Person>> members;

    public Team(Optional<Person> leader, Map<Role, Set<Person>> members) {
        this.leader = leader;
        this.members = members;
    }
}
