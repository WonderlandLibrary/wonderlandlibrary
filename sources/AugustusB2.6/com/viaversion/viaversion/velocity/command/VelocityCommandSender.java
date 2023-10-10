// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.velocity.command;

import com.velocitypowered.api.proxy.Player;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import com.viaversion.viaversion.VelocityPlugin;
import com.velocitypowered.api.command.CommandSource;
import com.viaversion.viaversion.api.command.ViaCommandSender;

public class VelocityCommandSender implements ViaCommandSender
{
    private final CommandSource source;
    
    public VelocityCommandSender(final CommandSource source) {
        this.source = source;
    }
    
    @Override
    public boolean hasPermission(final String permission) {
        return this.source.hasPermission(permission);
    }
    
    @Override
    public void sendMessage(final String msg) {
        this.source.sendMessage((Component)VelocityPlugin.COMPONENT_SERIALIZER.deserialize(msg));
    }
    
    @Override
    public UUID getUUID() {
        if (this.source instanceof Player) {
            return ((Player)this.source).getUniqueId();
        }
        return UUID.fromString(this.getName());
    }
    
    @Override
    public String getName() {
        if (this.source instanceof Player) {
            return ((Player)this.source).getUsername();
        }
        return "?";
    }
}
