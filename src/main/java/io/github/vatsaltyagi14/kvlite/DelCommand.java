package io.github.vatsaltyagi14.kvlite;

import java.util.List;

public class DelCommand implements Command {
    @Override
    public String execute(List<String> args, Store store) {
        if (args.isEmpty()) {
            return "-ERR wrong number of arguments for 'del' command";
        } else {
            int count = 0;
            for (String key : args) {
                if (store.delete(key) != false) {
                    count++;
                }
            }
            return ":" + count;
        }
    }

}
