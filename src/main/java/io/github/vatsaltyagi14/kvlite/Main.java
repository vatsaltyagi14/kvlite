package io.github.vatsaltyagi14.kvlite;

import java.time.Clock;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Store store = new Store(Clock.systemUTC());
        CommandDispatcher dispatcher = new CommandDispatcher(store);

        String[] lines = {"PING", "SET name vatsal", "GET name", "GET age", "SET name tyagi",
                "get name", "DEL name nope", "get", "foo", "get name"};

        for (String line : lines) {
            List<String> parts = List.of(line.split(" "));
            String reply = dispatcher.dispatch(parts);
            System.out.println(line + "  ->  " + reply.replace("\r\n", "\\r\\n"));
        }
    }
}
