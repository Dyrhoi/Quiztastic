package examples;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MapTest {

    @Test
    void aSimpleMapExample () {
        Map<String, String> favoritePets = new HashMap<>();

        favoritePets.put("Christian", "Kvast");
        favoritePets.put("Harry Potter", "Hedwig");
        favoritePets.put("Tin Tin", "Terry");

        // We can get a value
        assertEquals("Kvast", favoritePets.get("Christian"));

        // Stuff not there is null
        assertNull(favoritePets.get("Peter Pan"));

        // We can also get the names (keys)
        Set<String> names = favoritePets.keySet();
        assertEquals(Set.of("Christian", "Harry Potter", "Tin Tin"), names);

        // And, the pets (values)
        Set<String> pets  = Set.copyOf(favoritePets.values());
        assertEquals(Set.of("Kvast", "Hedwig", "Terry"), pets);

        // Remember if you need a list you can convert into one:
        List<String> listyNames = List.copyOf(favoritePets.keySet());
    }

    @Test
    void mapGrouping() {
        // We group things in a map
        List<String> names = List.of("Rip And",
                "Rap And",
                "Rup And",
                "Anders And",
                "Matilte Malkeko",
                "Maren Malkeko",
                "Peter Pan");

        Map<String, Set<String>> families = new HashMap<>();

        for (String name : names) {
            String[] words = name.split(" ");
            String firstName = words[0], lastName = words[1];
            // Singleton patern-ish
            Set<String> current = families.get(lastName);
            if (current == null) {
                current = new HashSet<>();
                families.put(lastName, current);
            }
            current.add(firstName);
        }

        assertEquals(Set.of("Rip", "Rap", "Rup", "Anders"),
                families.get("And"));
        assertEquals(Set.of("Peter"), families.get("Pan"));
    }
}
