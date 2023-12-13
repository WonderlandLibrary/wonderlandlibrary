// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandSetSpawnpoint extends CommandBase
{
    private static final String __OBFID = "CL_00001026";
    
    @Override
    public String getCommandName() {
        return "spawnpoint";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.spawnpoint.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length > 0 && args.length < 4) {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        }
        final EntityPlayerMP var3 = (args.length > 0) ? CommandBase.getPlayer(sender, args[0]) : CommandBase.getCommandSenderAsPlayer(sender);
        final BlockPos var4 = (args.length > 3) ? CommandBase.func_175757_a(sender, args, 1, true) : var3.getPosition();
        if (var3.worldObj != null) {
            var3.func_180473_a(var4, true);
            CommandBase.notifyOperators(sender, this, "commands.spawnpoint.success", var3.getName(), var4.getX(), var4.getY(), var4.getZ());
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : ((args.length > 1 && args.length <= 4) ? CommandBase.func_175771_a(args, 1, pos) : null);
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
