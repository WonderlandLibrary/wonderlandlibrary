/*
 * Decompiled with CFR 0.152.
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

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 9) {
            throw new WrongUsageException("commands.clone.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockpos = CommandClone.parseBlockPos(sender, args, 0, false);
        BlockPos blockpos1 = CommandClone.parseBlockPos(sender, args, 3, false);
        BlockPos blockpos2 = CommandClone.parseBlockPos(sender, args, 6, false);
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos1);
        StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockpos2, blockpos2.add(structureboundingbox.func_175896_b()));
        int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
        if (i > 32768) {
            throw new CommandException("commands.clone.tooManyBlocks", i, 32768);
        }
        boolean flag = false;
        Block block = null;
        int j = -1;
        if ((args.length < 11 || !args[10].equals("force") && !args[10].equals("move")) && structureboundingbox.intersectsWith(structureboundingbox1)) {
            throw new CommandException("commands.clone.noOverlap", new Object[0]);
        }
        if (args.length >= 11 && args[10].equals("move")) {
            flag = true;
        }
        if (structureboundingbox.minY < 0) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        if (structureboundingbox.maxY >= 256) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        if (structureboundingbox1.minY < 0) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        if (structureboundingbox1.maxY >= 256) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        World world = sender.getEntityWorld();
        if (!world.isAreaLoaded(structureboundingbox)) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        if (!world.isAreaLoaded(structureboundingbox1)) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        boolean flag1 = false;
        if (args.length >= 10) {
            if (args[9].equals("masked")) {
                flag1 = true;
            } else if (args[9].equals("filtered")) {
                if (args.length < 12) {
                    throw new WrongUsageException("commands.clone.usage", new Object[0]);
                }
                block = CommandClone.getBlockByText(sender, args[11]);
                if (args.length >= 13) {
                    j = CommandClone.parseInt(args[12], 0, 15);
                }
            }
        }
        ArrayList<StaticCloneData> list = Lists.newArrayList();
        ArrayList<StaticCloneData> list1 = Lists.newArrayList();
        ArrayList<StaticCloneData> list2 = Lists.newArrayList();
        LinkedList<BlockPos> linkedlist = Lists.newLinkedList();
        BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);
        int k = structureboundingbox.minZ;
        block0: while (true) {
            if (k > structureboundingbox.maxZ) {
                if (flag) {
                    for (BlockPos blockpos6 : linkedlist) {
                        TileEntity tileentity1 = world.getTileEntity(blockpos6);
                        if (tileentity1 instanceof IInventory) {
                            ((IInventory)((Object)tileentity1)).clear();
                        }
                        world.setBlockState(blockpos6, Blocks.barrier.getDefaultState(), 2);
                    }
                    for (BlockPos blockpos7 : linkedlist) {
                        world.setBlockState(blockpos7, Blocks.air.getDefaultState(), 3);
                    }
                }
                ArrayList<StaticCloneData> list3 = Lists.newArrayList();
                list3.addAll(list);
                list3.addAll(list1);
                list3.addAll(list2);
                List<StaticCloneData> list4 = Lists.reverse(list3);
                for (StaticCloneData commandclone$staticclonedata : list4) {
                    TileEntity tileentity2 = world.getTileEntity(commandclone$staticclonedata.field_179537_a);
                    if (tileentity2 instanceof IInventory) {
                        ((IInventory)((Object)tileentity2)).clear();
                    }
                    world.setBlockState(commandclone$staticclonedata.field_179537_a, Blocks.barrier.getDefaultState(), 2);
                }
                i = 0;
                for (StaticCloneData commandclone$staticclonedata1 : list3) {
                    if (!world.setBlockState(commandclone$staticclonedata1.field_179537_a, commandclone$staticclonedata1.blockState, 2)) continue;
                    ++i;
                }
                for (StaticCloneData commandclone$staticclonedata2 : list1) {
                    TileEntity tileentity3 = world.getTileEntity(commandclone$staticclonedata2.field_179537_a);
                    if (commandclone$staticclonedata2.field_179536_c != null && tileentity3 != null) {
                        commandclone$staticclonedata2.field_179536_c.setInteger("x", commandclone$staticclonedata2.field_179537_a.getX());
                        commandclone$staticclonedata2.field_179536_c.setInteger("y", commandclone$staticclonedata2.field_179537_a.getY());
                        commandclone$staticclonedata2.field_179536_c.setInteger("z", commandclone$staticclonedata2.field_179537_a.getZ());
                        tileentity3.readFromNBT(commandclone$staticclonedata2.field_179536_c);
                        tileentity3.markDirty();
                    }
                    world.setBlockState(commandclone$staticclonedata2.field_179537_a, commandclone$staticclonedata2.blockState, 2);
                }
                for (StaticCloneData commandclone$staticclonedata3 : list4) {
                    world.notifyNeighborsRespectDebug(commandclone$staticclonedata3.field_179537_a, commandclone$staticclonedata3.blockState.getBlock());
                }
                List<NextTickListEntry> list5 = world.func_175712_a(structureboundingbox, false);
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
                CommandClone.notifyOperators(sender, (ICommand)this, "commands.clone.success", i);
                return;
            }
            int l = structureboundingbox.minY;
            while (true) {
                if (l <= structureboundingbox.maxY) {
                } else {
                    ++k;
                    continue block0;
                }
                for (int i1 = structureboundingbox.minX; i1 <= structureboundingbox.maxX; ++i1) {
                    BlockPos blockpos4 = new BlockPos(i1, l, k);
                    BlockPos blockpos5 = blockpos4.add(blockpos3);
                    IBlockState iblockstate = world.getBlockState(blockpos4);
                    if (flag1 && iblockstate.getBlock() == Blocks.air || block != null && (iblockstate.getBlock() != block || j >= 0 && iblockstate.getBlock().getMetaFromState(iblockstate) != j)) continue;
                    TileEntity tileentity = world.getTileEntity(blockpos4);
                    if (tileentity != null) {
                        NBTTagCompound nbttagcompound = new NBTTagCompound();
                        tileentity.writeToNBT(nbttagcompound);
                        list1.add(new StaticCloneData(blockpos5, iblockstate, nbttagcompound));
                        linkedlist.addLast(blockpos4);
                        continue;
                    }
                    if (!iblockstate.getBlock().isFullBlock() && !iblockstate.getBlock().isFullCube()) {
                        list2.add(new StaticCloneData(blockpos5, iblockstate, null));
                        linkedlist.addFirst(blockpos4);
                        continue;
                    }
                    list.add(new StaticCloneData(blockpos5, iblockstate, null));
                    linkedlist.addLast(blockpos4);
                }
                ++l;
            }
            break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> list;
        if (args.length > 0 && args.length <= 3) {
            list = CommandClone.func_175771_a(args, 0, pos);
            return list;
        }
        if (args.length > 3 && args.length <= 6) {
            list = CommandClone.func_175771_a(args, 3, pos);
            return list;
        }
        if (args.length > 6 && args.length <= 9) {
            list = CommandClone.func_175771_a(args, 6, pos);
            return list;
        }
        if (args.length == 10) {
            list = CommandClone.getListOfStringsMatchingLastWord(args, "replace", "masked", "filtered");
            return list;
        }
        if (args.length == 11) {
            list = CommandClone.getListOfStringsMatchingLastWord(args, "normal", "force", "move");
            return list;
        }
        if (args.length != 12) return null;
        if (!"filtered".equals(args[9])) return null;
        list = CommandClone.getListOfStringsMatchingLastWord(args, Block.blockRegistry.getKeys());
        return list;
    }

    static class StaticCloneData {
        public final BlockPos field_179537_a;
        public final IBlockState blockState;
        public final NBTTagCompound field_179536_c;

        public StaticCloneData(BlockPos p_i46037_1_, IBlockState p_i46037_2_, NBTTagCompound p_i46037_3_) {
            this.field_179537_a = p_i46037_1_;
            this.blockState = p_i46037_2_;
            this.field_179536_c = p_i46037_3_;
        }
    }
}

