// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.commands.defaultsubs;

import com.viaversion.viaversion.api.configuration.ConfigurationProvider;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.command.ViaSubCommand;

public class DontBugMeSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "dontbugme";
    }
    
    @Override
    public String description() {
        return "Toggle checking for updates";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        final ConfigurationProvider provider = Via.getPlatform().getConfigurationProvider();
        final boolean newValue = !Via.getConfig().isCheckForUpdates();
        Via.getConfig().setCheckForUpdates(newValue);
        provider.saveConfig();
        ViaSubCommand.sendMessage(sender, "&6We will %snotify you about updates.", newValue ? "&a" : "&cnot ");
        return true;
    }
}
