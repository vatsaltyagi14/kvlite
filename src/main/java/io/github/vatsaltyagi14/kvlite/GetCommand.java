package io.github.vatsaltyagi14.kvlite;

import java.util.List;

public class GetCommand implements Command {
    @Override
    public String execute(List<String> args, Store store) {
        if (args.size() != 1) {
            return "-ERR wrong number of arguments for 'get' command";
        } else {
            String s = store.get(args.get(0));
            //String size = String.valueOf(s.length());

            if (s == null) {
                return "$-1";
            } else {
                return "$" + s.length() + "\r\n" + s;
            }
        }
    }
}
