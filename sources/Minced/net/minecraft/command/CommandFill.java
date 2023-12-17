// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.Collection;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.init.Blocks;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;

public class CommandFill extends CommandBase
{
    @Override
    public String getName() {
        return "fill";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.fill.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 7) {
            throw new WrongUsageException("commands.fill.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final BlockPos blockpos2 = CommandBase.parseBlockPos(sender, args, 3, false);
        final Block block = CommandBase.getBlockByText(sender, args[6]);
        IBlockState iblockstate;
        if (args.length >= 8) {
            iblockstate = CommandBase.convertArgToBlockState(block, args[7]);
        }
        else {
            iblockstate = block.getDefaultState();
        }
        final BlockPos blockpos3 = new BlockPos(Math.min(blockpos.getX(), blockpos2.getX()), Math.min(blockpos.getY(), blockpos2.getY()), Math.min(blockpos.getZ(), blockpos2.getZ()));
        final BlockPos blockpos4 = new BlockPos(Math.max(blockpos.getX(), blockpos2.getX()), Math.max(blockpos.getY(), blockpos2.getY()), Math.max(blockpos.getZ(), blockpos2.getZ()));
        int i = (blockpos4.getX() - blockpos3.getX() + 1) * (blockpos4.getY() - blockpos3.getY() + 1) * (blockpos4.getZ() - blockpos3.getZ() + 1);
        if (i > 32768) {
            throw new CommandException("commands.fill.tooManyBlocks", new Object[] { i, 32768 });
        }
        if (blockpos3.getY() < 0 || blockpos4.getY() >= 256) {
            throw new CommandException("commands.fill.outOfWorld", new Object[0]);
        }
        final World world = sender.getEntityWorld();
        for (int j = blockpos3.getZ(); j <= blockpos4.getZ(); j += 16) {
            for (int k = blockpos3.getX(); k <= blockpos4.getX(); k += 16) {
                if (!world.isBlockLoaded(new BlockPos(k, blockpos4.getY() - blockpos3.getY(), j))) {
                    throw new CommandException("commands.fill.outOfWorld", new Object[0]);
                }
            }
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;
        if (args.length >= 10 && block.hasTileEntity()) {
            final String s = CommandBase.buildString(args, 9);
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(s);
                flag = true;
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.fill.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        final List<BlockPos> list = (List<BlockPos>)Lists.newArrayList();
        i = 0;
        for (int l = blockpos3.getZ(); l <= blockpos4.getZ(); ++l) {
            for (int i2 = blockpos3.getY(); i2 <= blockpos4.getY(); ++i2) {
                for (int j2 = blockpos3.getX(); j2 <= blockpos4.getX(); ++j2) {
                    final BlockPos blockpos5 = new BlockPos(j2, i2, l);
                    if (args.length >= 9) {
                        if (!"outline".equals(args[8]) && !"hollow".equals(args[8])) {
                            if ("destroy".equals(args[8])) {
                                world.destroyBlock(blockpos5, true);
                            }
                            else if ("keep".equals(args[8])) {
                                if (!world.isAirBlock(blockpos5)) {
                                    continue;
                                }
                            }
                            else if ("replace".equals(args[8]) && !block.hasTileEntity() && args.length > 9) {
                                final Block block2 = CommandBase.getBlockByText(sender, args[9]);
                                if (world.getBlockState(blockpos5).getBlock() != block2) {
                                    continue;
                                }
                                if (args.length > 10 && !"-1".equals(args[10]) && !"*".equals(args[10]) && !CommandBase.convertArgToBlockStatePredicate(block2, args[10]).apply((Object)world.getBlockState(blockpos5))) {
                                    continue;
                                }
                            }
                        }
                        else if (j2 != blockpos3.getX() && j2 != blockpos4.getX() && i2 != blockpos3.getY() && i2 != blockpos4.getY() && l != blockpos3.getZ() && l != blockpos4.getZ()) {
                            if ("hollow".equals(args[8])) {
                                world.setBlockState(blockpos5, Blocks.AIR.getDefaultState(), 2);
                                list.add(blockpos5);
                            }
                            continue;
                        }
                    }
                    final TileEntity tileentity1 = world.getTileEntity(blockpos5);
                    if (tileentity1 != null && tileentity1 instanceof IInventory) {
                        ((IInventory)tileentity1).clear();
                    }
                    if (world.setBlockState(blockpos5, iblockstate, 2)) {
                        list.add(blockpos5);
                        ++i;
                        if (flag) {
                            final TileEntity tileentity2 = world.getTileEntity(blockpos5);
                            if (tileentity2 != null) {
                                nbttagcompound.setInteger("x", blockpos5.getX());
                                nbttagcompound.setInteger("y", blockpos5.getY());
                                nbttagcompound.setInteger("z", blockpos5.getZ());
                                tileentity2.readFromNBT(nbttagcompound);
                            }
                        }
                    }
                }
            }
        }
        for (final BlockPos blockpos6 : list) {
            final Block block3 = world.getBlockState(blockpos6).getBlock();
            world.notifyNeighborsRespectDebug(blockpos6, block3, false);
        }
        if (i <= 0) {
            throw new CommandException("commands.fill.failed", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
        CommandBase.notifyCommandListener(sender, this, "commands.fill.success", i);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length > 0 && args.length <= 3) {
            return CommandBase.getTabCompletionCoordinate(args, 0, targetPos);
        }
        if (args.length > 3 && args.length <= 6) {
            return CommandBase.getTabCompletionCoordinate(args, 3, targetPos);
        }
        if (args.length == 7) {
            return CommandBase.getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
        }
        if (args.length == 9) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "replace", "destroy", "keep", "hollow", "outline");
        }
        return (args.length == 10 && "replace".equals(args[8])) ? CommandBase.getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys()) : Collections.emptyList();
    }
}
