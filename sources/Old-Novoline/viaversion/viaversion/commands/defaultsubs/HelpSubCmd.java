package viaversion.viaversion.commands.defaultsubs;

import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.command.ViaCommandSender;
import viaversion.viaversion.api.command.ViaSubCommand;

public class HelpSubCmd extends ViaSubCommand {
    @Override
    public String name() {
        return "help";
    }

    @Override
    public String description() {
        return "You are looking at it right now!";
    }

    @Override
    public boolean execute(ViaCommandSender sender, String[] args) {
        Via.getManager().getCommandHandler().showHelp(sender);
        return true;
    }
}
