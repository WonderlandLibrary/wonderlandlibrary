// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.commands;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.UUID;
import net.md_5.bungee.api.CommandSender;
import com.viaversion.viaversion.api.command.ViaCommandSender;

public class BungeeCommandSender implements ViaCommandSender
{
    private final CommandSender sender;
    
    public BungeeCommandSender(final CommandSender sender) {
        this.sender = sender;
    }
    
    @Override
    public boolean hasPermission(final String permission) {
        return this.sender.hasPermission(permission);
    }
    
    @Override
    public void sendMessage(final String msg) {
        this.sender.sendMessage(msg);
    }
    
    @Override
    public UUID getUUID() {
        if (this.sender instanceof ProxiedPlayer) {
            return ((ProxiedPlayer)this.sender).getUniqueId();
        }
        return UUID.fromString(this.getName());
    }
    
    @Override
    public String getName() {
        return this.sender.getName();
    }
}
