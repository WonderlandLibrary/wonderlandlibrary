// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import java.util.LinkedList;
import java.util.ArrayList;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.world.NextTickListEntry;
import java.util.List;
import java.util.Collection;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Lists;
import net.minecraft.util.Vec3i;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandClone extends CommandBase
{
    private static final String __OBFID = "CL_00002348";
    
    @Override
    public String getCommandName() {
        return "clone";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.clone.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 9) {
            throw new WrongUsageException("commands.clone.usage", new Object[0]);
        }
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos var3 = CommandBase.func_175757_a(sender, args, 0, false);
        final BlockPos var4 = CommandBase.func_175757_a(sender, args, 3, false);
        final BlockPos var5 = CommandBase.func_175757_a(sender, args, 6, false);
        final StructureBoundingBox var6 = new StructureBoundingBox(var3, var4);
        final StructureBoundingBox var7 = new StructureBoundingBox(var5, var5.add(var6.func_175896_b()));
        int var8 = var6.getXSize() * var6.getYSize() * var6.getZSize();
        if (var8 > 32768) {
            throw new CommandException("commands.clone.tooManyBlocks", new Object[] { var8, 32768 });
        }
        boolean var9 = false;
        Block var10 = null;
        int var11 = -1;
        if ((args.length < 11 || (!args[10].equals("force") && !args[10].equals("move"))) && var6.intersectsWith(var7)) {
            throw new CommandException("commands.clone.noOverlap", new Object[0]);
        }
        if (args.length >= 11 && args[10].equals("move")) {
            var9 = true;
        }
        if (var6.minY < 0 || var6.maxY >= 256 || var7.minY < 0 || var7.maxY >= 256) {
            throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        }
        final World var12 = sender.getEntityWorld();
        if (!var12.isAreaLoaded(var6) || !var12.isAreaLoaded(var7)) {
            throw new CommandException("commands.clone.outOfWorld", new Object[0]);
        }
        boolean var13 = false;
        if (args.length >= 10) {
            if (args[9].equals("masked")) {
                var13 = true;
            }
            else if (args[9].equals("filtered")) {
                if (args.length < 12) {
                    throw new WrongUsageException("commands.clone.usage", new Object[0]);
                }
                var10 = CommandBase.getBlockByText(sender, args[11]);
                if (args.length >= 13) {
                    var11 = CommandBase.parseInt(args[12], 0, 15);
                }
            }
        }
        final ArrayList var14 = Lists.newArrayList();
        final ArrayList var15 = Lists.newArrayList();
        final ArrayList var16 = Lists.newArrayList();
        final LinkedList var17 = Lists.newLinkedList();
        final BlockPos var18 = new BlockPos(var7.minX - var6.minX, var7.minY - var6.minY, var7.minZ - var6.minZ);
        for (int var19 = var6.minZ; var19 <= var6.maxZ; ++var19) {
            for (int var20 = var6.minY; var20 <= var6.maxY; ++var20) {
                for (int var21 = var6.minX; var21 <= var6.maxX; ++var21) {
                    final BlockPos var22 = new BlockPos(var21, var20, var19);
                    final BlockPos var23 = var22.add(var18);
                    final IBlockState var24 = var12.getBlockState(var22);
                    if ((!var13 || var24.getBlock() != Blocks.air) && (var10 == null || (var24.getBlock() == var10 && (var11 < 0 || var24.getBlock().getMetaFromState(var24) == var11)))) {
                        final TileEntity var25 = var12.getTileEntity(var22);
                        if (var25 != null) {
                            final NBTTagCompound var26 = new NBTTagCompound();
                            var25.writeToNBT(var26);
                            var15.add(new StaticCloneData(var23, var24, var26));
                            var17.addLast(var22);
                        }
                        else if (!var24.getBlock().isFullBlock() && !var24.getBlock().isFullCube()) {
                            var16.add(new StaticCloneData(var23, var24, null));
                            var17.addFirst(var22);
                        }
                        else {
                            var14.add(new StaticCloneData(var23, var24, null));
                            var17.addLast(var22);
                        }
                    }
                }
            }
        }
        if (var9) {
            for (final BlockPos var28 : var17) {
                final TileEntity var29 = var12.getTileEntity(var28);
                if (var29 instanceof IInventory) {
                    ((IInventory)var29).clearInventory();
                }
                var12.setBlockState(var28, Blocks.barrier.getDefaultState(), 2);
            }
            for (final BlockPos var28 : var17) {
                var12.setBlockState(var28, Blocks.air.getDefaultState(), 3);
            }
        }
        final ArrayList var30 = Lists.newArrayList();
        var30.addAll(var14);
        var30.addAll(var15);
        var30.addAll(var16);
        final List var31 = Lists.reverse((List)var30);
        for (final StaticCloneData var33 : var31) {
            final TileEntity var34 = var12.getTileEntity(var33.field_179537_a);
            if (var34 instanceof IInventory) {
                ((IInventory)var34).clearInventory();
            }
            var12.setBlockState(var33.field_179537_a, Blocks.barrier.getDefaultState(), 2);
        }
        var8 = 0;
        for (final StaticCloneData var33 : var30) {
            if (var12.setBlockState(var33.field_179537_a, var33.field_179535_b, 2)) {
                ++var8;
            }
        }
        for (final StaticCloneData var33 : var15) {
            final TileEntity var34 = var12.getTileEntity(var33.field_179537_a);
            if (var33.field_179536_c != null && var34 != null) {
                var33.field_179536_c.setInteger("x", var33.field_179537_a.getX());
                var33.field_179536_c.setInteger("y", var33.field_179537_a.getY());
                var33.field_179536_c.setInteger("z", var33.field_179537_a.getZ());
                var34.readFromNBT(var33.field_179536_c);
                var34.markDirty();
            }
            var12.setBlockState(var33.field_179537_a, var33.field_179535_b, 2);
        }
        for (final StaticCloneData var33 : var31) {
            var12.func_175722_b(var33.field_179537_a, var33.field_179535_b.getBlock());
        }
        final List var35 = var12.func_175712_a(var6, false);
        if (var35 != null) {
            for (final NextTickListEntry var37 : var35) {
                if (var6.func_175898_b(var37.field_180282_a)) {
                    final BlockPos var38 = var37.field_180282_a.add(var18);
                    var12.func_180497_b(var38, var37.func_151351_a(), (int)(var37.scheduledTime - var12.getWorldInfo().getWorldTotalTime()), var37.priority);
                }
            }
        }
        if (var8 <= 0) {
            throw new CommandException("commands.clone.failed", new Object[0]);
        }
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, var8);
        CommandBase.notifyOperators(sender, this, "commands.clone.success", var8);
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.func_175771_a(args, 0, pos) : ((args.length > 3 && args.length <= 6) ? CommandBase.func_175771_a(args, 3, pos) : ((args.length > 6 && args.length <= 9) ? CommandBase.func_175771_a(args, 6, pos) : ((args.length == 10) ? CommandBase.getListOfStringsMatchingLastWord(args, "replace", "masked", "filtered") : ((args.length == 11) ? CommandBase.getListOfStringsMatchingLastWord(args, "normal", "force", "move") : ((args.length == 12 && "filtered".equals(args[9])) ? CommandBase.func_175762_a(args, Block.blockRegistry.getKeys()) : null)))));
    }
    
    static class StaticCloneData
    {
        public final BlockPos field_179537_a;
        public final IBlockState field_179535_b;
        public final NBTTagCompound field_179536_c;
        private static final String __OBFID = "CL_00002347";
        
        public StaticCloneData(final BlockPos p_i46037_1_, final IBlockState p_i46037_2_, final NBTTagCompound p_i46037_3_) {
            this.field_179537_a = p_i46037_1_;
            this.field_179535_b = p_i46037_2_;
            this.field_179536_c = p_i46037_3_;
        }
    }
}
