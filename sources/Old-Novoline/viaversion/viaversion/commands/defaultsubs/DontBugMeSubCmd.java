package viaversion.viaversion.commands.defaultsubs;

import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.command.ViaCommandSender;
import viaversion.viaversion.api.command.ViaSubCommand;
import viaversion.viaversion.api.configuration.ConfigurationProvider;

public class DontBugMeSubCmd extends ViaSubCommand {

    @Override
    public String name() {
        return "dontbugme";
    }

    @Override
    public String description() {
        return "Toggle checking for updates";
    }

    @Override
    public boolean execute(ViaCommandSender sender, String[] args) {
        ConfigurationProvider provider = Via.getPlatform().getConfigurationProvider();
        boolean newValue = !Via.getConfig().isCheckForUpdates();

        Via.getConfig().setCheckForUpdates(newValue);
        provider.saveConfig();
        sendMessage(sender, "&6We will %snotify you about updates.", (newValue ? "&a" : "&cnot "));

        return true;
    }
}
