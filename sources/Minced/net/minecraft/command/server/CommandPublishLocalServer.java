// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.world.GameType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandPublishLocalServer extends CommandBase
{
    @Override
    public String getName() {
        return "publish";
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.publish.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        final String s = server.shareToLAN(GameType.SURVIVAL, false);
        if (s != null) {
            CommandBase.notifyCommandListener(sender, this, "commands.publish.started", s);
        }
        else {
            CommandBase.notifyCommandListener(sender, this, "commands.publish.failed", new Object[0]);
        }
    }
}
