// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.commands;

import org.bukkit.entity.Player;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import com.viaversion.viaversion.api.command.ViaCommandSender;

public class BukkitCommandSender implements ViaCommandSender
{
    private final CommandSender sender;
    
    public BukkitCommandSender(final CommandSender sender) {
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
        if (this.sender instanceof Player) {
            return ((Player)this.sender).getUniqueId();
        }
        return UUID.fromString(this.getName());
    }
    
    @Override
    public String getName() {
        return this.sender.getName();
    }
}
