package club.async.command.impl;

import club.async.Async;
import club.async.command.Command;
import club.async.util.ChatUtil;

import java.util.Arrays;

@Command.Info(name = "Help", description = "Gives you information about commands", usage = ".help [command]", aliases = {"help", "h"})
public class HelpCommand extends Command {

    @Override
    public void execute(String[] args) {
        Command command = Async.INSTANCE.getCommandRegistry().commandBy(args[0]);
        if(command == null) {
            ChatUtil.addChatMessage("--------- Help ---------");
            Async.INSTANCE.getCommandRegistry().getCommands().forEach(commande -> {
                ChatUtil.addChatMessage(commande.name() + ": " + commande.getDescription());
            });
            ChatUtil.addChatMessage("-------------------------");
        } else {
            ChatUtil.addChatMessage("--------- " + command.name() + " ---------");
            ChatUtil.addChatMessage("usage: " + command.getUsage());
            ChatUtil.addChatMessage("description: " + command.getDescription());
            ChatUtil.addChatMessage("aliases: " + Arrays.toString(command.getAliases()));
            ChatUtil.addChatMessage("------------------------------");
        }

    }

}
