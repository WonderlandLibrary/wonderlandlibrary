package io.github.raze.commands.collection;

import io.github.raze.Raze;
import io.github.raze.commands.system.Command;
import io.github.raze.utilities.collection.visual.ChatUtil;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("Help", "View the client's information", "help", "h");
    }

    public String onCommand(String[] arguments, String command) {
        ChatUtil.addChatMessage("", false);
        ChatUtil.addChatMessage("Version " + Raze.INSTANCE.getVersion());
        ChatUtil.addChatMessage("", false);

        for(Command command1 : Raze.INSTANCE.managerRegistry.commandRegistry.getList()) {
            ChatUtil.addChatMessage(Raze.INSTANCE.getPrefix() + command1.syntax + " - " + command1.description);
        }

        return "";
    }

}
