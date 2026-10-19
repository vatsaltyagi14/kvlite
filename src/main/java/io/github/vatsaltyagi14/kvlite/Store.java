package io.github.vatsaltyagi14.kvlite;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

public class Store {
    private final Map<String, Entry> data = new HashMap<>(); // here we did new because we wanted the class to own data but for clock's case it is passed as param
    private final Clock clock;

    public Store(Clock clock) {
        this.clock = clock;
    }

    public String get(String key) {
        Entry entry = data.get(key); // we could have first done containskey and then get through if and else but that takes two hashtable lookups, this approach does only one lookup.
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public void set(String key, String value) {
        Entry entry = new Entry(value);
        data.put(key, entry);
    }

    public boolean delete(String key) {
        return data.remove(key) != null;
    }

}
