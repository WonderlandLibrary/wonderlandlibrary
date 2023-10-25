/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandClone
extends CommandBase {
    @Override
    public String getCommandName() {
        return "clone";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.clone.usage";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 9) {
            throw new WrongUsageException("commands.clone.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockpos = CommandClone.parseBlockPos(sender, args, 0, false);
        BlockPos blockpos1 = CommandClone.parseBlockPos(sender, args, 3, false);
        BlockPos blockpos2 = CommandClone.parseBlockPos(sender, args, 6, false);
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos1);
        StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockpos2, blockpos2.add(structureboundingbox.getLength()));
        int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
        if (i > 32768) {
            throw new CommandException("commands.clone.tooManyBlocks", i, 32768);
        }
        boolean flag = false;
        Block block = null;
        Predicate<IBlockState> predicate = null;
        if ((args.length < 11 || !"force".equals(args[10]) && !"move".equals(args[10])) && structureboundingbox.intersectsWith(structureboundingbox1)) {
            throw new CommandException("commands.clone.noOverlap", new Object[0]);
        }
        if (args.length >= 11 && "move".equals(args[10])) {
            flag = true;
        }
        if (structureboundingbox.minY < 0 || structureboundingbox.maxY >= 256 || structureboundingbox1.minY < 0 || structureboundingbox1.maxY >= 256) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        World world = sender.getEntityWorld();
        if (!world.isAreaLoaded(structureboundingbox) || !world.isAreaLoaded(structureboundingbox1)) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        boolean flag1 = false;
        if (args.length >= 10) {
            if ("masked".equals(args[9])) {
                flag1 = true;
            } else if ("filtered".equals(args[9])) {
                if (args.length < 12) {
                    throw new WrongUsageException("commands.clone.usage", new Object[0]);
                }
                block = CommandClone.getBlockByText(sender, args[11]);
                if (args.length >= 13) {
                    predicate = CommandClone.func_190791_b(block, args[12]);
                }
            }
        }
        ArrayList<StaticCloneData> list = Lists.newArrayList();
        ArrayList<StaticCloneData> list1 = Lists.newArrayList();
        ArrayList<StaticCloneData> list2 = Lists.newArrayList();
        LinkedList<BlockPos> deque = Lists.newLinkedList();
        BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);
        for (int j = structureboundingbox.minZ; j <= structureboundingbox.maxZ; ++j) {
            for (int k = structureboundingbox.minY; k <= structureboundingbox.maxY; ++k) {
                for (int l = structureboundingbox.minX; l <= structureboundingbox.maxX; ++l) {
                    BlockPos blockpos4 = new BlockPos(l, k, j);
                    BlockPos blockpos5 = blockpos4.add(blockpos3);
                    IBlockState iblockstate = world.getBlockState(blockpos4);
                    if (flag1 && iblockstate.getBlock() == Blocks.AIR || block != null && (iblockstate.getBlock() != block || predicate != null && !predicate.apply(iblockstate))) continue;
                    TileEntity tileentity = world.getTileEntity(blockpos4);
                    if (tileentity != null) {
                        NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                        list1.add(new StaticCloneData(blockpos5, iblockstate, nbttagcompound));
                        deque.addLast(blockpos4);
                        continue;
                    }
                    if (!iblockstate.isFullBlock() && !iblockstate.isFullCube()) {
                        list2.add(new StaticCloneData(blockpos5, iblockstate, null));
                        deque.addFirst(blockpos4);
                        continue;
                    }
                    list.add(new StaticCloneData(blockpos5, iblockstate, null));
                    deque.addLast(blockpos4);
                }
            }
        }
        if (flag) {
            for (BlockPos blockpos6 : deque) {
                TileEntity tileentity1 = world.getTileEntity(blockpos6);
                if (tileentity1 instanceof IInventory) {
                    ((IInventory)((Object)tileentity1)).clear();
                }
                world.setBlockState(blockpos6, Blocks.BARRIER.getDefaultState(), 2);
            }
            for (BlockPos blockpos7 : deque) {
                world.setBlockState(blockpos7, Blocks.AIR.getDefaultState(), 3);
            }
        }
        ArrayList<StaticCloneData> list3 = Lists.newArrayList();
        list3.addAll(list);
        list3.addAll(list1);
        list3.addAll(list2);
        List<StaticCloneData> list4 = Lists.reverse(list3);
        for (StaticCloneData commandclone$staticclonedata : list4) {
            TileEntity tileentity2 = world.getTileEntity(commandclone$staticclonedata.pos);
            if (tileentity2 instanceof IInventory) {
                ((IInventory)((Object)tileentity2)).clear();
            }
            world.setBlockState(commandclone$staticclonedata.pos, Blocks.BARRIER.getDefaultState(), 2);
        }
        i = 0;
        for (StaticCloneData commandclone$staticclonedata1 : list3) {
            if (!world.setBlockState(commandclone$staticclonedata1.pos, commandclone$staticclonedata1.blockState, 2)) continue;
            ++i;
        }
        for (StaticCloneData commandclone$staticclonedata2 : list1) {
            TileEntity tileentity3 = world.getTileEntity(commandclone$staticclonedata2.pos);
            if (commandclone$staticclonedata2.nbt != null && tileentity3 != null) {
                commandclone$staticclonedata2.nbt.setInteger("x", commandclone$staticclonedata2.pos.getX());
                commandclone$staticclonedata2.nbt.setInteger("y", commandclone$staticclonedata2.pos.getY());
                commandclone$staticclonedata2.nbt.setInteger("z", commandclone$staticclonedata2.pos.getZ());
                tileentity3.readFromNBT(commandclone$staticclonedata2.nbt);
                tileentity3.markDirty();
            }
            world.setBlockState(commandclone$staticclonedata2.pos, commandclone$staticclonedata2.blockState, 2);
        }
        for (StaticCloneData commandclone$staticclonedata3 : list4) {
            world.notifyNeighborsRespectDebug(commandclone$staticclonedata3.pos, commandclone$staticclonedata3.blockState.getBlock(), false);
        }
        List<NextTickListEntry> list5 = world.getPendingBlockUpdates(structureboundingbox, false);
        if (list5 != null) {
            for (NextTickListEntry nextticklistentry : list5) {
                if (!structureboundingbox.isVecInside(nextticklistentry.position)) continue;
                BlockPos blockpos8 = nextticklistentry.position.add(blockpos3);
                world.scheduleBlockUpdate(blockpos8, nextticklistentry.getBlock(), (int)(nextticklistentry.scheduledTime - world.getWorldInfo().getWorldTotalTime()), nextticklistentry.priority);
            }
        }
        if (i <= 0) {
            throw new CommandException("commands.clone.failed", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
        CommandClone.notifyCommandListener(sender, (ICommand)this, "commands.clone.success", i);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length > 0 && args.length <= 3) {
            return CommandClone.getTabCompletionCoordinate(args, 0, pos);
        }
        if (args.length > 3 && args.length <= 6) {
            return CommandClone.getTabCompletionCoordinate(args, 3, pos);
        }
        if (args.length > 6 && args.length <= 9) {
            return CommandClone.getTabCompletionCoordinate(args, 6, pos);
        }
        if (args.length == 10) {
            return CommandClone.getListOfStringsMatchingLastWord(args, "replace", "masked", "filtered");
        }
        if (args.length == 11) {
            return CommandClone.getListOfStringsMatchingLastWord(args, "normal", "force", "move");
        }
        return args.length == 12 && "filtered".equals(args[9]) ? CommandClone.getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys()) : Collections.emptyList();
    }

    static class StaticCloneData {
        public final BlockPos pos;
        public final IBlockState blockState;
        public final NBTTagCompound nbt;

        public StaticCloneData(BlockPos posIn, IBlockState stateIn, NBTTagCompound compoundIn) {
            this.pos = posIn;
            this.blockState = stateIn;
            this.nbt = compoundIn;
        }
    }
}

