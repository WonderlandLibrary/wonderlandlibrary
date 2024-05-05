// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.cmd;

import java.util.Iterator;
import java.util.List;
import ru.fluger.client.cmd.impl.BindCommand;
import ru.fluger.client.cmd.impl.ClipCommand;
import ru.fluger.client.cmd.impl.PanicCommand;
import ru.fluger.client.cmd.impl.HelpCommand;
import ru.fluger.client.cmd.impl.FriendCommand;
import ru.fluger.client.cmd.impl.MacroCommand;
import ru.fluger.client.cmd.impl.ConfigCommand;
import ru.fluger.client.event.EventManager;
import java.util.ArrayList;

public class CommandManager
{
    private final ArrayList<Command> commands;
    
    public CommandManager() {
        this.commands = new ArrayList<Command>();
        EventManager.register(new CommandHandler(this));
        this.commands.add(new ConfigCommand());
        this.commands.add(new MacroCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new PanicCommand());
        this.commands.add(new ClipCommand());
        this.commands.add(new BindCommand());
    }
    
    public List<Command> getCommands() {
        return this.commands;
    }
    
    public boolean execute(final String args) {
        final String noPrefix = args.substring(1);
        final String[] split = noPrefix.split(" ");
        if (split.length > 0) {
            for (final Command command : this.commands) {
                final CommandAbstract abstractCommand = (CommandAbstract)command;
                final String[] aliases;
                final String[] commandAliases = aliases = abstractCommand.getAliases();
                for (final String alias : aliases) {
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
