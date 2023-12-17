// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

public class CommandExecuteAt extends CommandBase
{
    @Override
    public String getName() {
        return "execute";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.execute.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 5) {
            throw new WrongUsageException("commands.execute.usage", new Object[0]);
        }
        final Entity entity = CommandBase.getEntity(server, sender, args[0], (Class<? extends Entity>)Entity.class);
        final double d0 = CommandBase.parseDouble(entity.posX, args[1], false);
        final double d2 = CommandBase.parseDouble(entity.posY, args[2], false);
        final double d3 = CommandBase.parseDouble(entity.posZ, args[3], false);
        new BlockPos(d0, d2, d3);
        int i = 4;
        if ("detect".equals(args[4]) && args.length > 10) {
            final World world = entity.getEntityWorld();
            final double d4 = CommandBase.parseDouble(d0, args[5], false);
            final double d5 = CommandBase.parseDouble(d2, args[6], false);
            final double d6 = CommandBase.parseDouble(d3, args[7], false);
            final Block block = CommandBase.getBlockByText(sender, args[8]);
            final BlockPos blockpos = new BlockPos(d4, d5, d6);
            if (!world.isBlockLoaded(blockpos)) {
                throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.getName() });
            }
            final IBlockState iblockstate = world.getBlockState(blockpos);
            if (iblockstate.getBlock() != block) {
                throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.getName() });
            }
            if (!CommandBase.convertArgToBlockStatePredicate(block, args[9]).apply((Object)iblockstate)) {
                throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.getName() });
            }
            i = 10;
        }
        final String s = CommandBase.buildString(args, i);
        final ICommandSender icommandsender = CommandSenderWrapper.create(sender).withEntity(entity, new Vec3d(d0, d2, d3)).withSendCommandFeedback(server.worlds[0].getGameRules().getBoolean("commandBlockOutput"));
        final ICommandManager icommandmanager = server.getCommandManager();
        try {
            final int j = icommandmanager.executeCommand(icommandsender, s);
            if (j < 1) {
                throw new CommandException("commands.execute.allInvocationsFailed", new Object[] { s });
            }
        }
        catch (Throwable var23) {
            throw new CommandException("commands.execute.failed", new Object[] { s, entity.getName() });
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        if (args.length > 1 && args.length <= 4) {
            return CommandBase.getTabCompletionCoordinate(args, 1, targetPos);
        }
        if (args.length > 5 && args.length <= 8 && "detect".equals(args[4])) {
            return CommandBase.getTabCompletionCoordinate(args, 5, targetPos);
        }
        return (args.length == 9 && "detect".equals(args[4])) ? CommandBase.getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys()) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
