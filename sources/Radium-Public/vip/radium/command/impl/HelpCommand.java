package vip.radium.command.impl;

import vip.radium.RadiumClient;
import vip.radium.command.Command;
import vip.radium.command.CommandExecutionException;
import vip.radium.utils.Wrapper;

import java.util.Arrays;

public final class HelpCommand implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"help", "h"};
    }

    @Override
    public void execute(String[] arguments) throws CommandExecutionException {
        Wrapper.addChatMessage("Available Commands:");
        for (Command command : RadiumClient.getInstance().getCommandHandler().getElements()) {
            if (RadiumClient.getInstance().getModuleManager().getModules().stream().noneMatch(module ->
                    Arrays.stream(command.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(module.getLabel())))) {
                Wrapper.addChatMessage(Arrays.toString(command.getAliases()) + ": " + command.getUsage());
            }
        }
    }

    @Override
    public String getUsage() {
        return "help/h";
    }
}
