// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import java.util.ArrayList;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.init.Blocks;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class CommandFill extends CommandBase
{
    private static final String __OBFID = "CL_00002342";
    
    @Override
    public String getCommandName() {
        return "fill";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.fill.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 7) {
            throw new WrongUsageException("commands.fill.usage", new Object[0]);
        }
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        final BlockPos var3 = CommandBase.func_175757_a(sender, args, 0, false);
        final BlockPos var4 = CommandBase.func_175757_a(sender, args, 3, false);
        final Block var5 = CommandBase.getBlockByText(sender, args[6]);
        int var6 = 0;
        if (args.length >= 8) {
            var6 = CommandBase.parseInt(args[7], 0, 15);
        }
        final BlockPos var7 = new BlockPos(Math.min(var3.getX(), var4.getX()), Math.min(var3.getY(), var4.getY()), Math.min(var3.getZ(), var4.getZ()));
        final BlockPos var8 = new BlockPos(Math.max(var3.getX(), var4.getX()), Math.max(var3.getY(), var4.getY()), Math.max(var3.getZ(), var4.getZ()));
        int var9 = (var8.getX() - var7.getX() + 1) * (var8.getY() - var7.getY() + 1) * (var8.getZ() - var7.getZ() + 1);
        if (var9 > 32768) {
            throw new CommandException("commands.fill.tooManyBlocks", new Object[] { var9, 32768 });
        }
        if (var7.getY() < 0 || var8.getY() >= 256) {
            throw new CommandException("commands.fill.outOfWorld", new Object[0]);
        }
        final World var10 = sender.getEntityWorld();
        for (int var11 = var7.getZ(); var11 < var8.getZ() + 16; var11 += 16) {
            for (int var12 = var7.getX(); var12 < var8.getX() + 16; var12 += 16) {
                if (!var10.isBlockLoaded(new BlockPos(var12, var8.getY() - var7.getY(), var11))) {
                    throw new CommandException("commands.fill.outOfWorld", new Object[0]);
                }
            }
        }
        NBTTagCompound var13 = new NBTTagCompound();
        boolean var14 = false;
        if (args.length >= 10 && var5.hasTileEntity()) {
            final String var15 = CommandBase.getChatComponentFromNthArg(sender, args, 9).getUnformattedText();
            try {
                var13 = JsonToNBT.func_180713_a(var15);
                var14 = true;
            }
            catch (NBTException var16) {
                throw new CommandException("commands.fill.tagError", new Object[] { var16.getMessage() });
            }
        }
        final ArrayList var17 = Lists.newArrayList();
        var9 = 0;
        for (int var18 = var7.getZ(); var18 <= var8.getZ(); ++var18) {
            for (int var19 = var7.getY(); var19 <= var8.getY(); ++var19) {
                for (int var20 = var7.getX(); var20 <= var8.getX(); ++var20) {
                    final BlockPos var21 = new BlockPos(var20, var19, var18);
                    if (args.length >= 9) {
                        if (!args[8].equals("outline") && !args[8].equals("hollow")) {
                            if (args[8].equals("destroy")) {
                                var10.destroyBlock(var21, true);
                            }
                            else if (args[8].equals("keep")) {
                                if (!var10.isAirBlock(var21)) {
                                    continue;
                                }
                            }
                            else if (args[8].equals("replace") && !var5.hasTileEntity()) {
                                if (args.length > 9) {
                                    final Block var22 = CommandBase.getBlockByText(sender, args[9]);
                                    if (var10.getBlockState(var21).getBlock() != var22) {
                                        continue;
                                    }
                                }
                                if (args.length > 10) {
                                    final int var23 = CommandBase.parseInt(args[10]);
                                    final IBlockState var24 = var10.getBlockState(var21);
                                    if (var24.getBlock().getMetaFromState(var24) != var23) {
                                        continue;
                                    }
                                }
                            }
                        }
                        else if (var20 != var7.getX() && var20 != var8.getX() && var19 != var7.getY() && var19 != var8.getY() && var18 != var7.getZ() && var18 != var8.getZ()) {
                            if (args[8].equals("hollow")) {
                                var10.setBlockState(var21, Blocks.air.getDefaultState(), 2);
                                var17.add(var21);
                            }
                            continue;
                        }
                    }
                    final TileEntity var25 = var10.getTileEntity(var21);
                    if (var25 != null) {
                        if (var25 instanceof IInventory) {
                            ((IInventory)var25).clearInventory();
                        }
                        var10.setBlockState(var21, Blocks.barrier.getDefaultState(), (var5 == Blocks.barrier) ? 2 : 4);
                    }
                    final IBlockState var24 = var5.getStateFromMeta(var6);
                    if (var10.setBlockState(var21, var24, 2)) {
                        var17.add(var21);
                        ++var9;
                        if (var14) {
                            final TileEntity var26 = var10.getTileEntity(var21);
                            if (var26 != null) {
                                var13.setInteger("x", var21.getX());
                                var13.setInteger("y", var21.getY());
                                var13.setInteger("z", var21.getZ());
                                var26.readFromNBT(var13);
                            }
                        }
                    }
                }
            }
        }
        for (final BlockPos var28 : var17) {
            final Block var29 = var10.getBlockState(var28).getBlock();
            var10.func_175722_b(var28, var29);
        }
        if (var9 <= 0) {
            throw new CommandException("commands.fill.failed", new Object[0]);
        }
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, var9);
        CommandBase.notifyOperators(sender, this, "commands.fill.success", var9);
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length > 0 && args.length <= 3) ? CommandBase.func_175771_a(args, 0, pos) : ((args.length > 3 && args.length <= 6) ? CommandBase.func_175771_a(args, 3, pos) : ((args.length == 7) ? CommandBase.func_175762_a(args, Block.blockRegistry.getKeys()) : ((args.length == 9) ? CommandBase.getListOfStringsMatchingLastWord(args, "replace", "destroy", "keep", "hollow", "outline") : null)));
    }
}
