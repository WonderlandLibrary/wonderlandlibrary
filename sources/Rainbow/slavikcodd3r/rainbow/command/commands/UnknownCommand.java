package slavikcodd3r.rainbow.command.commands;

import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "" })
public class UnknownCommand extends Command
{
    @Override
    public void runCommand(final String[] args) {
        ClientUtils.sendMessage("Unknown command");
    }
}
