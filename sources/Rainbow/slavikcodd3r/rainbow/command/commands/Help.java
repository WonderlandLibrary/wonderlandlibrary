package slavikcodd3r.rainbow.command.commands;

import java.util.Iterator;

import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.command.CommandManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "help" })
public class Help extends Command
{
    @Override
    public void runCommand(final String[] args) {
        for (final Command command : CommandManager.commandList) {
            if (command instanceof OptionCommand) {
                continue;
            }
            if (command.getHelp() == null) {
                continue;
            }
            ClientUtils.sendMessage(command.getHelp());
        }
        ClientUtils.sendMessage(OptionCommand.getHelpString());
    }
    
    @Override
    public String getHelp() {
        return "help";
    }
}
