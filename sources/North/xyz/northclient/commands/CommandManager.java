package xyz.northclient.commands;


import xyz.northclient.NorthSingleton;
import xyz.northclient.commands.impl.CommandBind;
import xyz.northclient.commands.impl.CommandConfig;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();


    public void stop() {

    }

    public CommandManager() {
        commands.add(new CommandBind());
        commands.add(new CommandConfig());
    }

    public List<Command> getCommands() {
        return commands;
    }

    public boolean onSendMessage(final String message) {
        if (message.startsWith(".")) {
            String withoutPrefix = message.substring(1);
            String[] args = withoutPrefix.split(" ");
            if (!withoutPrefix.isEmpty() && args.length > 0) {
                String commmand = withoutPrefix.substring(args[0].length()).trim();
                for (Command command : commands) {
                        if (command.getAliases().equalsIgnoreCase(args[0])) {
                            command.execute(commmand, args);
                            return true;
                        }
                }
                NorthSingleton.logChat("'" + args[0] + "' is not a command.");
            } else {
                NorthSingleton.logChat("No arguments provided!");
            }
            return true;
        }
        return false;
    }


}
