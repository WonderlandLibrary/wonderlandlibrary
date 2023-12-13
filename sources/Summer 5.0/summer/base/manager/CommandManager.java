package summer.base.manager;

import java.util.HashMap;

import summer.base.commands.*;
import summer.base.utilities.ChatUtils;

public class CommandManager {

    /**
     * The HashMap that holds all the commands.
     **/
    private HashMap<String[], Command> commands;

    /**
     * The prefix that holds the prefix required for the commands.
     **/
    private String prefix;

    public CommandManager() {
        commands = new HashMap<String[], Command>();
        prefix = ".";
        registerCommands();
    }

    /**
     * Loads all the commands. These commands are stored in a hashmap.
     **/
    private void registerCommands() {
        commands.put(new String[]{"help", "h"}, new HelpCommand());
        commands.put(new String[]{"bind", "b"}, new BindCommand());
        commands.put(new String[]{"toggle", "t"}, new ToggleCommand());
        commands.put(new String[]{"config", "c"}, new ConfigCommand());
        commands.put(new String[]{"friend", "f"}, new FriendCommand());
        commands.put(new String[]{"hclip", "hc"}, new HClipCommand());
        commands.put(new String[]{"vclip", "vc"}, new VClipCommand());
        //TODO when im not lazy :(
        //commands.put(new String[]{"setting", "s"}, new SettingsCommand());
    }

    public boolean processCommand(String rawMessage) {
        /**
         * Checks if the rawMessage starts if the prefix for the commands. If it does
         * not start with the prefix it does not process the command.
         **/
        if (!rawMessage.startsWith(prefix))
            return false;

        /** Gets rid of the prefix from the rawMessage. **/
        String beheaded = rawMessage.substring(1);

        String[] args = beheaded.split(" ");

        /** Gets the command using the start of the array. **/
        Command command = getCommand(args[0]);

        /** If a command was found it runs the command. **/
        if (command != null) {
            /** If the command failed it tell the user how to use that command. **/
            if (!command.run(args)) {
                ChatUtils.sendMessage(command.usage());
            }
        }
        /** If no command was found it tell the user to do the help command. **/
        else {
            ChatUtils.sendMessage("Try " + prefix + "help.");
        }

        return true;
    }

    /**
     * Goes through all the entries in the HashMap and checks if the name provided
     * is a valid command name.
     **/
    private Command getCommand(String name) {
        for (String[] keys : commands.keySet()) {
            for (String key : keys) {
                if (key.equalsIgnoreCase(name))
                    return commands.get(keys);
            }
        }

        return null;
    }

    /**
     * Returns the HashMap that contains all the commands.
     **/
    public HashMap<String[], Command> getCommands() {
        return commands;
    }

}