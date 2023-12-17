// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandKill extends CommandBase
{
    @Override
    public String getName() {
        return "kill";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.kill.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length == 0) {
            final EntityPlayer entityplayer = CommandBase.getCommandSenderAsPlayer(sender);
            entityplayer.onKillCommand();
            CommandBase.notifyCommandListener(sender, this, "commands.kill.successful", entityplayer.getDisplayName());
        }
        else {
            final Entity entity = CommandBase.getEntity(server, sender, args[0]);
            entity.onKillCommand();
            CommandBase.notifyCommandListener(sender, this, "commands.kill.successful", entity.getDisplayName());
        }
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
