package io.github.vatsaltyagi14.kvlite;

import java.util.List;
import java.util.Map;

public class CommandDispatcher {
    private final Store store;
    private final Map<String, Command> commands;

    public CommandDispatcher(Store store) {
        this.store = store;
        this.commands = Map.of(
                "PING", new PingCommand(),
                "GET", new GetCommand(),
                "SET", new SetCommand(),
                "DEL", new DelCommand()
        );
    }

    public String dispatch(List<String> parts) {
        if (parts.isEmpty()) {
            return "-ERR empty command";
        } else {
            String name = parts.get(0);
            Command command = commands.get(name.toUpperCase());
            if (command == null) {
                return "-ERR unknown command" + " " + name;
            } else {
                List<String> argsList = parts.subList(1, parts.size());
                return command.execute(argsList, store);
            }
        }
    }
}
