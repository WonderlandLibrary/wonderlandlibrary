// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import java.util.Deque;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.world.NextTickListEntry;
import java.util.List;
import java.util.Collection;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import com.google.common.collect.Lists;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.server.MinecraftServer;

public class CommandClone extends CommandBase
{
    @Override
    public String getName() {
        return "clone";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.clone.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 9) {
            throw new WrongUsageException("commands.clone.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 0, false);
        final BlockPos blockpos2 = CommandBase.parseBlockPos(sender, args, 3, false);
        final BlockPos blockpos3 = CommandBase.parseBlockPos(sender, args, 6, false);
        final StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos2);
        final StructureBoundingBox structureboundingbox2 = new StructureBoundingBox(blockpos3, blockpos3.add(structureboundingbox.getLength()));
        int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
        if (i > 32768) {
            throw new CommandException("commands.clone.tooManyBlocks", new Object[] { i, 32768 });
        }
        boolean flag = false;
        Block block = null;
        Predicate<IBlockState> predicate = null;
        if ((args.length < 11 || (!"force".equals(args[10]) && !"move".equals(args[10]))) && structureboundingbox.intersectsWith(structureboundingbox2)) {
            throw new CommandException("commands.clone.noOverlap", new Object[0]);
        }
        if (args.length >= 11 && "move".equals(args[10])) {
            flag = true;
        }
        if (structureboundingbox.minY < 0 || structureboundingbox.maxY >= 256 || structureboundingbox2.minY < 0 || structureboundingbox2.maxY >= 256) {
            throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        }
        final World world = sender.getEntityWorld();
        if (!world.isAreaLoaded(structureboundingbox) || !world.isAreaLoaded(structureboundingbox2)) {
            throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        }
        boolean flag2 = false;
        if (args.length >= 10) {
            if ("masked".equals(args[9])) {
                flag2 = true;
            }
            else if ("filtered".equals(args[9])) {
                if (args.length < 12) {
                    throw new WrongUsageException("commands.clone.usage", new Object[0]);
                }
                block = CommandBase.getBlockByText(sender, args[11]);
                if (args.length >= 13) {
                    predicate = CommandBase.convertArgToBlockStatePredicate(block, args[12]);
                }
            }
        }
        final List<StaticCloneData> list = (List<StaticCloneData>)Lists.newArrayList();
        final List<StaticCloneData> list2 = (List<StaticCloneData>)Lists.newArrayList();
        final List<StaticCloneData> list3 = (List<StaticCloneData>)Lists.newArrayList();
        final Deque<BlockPos> deque = (Deque<BlockPos>)Lists.newLinkedList();
        final BlockPos blockpos4 = new BlockPos(structureboundingbox2.minX - structureboundingbox.minX, structureboundingbox2.minY - structureboundingbox.minY, structureboundingbox2.minZ - structureboundingbox.minZ);
        for (int j = structureboundingbox.minZ; j <= structureboundingbox.maxZ; ++j) {
            for (int k = structureboundingbox.minY; k <= structureboundingbox.maxY; ++k) {
                for (int l = structureboundingbox.minX; l <= structureboundingbox.maxX; ++l) {
                    final BlockPos blockpos5 = new BlockPos(l, k, j);
                    final BlockPos blockpos6 = blockpos5.add(blockpos4);
                    final IBlockState iblockstate = world.getBlockState(blockpos5);
                    if ((!flag2 || iblockstate.getBlock() != Blocks.AIR) && (block == null || (iblockstate.getBlock() == block && (predicate == null || predicate.apply((Object)iblockstate))))) {
                        final TileEntity tileentity = world.getTileEntity(blockpos5);
                        if (tileentity != null) {
                            final NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                            list2.add(new StaticCloneData(blockpos6, iblockstate, nbttagcompound));
                            deque.addLast(blockpos5);
                        }
                        else if (!iblockstate.isFullBlock() && !iblockstate.isFullCube()) {
                            list3.add(new StaticCloneData(blockpos6, iblockstate, null));
                            deque.addFirst(blockpos5);
                        }
                        else {
                            list.add(new StaticCloneData(blockpos6, iblockstate, null));
                            deque.addLast(blockpos5);
                        }
                    }
                }
            }
        }
        if (flag) {
            for (final BlockPos blockpos7 : deque) {
                final TileEntity tileentity2 = world.getTileEntity(blockpos7);
                if (tileentity2 instanceof IInventory) {
                    ((IInventory)tileentity2).clear();
                }
                world.setBlockState(blockpos7, Blocks.BARRIER.getDefaultState(), 2);
            }
            for (final BlockPos blockpos8 : deque) {
                world.setBlockState(blockpos8, Blocks.AIR.getDefaultState(), 3);
            }
        }
        final List<StaticCloneData> list4 = (List<StaticCloneData>)Lists.newArrayList();
        list4.addAll(list);
        list4.addAll(list2);
        list4.addAll(list3);
        final List<StaticCloneData> list5 = (List<StaticCloneData>)Lists.reverse((List)list4);
        for (final StaticCloneData commandclone$staticclonedata : list5) {
            final TileEntity tileentity3 = world.getTileEntity(commandclone$staticclonedata.pos);
            if (tileentity3 instanceof IInventory) {
                ((IInventory)tileentity3).clear();
            }
            world.setBlockState(commandclone$staticclonedata.pos, Blocks.BARRIER.getDefaultState(), 2);
        }
        i = 0;
        for (final StaticCloneData commandclone$staticclonedata2 : list4) {
            if (world.setBlockState(commandclone$staticclonedata2.pos, commandclone$staticclonedata2.blockState, 2)) {
                ++i;
            }
        }
        for (final StaticCloneData commandclone$staticclonedata3 : list2) {
            final TileEntity tileentity4 = world.getTileEntity(commandclone$staticclonedata3.pos);
            if (commandclone$staticclonedata3.nbt != null && tileentity4 != null) {
                commandclone$staticclonedata3.nbt.setInteger("x", commandclone$staticclonedata3.pos.getX());
                commandclone$staticclonedata3.nbt.setInteger("y", commandclone$staticclonedata3.pos.getY());
                commandclone$staticclonedata3.nbt.setInteger("z", commandclone$staticclonedata3.pos.getZ());
                tileentity4.readFromNBT(commandclone$staticclonedata3.nbt);
                tileentity4.markDirty();
            }
            world.setBlockState(commandclone$staticclonedata3.pos, commandclone$staticclonedata3.blockState, 2);
        }
        for (final StaticCloneData commandclone$staticclonedata4 : list5) {
            world.notifyNeighborsRespectDebug(commandclone$staticclonedata4.pos, commandclone$staticclonedata4.blockState.getBlock(), false);
        }
        final List<NextTickListEntry> list6 = world.getPendingBlockUpdates(structureboundingbox, false);
        if (list6 != null) {
            for (final NextTickListEntry nextticklistentry : list6) {
                if (structureboundingbox.isVecInside(nextticklistentry.position)) {
                    final BlockPos blockpos9 = nextticklistentry.position.add(blockpos4);
                    world.scheduleBlockUpdate(blockpos9, nextticklistentry.getBlock(), (int)(nextticklistentry.scheduledTime - world.getWorldInfo().getWorldTotalTime()), nextticklistentry.priority);
                }
            }
        }
        if (i <= 0) {
            throw new CommandException("commands.clone.failed", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
        CommandBase.notifyCommandListener(sender, this, "commands.clone.success", i);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length > 0 && args.length <= 3) {
            return CommandBase.getTabCompletionCoordinate(args, 0, targetPos);
        }
        if (args.length > 3 && args.length <= 6) {
            return CommandBase.getTabCompletionCoordinate(args, 3, targetPos);
        }
        if (args.length > 6 && args.length <= 9) {
            return CommandBase.getTabCompletionCoordinate(args, 6, targetPos);
        }
        if (args.length == 10) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "replace", "masked", "filtered");
        }
        if (args.length == 11) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "normal", "force", "move");
        }
        return (args.length == 12 && "filtered".equals(args[9])) ? CommandBase.getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys()) : Collections.emptyList();
    }
    
    static class StaticCloneData
    {
        public final BlockPos pos;
        public final IBlockState blockState;
        public final NBTTagCompound nbt;
        
        public StaticCloneData(final BlockPos posIn, final IBlockState stateIn, final NBTTagCompound compoundIn) {
            this.pos = posIn;
            this.blockState = stateIn;
            this.nbt = compoundIn;
        }
    }
}
