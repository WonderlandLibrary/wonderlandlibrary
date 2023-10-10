// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.commands;

import java.util.List;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.command.CommandExecutor;
import com.viaversion.viaversion.commands.ViaCommandHandler;

public class BukkitCommandHandler extends ViaCommandHandler implements CommandExecutor, TabExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        return this.onCommand(new BukkitCommandSender(sender), args);
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return this.onTabComplete(new BukkitCommandSender(sender), args);
    }
}
