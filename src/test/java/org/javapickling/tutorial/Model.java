package org.javapickling.tutorial;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.javapickling.core.DefaultPickler;
import org.javapickling.core.Pickler;
import org.javapickling.json.JsonPicklerCore;

import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public abstract class Model {
    @DefaultPickler(Picklers.PersonPickler.class)
    static public class Person implements Comparable<Person> {
        public final String name;
        public final boolean isFemale;
        public final Date dateOfBirth;

        Person(String name, boolean isFemale, Date dateOfBirth) {
            this.name = name;
            this.isFemale = isFemale;
            this.dateOfBirth = dateOfBirth;
        }

        @Override
        public int compareTo(Person rhs) {
            return name.compareTo(rhs.name);
        }

        @Override
        public boolean equals(Object rhs) {
            if (this == rhs)
                return true;
            if (rhs == null || getClass() != rhs.getClass())
                return false;

            Person person = (Person) rhs;

            if (name != null ? !name.equals(person.name) : person.name != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }

    @DefaultPickler(Picklers.OptionalPickler.class)
    static public class Optional<T> {
        private final T value;

        public Optional(T value) {
            this.value = value;
        }

        public T orElse(T alt) {
            return value == null ? alt : value;
        }

        public boolean isPresent() {
            return value != null;
        }

        public T getValue() {
            if (value == null) {
                throw new NoSuchElementException("Optional value is not present");
            } else {
                return value;
            }
        }
    }

    @DefaultPickler(Picklers.TeamPickler.class)
    static public class Team {
        public enum Role {ANALYST, DEVELOPER, TESTER};
        public final Optional<Person> leader;
        public final Map<Role, Set<Person>> members;

        public Team(Optional<Person> leader, Map<Role, Set<Person>> members) {
            this.leader = leader;
            this.members = members;
        }
    }

    public static Team createSample() {
        final Person amy = new Person("Amy", true, new Date(72, 8, 3));
        final Person bill = new Person("Bill", false, new Date(70, 3, 1));
        final Person claire = new Person("Claire", true, new Date(71, 1, 1));
        final Person david = new Person("David", false, new Date(78, 11, 31));
        final Person emi = new Person("Emi", true, new Date(75, 5, 15));
        final Optional<Person> leader = new Optional<Person>(amy);
        final Map<Team.Role, Set<Person>> members = Maps.newTreeMap();
        members.put(Team.Role.ANALYST, create(bill));
        members.put(Team.Role.DEVELOPER, create(claire, david));
        members.put(Team.Role.TESTER, create(david, emi));
        return new Team(leader, members);
    }

    private static Set<Person> create(Person... persons) {
        final Set<Person> set = Sets.newTreeSet();
        for (Person person : persons)
            set.add(person);
        return set;
    }

    private static String jsonToString(JsonNode node) throws JsonProcessingException {
        final ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return writer.writeValueAsString(node);
    }

    public static void main(String[] args) {
        try {
            final JsonPicklerCore jsonPickleCore = new JsonPicklerCore();
            jsonPickleCore.register(Date.class, Picklers.DatePickler.class);

            final Pickler<Team, JsonNode> teamPickler = jsonPickleCore.object_p(Team.class);

            final Team teamA = createSample();
            final JsonNode node = teamPickler.pickle(teamA, null);

            System.out.println(jsonToString(node));

            final Team teamB = teamPickler.unpickle(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
