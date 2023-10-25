package fun.rich.client.command;

import fun.rich.client.command.impl.*;
import fun.rich.client.event.EventManager;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        EventManager.register(new CommandHandler(this));
        commands.add(new ConfigCommand());
        commands.add(new MacroCommand());
        commands.add(new FriendCommand());
        commands.add(new FakeNameCommand());
        commands.add(new HelpCommand());
        commands.add(new ParseCommand());
        commands.add(new PanicCommand());
        commands.add(new GPSCommand());
        commands.add(new ClipCommand());
        commands.add(new BindCommand());
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    public boolean execute(String args) {
        String noPrefix = args.substring(1);
        String[] split = noPrefix.split(" ");
        if (split.length > 0) {
            for (Command command : commands) {
                CommandAbstract abstractCommand = (CommandAbstract) command;
                String[] commandAliases = abstractCommand.getAliases();
                for (String alias : commandAliases) {
                    if (split[0].equalsIgnoreCase(alias)) {
                        abstractCommand.execute(split);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}