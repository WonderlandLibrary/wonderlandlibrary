// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.ICommand;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.command.CommandException;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandTestForBlock extends CommandBase
{
    @Override
    public String getName() {
        return "testforblock";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.testforblock.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 4) {
            throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final Block block = CommandBase.getBlockByText(sender, args[3]);
        if (block == null) {
            throw new NumberInvalidException("commands.setblock.notFound", new Object[] { args[3] });
        }
        final World world = sender.getEntityWorld();
        if (!world.isBlockLoaded(blockpos)) {
            throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;
        if (args.length >= 6 && block.hasTileEntity()) {
            final String s = CommandBase.buildString(args, 5);
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(s);
                flag = true;
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.setblock.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        final IBlockState iblockstate = world.getBlockState(blockpos);
        final Block block2 = iblockstate.getBlock();
        if (block2 != block) {
            throw new CommandException("commands.testforblock.failed.tile", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ(), block2.getLocalizedName(), block.getLocalizedName() });
        }
        if (args.length >= 5 && !CommandBase.convertArgToBlockStatePredicate(block, args[4]).apply((Object)iblockstate)) {
            try {
                final int i = iblockstate.getBlock().getMetaFromState(iblockstate);
                throw new CommandException("commands.testforblock.failed.data", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ(), i, Integer.parseInt(args[4]) });
            }
            catch (NumberFormatException var13) {
                throw new CommandException("commands.testforblock.failed.data", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ(), iblockstate.toString(), args[4] });
            }
        }
        if (flag) {
            final TileEntity tileentity = world.getTileEntity(blockpos);
            if (tileentity == null) {
                throw new CommandException("commands.testforblock.failed.tileEntity", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ() });
            }
            final NBTTagCompound nbttagcompound2 = tileentity.writeToNBT(new NBTTagCompound());
            if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound2, true)) {
                throw new CommandException("commands.testforblock.failed.nbt", new Object[] { blockpos.getX(), blockpos.getY(), blockpos.getZ() });
            }
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandBase.notifyCommandListener(sender, this, "commands.testforblock.success", blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length > 0 && args.length <= 3) {
            return CommandBase.getTabCompletionCoordinate(args, 0, targetPos);
        }
        return (args.length == 4) ? CommandBase.getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys()) : Collections.emptyList();
    }
}
