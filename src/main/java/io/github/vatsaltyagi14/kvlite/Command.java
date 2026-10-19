package io.github.vatsaltyagi14.kvlite;

import java.util.List;

public interface Command {
    String execute(List<String> args, Store store);
}
