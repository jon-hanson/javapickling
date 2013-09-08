package org.javapickling.tutorial;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.javapickling.core.Pickler;
import org.javapickling.json.JsonNodePicklerCore;
import org.javapickling.tutorial.model.Optional;
import org.javapickling.tutorial.model.Person;
import org.javapickling.tutorial.model.Team;
import org.javapickling.tutorial.picklers.DatePickler;
import org.javapickling.xml.XmlNodePicklerCore;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public abstract class Main {

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
        for (Person person : persons) {
            set.add(person);
        }
        return set;
    }

    private static String pickleJson(Team team) throws Exception {

        final JsonNodePicklerCore jsonPickleCore = new JsonNodePicklerCore();
        jsonPickleCore.register(Date.class, DatePickler.class);

        final Pickler<Team, JsonNode> pickler = jsonPickleCore.object_p(Team.class);
        final JsonNode node = pickler.pickle(team, null);

        final Team teamB = pickler.unpickle(node);

        return JsonNodePicklerCore.nodeToString(node, true);
    }

    private static String pickleXml(Team team) throws Exception {

        final XmlNodePicklerCore xmlPickleCore = new XmlNodePicklerCore();
        xmlPickleCore.register(Date.class, DatePickler.class);

        final Element rootNode = xmlPickleCore.doc.createElement("team");
        xmlPickleCore.doc.appendChild(rootNode);

        final Pickler<Team, Node> pickler = xmlPickleCore.object_p(Team.class);
        final Node node = pickler.pickle(team, rootNode);

        final Team teamB = pickler.unpickle(node);

        return XmlNodePicklerCore.nodeToString(xmlPickleCore.doc, true);
    }

    public static void main(String[] args) {
        try {
            final Team team = createSample();

            System.out.println("JSON=");
            System.out.println(pickleJson(team));

            System.out.println("XML=");
            System.out.println(pickleXml(team));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
