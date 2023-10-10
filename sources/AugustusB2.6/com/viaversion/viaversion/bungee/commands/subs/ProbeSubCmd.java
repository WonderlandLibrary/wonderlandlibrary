// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.commands.subs;

import com.viaversion.viaversion.bungee.service.ProtocolDetectorService;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.bungee.platform.BungeeViaConfig;
import com.viaversion.viaversion.api.command.ViaSubCommand;

public class ProbeSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "probe";
    }
    
    @Override
    public String description() {
        return "Forces ViaVersion to scan server protocol versions " + ((((BungeeViaConfig)Via.getConfig()).getBungeePingInterval() == -1) ? "" : "(Also happens at an interval)");
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        ProtocolDetectorService.getInstance().run();
        ViaSubCommand.sendMessage(sender, "&6Started searching for protocol versions", new Object[0]);
        return true;
    }
}
