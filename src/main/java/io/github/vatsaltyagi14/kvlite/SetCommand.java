package io.github.vatsaltyagi14.kvlite;

import java.util.List;

public class SetCommand implements Command {
    @Override
    public String execute(List<String> args, Store store) {
        if (args.size() != 2) {
            return "-ERR wrong number of arguments for 'set' command";
        } else {
            String key = args.get(0);
            String value = args.get(1);
            store.set(key, value);
            return "+OK";
        }
    }
}
