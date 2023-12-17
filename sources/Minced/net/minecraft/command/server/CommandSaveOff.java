// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.world.WorldServer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSaveOff extends CommandBase
{
    @Override
    public String getName() {
        return "save-off";
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.save-off.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        boolean flag = false;
        for (int i = 0; i < server.worlds.length; ++i) {
            if (server.worlds[i] != null) {
                final WorldServer worldserver = server.worlds[i];
                if (!worldserver.disableLevelSaving) {
                    worldserver.disableLevelSaving = true;
                    flag = true;
                }
            }
        }
        if (flag) {
            CommandBase.notifyCommandListener(sender, this, "commands.save.disabled", new Object[0]);
            return;
        }
        throw new CommandException("commands.save-off.alreadyOff", new Object[0]);
    }
}
