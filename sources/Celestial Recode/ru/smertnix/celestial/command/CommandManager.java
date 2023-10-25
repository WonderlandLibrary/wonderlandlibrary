package ru.smertnix.celestial.command;

import java.util.ArrayList;
import java.util.List;

import ru.smertnix.celestial.command.impl.*;
import ru.smertnix.celestial.event.EventManager;

public class CommandManager {

    private final ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        EventManager.register(new CommandHandler(this));
        commands.add(new ConfigCommand());
        commands.add(new MacroCommand());
        commands.add(new FriendCommand());
        commands.add(new HelpCommand());
        commands.add(new ParseCommand());
        commands.add(new PanicCommand());
        commands.add(new GPSCommand());
        commands.add(new ClipCommand());
        commands.add(new BindCommand());
        commands.add(new TpCommand());
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