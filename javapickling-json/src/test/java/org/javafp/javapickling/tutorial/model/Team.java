package org.javafp.javapickling.tutorial.model;

import org.javafp.javapickling.core.DefaultPickler;
import org.javafp.javapickling.tutorial.picklers.TeamPickler;

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
