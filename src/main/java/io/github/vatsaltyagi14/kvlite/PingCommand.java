package io.github.vatsaltyagi14.kvlite;

import java.util.List;

public class PingCommand implements Command {
    @Override
    public String execute(List<String> args, Store store){
        if(args.isEmpty()){
            return "+PONG";
        }
        else{
            return "-ERR wrong number of arguments for 'ping' command";
        }
    }
}
