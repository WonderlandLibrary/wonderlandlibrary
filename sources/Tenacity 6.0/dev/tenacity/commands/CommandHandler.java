// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands;

import java.util.Iterator;
import dev.tenacity.utils.player.ChatUtil;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class CommandHandler
{
    public static String CHAT_PREFIX;
    public List<Command> commands;
    
    public CommandHandler() {
        this.commands = new ArrayList<Command>();
    }
    
    public boolean execute(final String txt) {
        if (!txt.startsWith(CommandHandler.CHAT_PREFIX)) {
            return false;
        }
        final String[] arguments = txt.substring(1).split(" ");
        for (final Command command : this.commands) {
            if (command.getName().equalsIgnoreCase(arguments[0]) || Arrays.stream(command.getOtherPrefixes()).anyMatch(p -> p.equalsIgnoreCase(arguments[0]))) {
                command.execute(Arrays.copyOfRange(arguments, 1, arguments.length));
                return true;
            }
        }
        ChatUtil.print("Invalid command.");
        return false;
    }
    
    public List<Command> getCommands() {
        return this.commands;
    }
    
    public Command getCommand(final Class<? extends Command> command) {
        return this.getCommands().stream().filter(com -> command == com.getClass()).findFirst().orElse(null);
    }
    
    static {
        CommandHandler.CHAT_PREFIX = ".";
    }
}
