package viaversion.viaversion.commands.defaultsubs;

import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.command.ViaCommandSender;
import viaversion.viaversion.api.command.ViaSubCommand;

public class DebugSubCmd extends ViaSubCommand {
    @Override
    public String name() {
        return "debug";
    }

    @Override
    public String description() {
        return "Toggle debug mode";
    }

    @Override
    public boolean execute(ViaCommandSender sender, String[] args) {
        Via.getManager().setDebug(!Via.getManager().isDebug());
        sendMessage(sender, "&6Debug mode is now %s", (Via.getManager().isDebug() ? "&aenabled" : "&cdisabled"));
        return true;
    }
}
