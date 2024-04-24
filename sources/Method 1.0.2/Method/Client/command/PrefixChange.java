package Method.Client.command;

import Method.Client.managers.CommandManager;
import Method.Client.utils.visual.ChatUtils;

@Command.CommandFeatures(description = "Prefix Changer", Syntax = "PrefixChange <Name>")

public class PrefixChange extends Command {

    @Override
    public void runCommand(String s, String[] args) {

        if (args[0].length() < 2) {
            CommandManager.cmdPrefix = args[0].charAt(0);
            ChatUtils.message("Prefix Changed");
        } else {
            ChatUtils.error("Prefix must be 1 length");
        }

    }

}