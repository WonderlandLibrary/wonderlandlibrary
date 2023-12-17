// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandSetSpawnpoint extends CommandBase
{
    @Override
    public String getName() {
        return "spawnpoint";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.spawnpoint.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length > 1 && args.length < 4) {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        }
        final EntityPlayerMP entityplayermp = (args.length > 0) ? CommandBase.getPlayer(server, sender, args[0]) : CommandBase.getCommandSenderAsPlayer(sender);
        final BlockPos blockpos = (args.length > 3) ? CommandBase.parseBlockPos(sender, args, 1, true) : entityplayermp.getPosition();
        if (entityplayermp.world != null) {
            entityplayermp.setSpawnPoint(blockpos, true);
            CommandBase.notifyCommandListener(sender, this, "commands.spawnpoint.success", entityplayermp.getName(), blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return (args.length > 1 && args.length <= 4) ? CommandBase.getTabCompletionCoordinate(args, 1, targetPos) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
