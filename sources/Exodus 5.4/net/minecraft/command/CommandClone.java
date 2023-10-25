/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandClone
extends CommandBase {
    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length > 0 && stringArray.length <= 3 ? CommandClone.func_175771_a(stringArray, 0, blockPos) : (stringArray.length > 3 && stringArray.length <= 6 ? CommandClone.func_175771_a(stringArray, 3, blockPos) : (stringArray.length > 6 && stringArray.length <= 9 ? CommandClone.func_175771_a(stringArray, 6, blockPos) : (stringArray.length == 10 ? CommandClone.getListOfStringsMatchingLastWord(stringArray, "replace", "masked", "filtered") : (stringArray.length == 11 ? CommandClone.getListOfStringsMatchingLastWord(stringArray, "normal", "force", "move") : (stringArray.length == 12 && "filtered".equals(stringArray[9]) ? CommandClone.getListOfStringsMatchingLastWord(stringArray, Block.blockRegistry.getKeys()) : null)))));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        Object object;
        Object object2;
        if (stringArray.length < 9) {
            throw new WrongUsageException("commands.clone.usage", new Object[0]);
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockPos = CommandClone.parseBlockPos(iCommandSender, stringArray, 0, false);
        BlockPos blockPos2 = CommandClone.parseBlockPos(iCommandSender, stringArray, 3, false);
        BlockPos blockPos3 = CommandClone.parseBlockPos(iCommandSender, stringArray, 6, false);
        StructureBoundingBox structureBoundingBox = new StructureBoundingBox(blockPos, blockPos2);
        StructureBoundingBox structureBoundingBox2 = new StructureBoundingBox(blockPos3, blockPos3.add(structureBoundingBox.func_175896_b()));
        int n = structureBoundingBox.getXSize() * structureBoundingBox.getYSize() * structureBoundingBox.getZSize();
        if (n > 32768) {
            throw new CommandException("commands.clone.tooManyBlocks", n, 32768);
        }
        boolean bl = false;
        Block block = null;
        int n2 = -1;
        if ((stringArray.length < 11 || !stringArray[10].equals("force") && !stringArray[10].equals("move")) && structureBoundingBox.intersectsWith(structureBoundingBox2)) {
            throw new CommandException("commands.clone.noOverlap", new Object[0]);
        }
        if (stringArray.length >= 11 && stringArray[10].equals("move")) {
            bl = true;
        }
        if (structureBoundingBox.minY < 0 || structureBoundingBox.maxY >= 256 || structureBoundingBox2.minY < 0 || structureBoundingBox2.maxY >= 256) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        World world = iCommandSender.getEntityWorld();
        if (!world.isAreaLoaded(structureBoundingBox) || !world.isAreaLoaded(structureBoundingBox2)) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        boolean bl2 = false;
        if (stringArray.length >= 10) {
            if (stringArray[9].equals("masked")) {
                bl2 = true;
            } else if (stringArray[9].equals("filtered")) {
                if (stringArray.length < 12) {
                    throw new WrongUsageException("commands.clone.usage", new Object[0]);
                }
                block = CommandClone.getBlockByText(iCommandSender, stringArray[11]);
                if (stringArray.length >= 13) {
                    n2 = CommandClone.parseInt(stringArray[12], 0, 15);
                }
            }
        }
        ArrayList arrayList = Lists.newArrayList();
        ArrayList arrayList2 = Lists.newArrayList();
        ArrayList arrayList3 = Lists.newArrayList();
        LinkedList linkedList = Lists.newLinkedList();
        BlockPos blockPos4 = new BlockPos(structureBoundingBox2.minX - structureBoundingBox.minX, structureBoundingBox2.minY - structureBoundingBox.minY, structureBoundingBox2.minZ - structureBoundingBox.minZ);
        int n3 = structureBoundingBox.minZ;
        while (n3 <= structureBoundingBox.maxZ) {
            int n4 = structureBoundingBox.minY;
            while (n4 <= structureBoundingBox.maxY) {
                int n5 = structureBoundingBox.minX;
                while (n5 <= structureBoundingBox.maxX) {
                    BlockPos blockPos5 = new BlockPos(n5, n4, n3);
                    object2 = blockPos5.add(blockPos4);
                    object = world.getBlockState(blockPos5);
                    if (!(bl2 && object.getBlock() == Blocks.air || block != null && (object.getBlock() != block || n2 >= 0 && object.getBlock().getMetaFromState((IBlockState)object) != n2))) {
                        TileEntity tileEntity = world.getTileEntity(blockPos5);
                        if (tileEntity != null) {
                            NBTTagCompound nBTTagCompound = new NBTTagCompound();
                            tileEntity.writeToNBT(nBTTagCompound);
                            arrayList2.add(new StaticCloneData((BlockPos)object2, (IBlockState)object, nBTTagCompound));
                            linkedList.addLast(blockPos5);
                        } else if (!object.getBlock().isFullBlock() && !object.getBlock().isFullCube()) {
                            arrayList3.add(new StaticCloneData((BlockPos)object2, (IBlockState)object, null));
                            linkedList.addFirst(blockPos5);
                        } else {
                            arrayList.add(new StaticCloneData((BlockPos)object2, (IBlockState)object, null));
                            linkedList.addLast(blockPos5);
                        }
                    }
                    ++n5;
                }
                ++n4;
            }
            ++n3;
        }
        if (bl) {
            for (BlockPos blockPos6 : linkedList) {
                TileEntity tileEntity = world.getTileEntity(blockPos6);
                if (tileEntity instanceof IInventory) {
                    ((IInventory)((Object)tileEntity)).clear();
                }
                world.setBlockState(blockPos6, Blocks.barrier.getDefaultState(), 2);
            }
            for (BlockPos blockPos7 : linkedList) {
                world.setBlockState(blockPos7, Blocks.air.getDefaultState(), 3);
            }
        }
        ArrayList arrayList4 = Lists.newArrayList();
        arrayList4.addAll(arrayList);
        arrayList4.addAll(arrayList2);
        arrayList4.addAll(arrayList3);
        List list = Lists.reverse((List)arrayList4);
        for (StaticCloneData staticCloneData : list) {
            object2 = world.getTileEntity(staticCloneData.field_179537_a);
            if (object2 instanceof IInventory) {
                ((IInventory)object2).clear();
            }
            world.setBlockState(staticCloneData.field_179537_a, Blocks.barrier.getDefaultState(), 2);
        }
        n = 0;
        for (StaticCloneData staticCloneData : arrayList4) {
            if (!world.setBlockState(staticCloneData.field_179537_a, staticCloneData.blockState, 2)) continue;
            ++n;
        }
        for (StaticCloneData staticCloneData : arrayList2) {
            object2 = world.getTileEntity(staticCloneData.field_179537_a);
            if (staticCloneData.field_179536_c != null && object2 != null) {
                staticCloneData.field_179536_c.setInteger("x", staticCloneData.field_179537_a.getX());
                staticCloneData.field_179536_c.setInteger("y", staticCloneData.field_179537_a.getY());
                staticCloneData.field_179536_c.setInteger("z", staticCloneData.field_179537_a.getZ());
                ((TileEntity)object2).readFromNBT(staticCloneData.field_179536_c);
                ((TileEntity)object2).markDirty();
            }
            world.setBlockState(staticCloneData.field_179537_a, staticCloneData.blockState, 2);
        }
        for (StaticCloneData staticCloneData : list) {
            world.notifyNeighborsRespectDebug(staticCloneData.field_179537_a, staticCloneData.blockState.getBlock());
        }
        List<NextTickListEntry> list2 = world.func_175712_a(structureBoundingBox, false);
        if (list2 != null) {
            for (NextTickListEntry nextTickListEntry : list2) {
                if (!structureBoundingBox.isVecInside(nextTickListEntry.position)) continue;
                object = nextTickListEntry.position.add(blockPos4);
                world.scheduleBlockUpdate((BlockPos)object, nextTickListEntry.getBlock(), (int)(nextTickListEntry.scheduledTime - world.getWorldInfo().getWorldTotalTime()), nextTickListEntry.priority);
            }
        }
        if (n <= 0) {
            throw new CommandException("commands.clone.failed", new Object[0]);
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, n);
        CommandClone.notifyOperators(iCommandSender, (ICommand)this, "commands.clone.success", n);
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.clone.usage";
    }

    @Override
    public String getCommandName() {
        return "clone";
    }

    static class StaticCloneData {
        public final NBTTagCompound field_179536_c;
        public final BlockPos field_179537_a;
        public final IBlockState blockState;

        public StaticCloneData(BlockPos blockPos, IBlockState iBlockState, NBTTagCompound nBTTagCompound) {
            this.field_179537_a = blockPos;
            this.blockState = iBlockState;
            this.field_179536_c = nBTTagCompound;
        }
    }
}

