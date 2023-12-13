// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.velocity.command.subs;

import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.velocity.platform.VelocityViaConfig;
import com.viaversion.viaversion.api.command.ViaSubCommand;

public class ProbeSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "probe";
    }
    
    @Override
    public String description() {
        return "Forces ViaVersion to scan server protocol versions " + ((((VelocityViaConfig)Via.getConfig()).getVelocityPingInterval() == -1) ? "" : "(Also happens at an interval)");
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        Via.proxyPlatform().protocolDetectorService().probeAllServers();
        ViaSubCommand.sendMessage(sender, "&6Started searching for protocol versions", new Object[0]);
        return true;
    }
}
