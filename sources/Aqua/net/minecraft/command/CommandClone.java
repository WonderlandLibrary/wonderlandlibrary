package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandClone;
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
import net.minecraft.util.Vec3i;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandClone
extends CommandBase {
    public String getCommandName() {
        return "clone";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.clone.usage";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 9) {
            throw new WrongUsageException("commands.clone.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockpos = CommandClone.parseBlockPos((ICommandSender)sender, (String[])args, (int)0, (boolean)false);
        BlockPos blockpos1 = CommandClone.parseBlockPos((ICommandSender)sender, (String[])args, (int)3, (boolean)false);
        BlockPos blockpos2 = CommandClone.parseBlockPos((ICommandSender)sender, (String[])args, (int)6, (boolean)false);
        StructureBoundingBox structureboundingbox = new StructureBoundingBox((Vec3i)blockpos, (Vec3i)blockpos1);
        StructureBoundingBox structureboundingbox1 = new StructureBoundingBox((Vec3i)blockpos2, (Vec3i)blockpos2.add(structureboundingbox.func_175896_b()));
        int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
        if (i > 32768) {
            throw new CommandException("commands.clone.tooManyBlocks", new Object[]{i, 32768});
        }
        boolean flag = false;
        Block block = null;
        int j = -1;
        if ((args.length < 11 || !args[10].equals((Object)"force") && !args[10].equals((Object)"move")) && structureboundingbox.intersectsWith(structureboundingbox1)) {
            throw new CommandException("commands.clone.noOverlap", new Object[0]);
        }
        if (args.length >= 11 && args[10].equals((Object)"move")) {
            flag = true;
        }
        if (structureboundingbox.minY < 0 || structureboundingbox.maxY >= 256 || structureboundingbox1.minY < 0 || structureboundingbox1.maxY >= 256) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        World world = sender.getEntityWorld();
        if (!world.isAreaLoaded(structureboundingbox) || !world.isAreaLoaded(structureboundingbox1)) throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        boolean flag1 = false;
        if (args.length >= 10) {
            if (args[9].equals((Object)"masked")) {
                flag1 = true;
            } else if (args[9].equals((Object)"filtered")) {
                if (args.length < 12) {
                    throw new WrongUsageException("commands.clone.usage", new Object[0]);
                }
                block = CommandClone.getBlockByText((ICommandSender)sender, (String)args[11]);
                if (args.length >= 13) {
                    j = CommandClone.parseInt((String)args[12], (int)0, (int)15);
                }
            }
        }
        ArrayList list = Lists.newArrayList();
        ArrayList list1 = Lists.newArrayList();
        ArrayList list2 = Lists.newArrayList();
        LinkedList linkedlist = Lists.newLinkedList();
        BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);
        for (int k = structureboundingbox.minZ; k <= structureboundingbox.maxZ; ++k) {
            for (int l = structureboundingbox.minY; l <= structureboundingbox.maxY; ++l) {
                for (int i1 = structureboundingbox.minX; i1 <= structureboundingbox.maxX; ++i1) {
                    BlockPos blockpos4 = new BlockPos(i1, l, k);
                    BlockPos blockpos5 = blockpos4.add((Vec3i)blockpos3);
                    IBlockState iblockstate = world.getBlockState(blockpos4);
                    if (flag1 && iblockstate.getBlock() == Blocks.air || block != null && (iblockstate.getBlock() != block || j >= 0 && iblockstate.getBlock().getMetaFromState(iblockstate) != j)) continue;
                    TileEntity tileentity = world.getTileEntity(blockpos4);
                    if (tileentity != null) {
                        NBTTagCompound nbttagcompound = new NBTTagCompound();
                        tileentity.writeToNBT(nbttagcompound);
                        list1.add((Object)new StaticCloneData(blockpos5, iblockstate, nbttagcompound));
                        linkedlist.addLast((Object)blockpos4);
                        continue;
                    }
                    if (!iblockstate.getBlock().isFullBlock() && !iblockstate.getBlock().isFullCube()) {
                        list2.add((Object)new StaticCloneData(blockpos5, iblockstate, (NBTTagCompound)null));
                        linkedlist.addFirst((Object)blockpos4);
                        continue;
                    }
                    list.add((Object)new StaticCloneData(blockpos5, iblockstate, (NBTTagCompound)null));
                    linkedlist.addLast((Object)blockpos4);
                }
            }
        }
        if (flag) {
            for (BlockPos blockpos6 : linkedlist) {
                TileEntity tileentity1 = world.getTileEntity(blockpos6);
                if (tileentity1 instanceof IInventory) {
                    ((IInventory)tileentity1).clear();
                }
                world.setBlockState(blockpos6, Blocks.barrier.getDefaultState(), 2);
            }
            for (BlockPos blockpos7 : linkedlist) {
                world.setBlockState(blockpos7, Blocks.air.getDefaultState(), 3);
            }
        }
        ArrayList list3 = Lists.newArrayList();
        list3.addAll((Collection)list);
        list3.addAll((Collection)list1);
        list3.addAll((Collection)list2);
        List list4 = Lists.reverse((List)list3);
        for (StaticCloneData commandclone$staticclonedata : list4) {
            TileEntity tileentity2 = world.getTileEntity(commandclone$staticclonedata.pos);
            if (tileentity2 instanceof IInventory) {
                ((IInventory)tileentity2).clear();
            }
            world.setBlockState(commandclone$staticclonedata.pos, Blocks.barrier.getDefaultState(), 2);
        }
        i = 0;
        for (StaticCloneData commandclone$staticclonedata1 : list3) {
            if (!world.setBlockState(commandclone$staticclonedata1.pos, commandclone$staticclonedata1.blockState, 2)) continue;
            ++i;
        }
        for (StaticCloneData commandclone$staticclonedata2 : list1) {
            TileEntity tileentity3 = world.getTileEntity(commandclone$staticclonedata2.pos);
            if (commandclone$staticclonedata2.compound != null && tileentity3 != null) {
                commandclone$staticclonedata2.compound.setInteger("x", commandclone$staticclonedata2.pos.getX());
                commandclone$staticclonedata2.compound.setInteger("y", commandclone$staticclonedata2.pos.getY());
                commandclone$staticclonedata2.compound.setInteger("z", commandclone$staticclonedata2.pos.getZ());
                tileentity3.readFromNBT(commandclone$staticclonedata2.compound);
                tileentity3.markDirty();
            }
            world.setBlockState(commandclone$staticclonedata2.pos, commandclone$staticclonedata2.blockState, 2);
        }
        for (StaticCloneData commandclone$staticclonedata3 : list4) {
            world.notifyNeighborsRespectDebug(commandclone$staticclonedata3.pos, commandclone$staticclonedata3.blockState.getBlock());
        }
        List list5 = world.func_175712_a(structureboundingbox, false);
        if (list5 != null) {
            for (NextTickListEntry nextticklistentry : list5) {
                if (!structureboundingbox.isVecInside((Vec3i)nextticklistentry.position)) continue;
                BlockPos blockpos8 = nextticklistentry.position.add((Vec3i)blockpos3);
                world.scheduleBlockUpdate(blockpos8, nextticklistentry.getBlock(), (int)(nextticklistentry.scheduledTime - world.getWorldInfo().getWorldTotalTime()), nextticklistentry.priority);
            }
        }
        if (i <= 0) {
            throw new CommandException("commands.clone.failed", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
        CommandClone.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.clone.success", (Object[])new Object[]{i});
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length > 0 && args.length <= 3 ? CommandClone.func_175771_a((String[])args, (int)0, (BlockPos)pos) : (args.length > 3 && args.length <= 6 ? CommandClone.func_175771_a((String[])args, (int)3, (BlockPos)pos) : (args.length > 6 && args.length <= 9 ? CommandClone.func_175771_a((String[])args, (int)6, (BlockPos)pos) : (args.length == 10 ? CommandClone.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"replace", "masked", "filtered"}) : (args.length == 11 ? CommandClone.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"normal", "force", "move"}) : (args.length == 12 && "filtered".equals((Object)args[9]) ? CommandClone.getListOfStringsMatchingLastWord((String[])args, (Collection)Block.blockRegistry.getKeys()) : null)))));
    }
}
