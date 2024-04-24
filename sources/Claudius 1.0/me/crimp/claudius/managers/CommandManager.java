package me.crimp.claudius.managers;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.crimp.claudius.mod.Feature;
import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.command.commands.*;
import me.crimp.claudius.mod.modules.client.Prefix;

import java.util.ArrayList;
import java.util.LinkedList;

public class CommandManager
        extends Feature {
    private final ArrayList<Command> commands = new ArrayList<>();
    private String clientMessage = "[claudius]";
    private String prefix = String.valueOf(Prefix.INSTANCE.prefix);

    public CommandManager() {
        super("Command");
        commands.add(new YesCommand());
        commands.add(new BindCommand());
        commands.add(new PrefixCommand());
        commands.add(new ConfigCommand());
        commands.add(new HelpCommand());
        commands.add(new ReloadCommand());
        commands.add(new UnloadCommand());
        commands.add(new Svae());
        commands.add(new ThirtyTwoKay());
        commands.add(new NameMC());
        commands.add(new TestCommand());
        commands.add(new DiscCommand());
        commands.add(new ChinaCommand());
        commands.add(new TokenCommand());
        commands.add(new FriendCommand());
        commands.add(new addsyncCommand());
        commands.add(new delsyncCommand());
        commands.add(new EnemyCommand());
    }

    public static String[] removeElement(String[] input, int indexToDelete) {
        LinkedList<String> result = new LinkedList<>();
        for (int i = 0; i < input.length; ++i) {
            if (i == indexToDelete) continue;
            result.add(input[i]);
        }
        return result.toArray(input);
    }

    private static String strip(String str, String key) {
        if (str.startsWith(key) && str.endsWith(key)) {
            return str.substring(key.length(), str.length() - key.length());
        }
        return str;
    }

    public void executeCommand(String command) {
        String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String name = parts[0].substring(1);
        String[] args = CommandManager.removeElement(parts, 0);
        for (int i = 0; i < args.length; ++i) {
            if (args[i] == null) continue;
            args[i] = CommandManager.strip(args[i], "\"");
        }
        for (Command c : commands) {
            if (!c.getName().equalsIgnoreCase(name)) continue;
            c.execute(parts);
            return;
        }
        Command.sendMessage(ChatFormatting.GRAY + "Command not found, type 'help' for the commands list.");
    }

    public Command getCommandByName(String name) {
        for (Command command : commands) {
            if (!command.getName().equals(name)) continue;
            return command;
        }
        return null;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public String getClientMessage() {
        return this.clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

