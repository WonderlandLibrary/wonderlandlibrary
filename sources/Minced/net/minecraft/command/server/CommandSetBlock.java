// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.inventory.IInventory;
import net.minecraft.command.ICommand;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandSetBlock extends CommandBase
{
    @Override
    public String getName() {
        return "setblock";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.setblock.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 4) {
            throw new WrongUsageException("commands.setblock.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final Block block = CommandBase.getBlockByText(sender, args[3]);
        IBlockState iblockstate;
        if (args.length >= 5) {
            iblockstate = CommandBase.convertArgToBlockState(block, args[4]);
        }
        else {
            iblockstate = block.getDefaultState();
        }
        final World world = sender.getEntityWorld();
        if (!world.isBlockLoaded(blockpos)) {
            throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;
        if (args.length >= 7 && block.hasTileEntity()) {
            final String s = CommandBase.buildString(args, 6);
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(s);
                flag = true;
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.setblock.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        if (args.length >= 6) {
            if ("destroy".equals(args[5])) {
                world.destroyBlock(blockpos, true);
                if (block == Blocks.AIR) {
                    CommandBase.notifyCommandListener(sender, this, "commands.setblock.success", new Object[0]);
                    return;
                }
            }
            else if ("keep".equals(args[5]) && !world.isAirBlock(blockpos)) {
                throw new CommandException("commands.setblock.noChange", new Object[0]);
            }
        }
        final TileEntity tileentity1 = world.getTileEntity(blockpos);
        if (tileentity1 != null && tileentity1 instanceof IInventory) {
            ((IInventory)tileentity1).clear();
        }
        if (!world.setBlockState(blockpos, iblockstate, 2)) {
            throw new CommandException("commands.setblock.noChange", new Object[0]);
        }
        if (flag) {
            final TileEntity tileentity2 = world.getTileEntity(blockpos);
            if (tileentity2 != null) {
                nbttagcompound.setInteger("x", blockpos.getX());
                nbttagcompound.setInteger("y", blockpos.getY());
                nbttagcompound.setInteger("z", blockpos.getZ());
                tileentity2.readFromNBT(nbttagcompound);
            }
        }
        world.notifyNeighborsRespectDebug(blockpos, iblockstate.getBlock(), false);
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandBase.notifyCommandListener(sender, this, "commands.setblock.success", new Object[0]);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length > 0 && args.length <= 3) {
            return CommandBase.getTabCompletionCoordinate(args, 0, targetPos);
        }
        if (args.length == 4) {
            return CommandBase.getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
        }
        return (args.length == 6) ? CommandBase.getListOfStringsMatchingLastWord(args, "replace", "destroy", "keep") : Collections.emptyList();
    }
}
