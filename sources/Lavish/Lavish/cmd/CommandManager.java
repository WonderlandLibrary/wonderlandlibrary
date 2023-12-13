// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.cmd;

import Lavish.cmd.impl.ClientName;
import Lavish.cmd.impl.Config;
import Lavish.cmd.impl.Toggle;
import Lavish.cmd.impl.VClip;
import Lavish.cmd.impl.Bind;
import Lavish.cmd.impl.Help;
import java.util.ArrayList;
import java.util.List;

public class CommandManager
{
    private static final List<Command> commands;
    public static final String prefix;
    
    static {
        prefix = ".";
        commands = new ArrayList<Command>();
    }
    
    public static void loadCommands() {
        CommandManager.commands.add(new Command("help", "List of all the commands you can do!", new Help()));
        CommandManager.commands.add(new Command("bind", "Bind Modules to key", new Bind()));
        CommandManager.commands.add(new Command("vclip", "Goes under or above blocks!", new VClip()));
        CommandManager.commands.add(new Command("toggle", "Toggle modules on or off", new Toggle()));
        CommandManager.commands.add(new Command("t", "Alias to .toggle", new Toggle()));
        CommandManager.commands.add(new Command("config", "Loads / Saves configs", new Config()));
        CommandManager.commands.add(new Command("clientname", "Changes the client name on HUD", new ClientName()));
    }
    
    public static List<String> getArgs(final String text) {
        if (!isCommand(text)) {
            return new ArrayList<String>();
        }
        final List<String> args = new ArrayList<String>();
        final String[] split = seperatePrefix(text).split(" ");
        int i;
        for (int beginIndex = i = 1; i < split.length; ++i) {
            final String arg = split[i];
            if (arg != null) {
                args.add(arg);
            }
        }
        return args;
    }
    
    public static Command findCommand(final String text) {
        final String[] split = seperatePrefix(text).split(" ");
        if (split.length <= 0) {
            return null;
        }
        return CommandManager.commands.stream().filter(cmd -> cmd.getName().equalsIgnoreCase(split[0])).findFirst().orElse(null);
    }
    
    public static String seperatePrefix(final String text) {
        if (!text.startsWith(".")) {
            return "." + text;
        }
        return text.substring(1);
    }
    
    public static boolean isCommand(final String text) {
        return findCommand(text) != null;
    }
    
    public static List<Command> getCommands() {
        return CommandManager.commands;
    }
}
