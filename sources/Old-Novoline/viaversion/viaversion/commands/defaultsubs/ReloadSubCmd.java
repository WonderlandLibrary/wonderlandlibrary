package viaversion.viaversion.commands.defaultsubs;

import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.command.ViaCommandSender;
import viaversion.viaversion.api.command.ViaSubCommand;

public class ReloadSubCmd extends ViaSubCommand {
    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String description() {
        return "Reload the config from the disk";
    }

    @Override
    public boolean execute(ViaCommandSender sender, String[] args) {
        Via.getPlatform().getConfigurationProvider().reloadConfig();
        sendMessage(sender, "&6Configuration successfully reloaded! Some features may need a restart.");
        return true;
    }
}
