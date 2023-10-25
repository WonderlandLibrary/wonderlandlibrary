/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class CommandFill
extends CommandBase {
    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.fill.usage";
    }

    @Override
    public String getCommandName() {
        return "fill";
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void processCommand(ICommandSender var1_1, String[] var2_2) throws CommandException {
        block27: {
            block20: {
                if (var2_2.length < 7) {
                    throw new WrongUsageException("commands.fill.usage", new Object[0]);
                }
                var1_1.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
                var3_3 = CommandFill.parseBlockPos(var1_1, var2_2, 0, false);
                var4_4 = CommandFill.parseBlockPos(var1_1, var2_2, 3, false);
                var5_5 = CommandBase.getBlockByText(var1_1, var2_2[6]);
                var6_6 = 0;
                if (var2_2.length >= 8) {
                    var6_6 = CommandFill.parseInt(var2_2[7], 0, 15);
                }
                var7_7 = new BlockPos(Math.min(var3_3.getX(), var4_4.getX()), Math.min(var3_3.getY(), var4_4.getY()), Math.min(var3_3.getZ(), var4_4.getZ()));
                var8_8 = new BlockPos(Math.max(var3_3.getX(), var4_4.getX()), Math.max(var3_3.getY(), var4_4.getY()), Math.max(var3_3.getZ(), var4_4.getZ()));
                var9_9 = (var8_8.getX() - var7_7.getX() + 1) * (var8_8.getY() - var7_7.getY() + 1) * (var8_8.getZ() - var7_7.getZ() + 1);
                if (var9_9 > 32768) {
                    throw new CommandException("commands.fill.tooManyBlocks", new Object[]{var9_9, 32768});
                }
                if (var7_7.getY() < 0 || var8_8.getY() >= 256) break block20;
                var10_10 = var1_1.getEntityWorld();
                var11_11 = var7_7.getZ();
                while (var11_11 < var8_8.getZ() + 16) {
                    var12_13 = var7_7.getX();
                    while (var12_13 < var8_8.getX() + 16) {
                        if (!var10_10.isBlockLoaded(new BlockPos(var12_13, var8_8.getY() - var7_7.getY(), var11_11))) {
                            throw new CommandException("commands.fill.outOfWorld", new Object[0]);
                        }
                        var12_13 += 16;
                    }
                    var11_11 += 16;
                }
                var11_12 = new NBTTagCompound();
                var12_13 = 0;
                if (var2_2.length >= 10 && var5_5.hasTileEntity()) {
                    var13_14 = CommandFill.getChatComponentFromNthArg(var1_1, var2_2, 9).getUnformattedText();
                    try {
                        var11_12 = JsonToNBT.getTagFromJson((String)var13_14);
                        var12_13 = 1;
                    }
                    catch (NBTException var14_15) {
                        throw new CommandException("commands.fill.tagError", new Object[]{var14_15.getMessage()});
                    }
                }
                var13_14 = Lists.newArrayList();
                var9_9 = 0;
                var14_16 = var7_7.getZ();
                while (var14_16 <= var8_8.getZ()) {
                    var15_18 = var7_7.getY();
                    while (var15_18 <= var8_8.getY()) {
                        var16_20 = var7_7.getX();
                        while (var16_20 <= var8_8.getX()) {
                            block25: {
                                block21: {
                                    block22: {
                                        block26: {
                                            block24: {
                                                block23: {
                                                    var17_23 = new BlockPos(var16_20, var15_18, var14_16);
                                                    if (var2_2.length < 9) break block21;
                                                    if (var2_2[8].equals("outline") || var2_2[8].equals("hollow")) break block22;
                                                    if (!var2_2[8].equals("destroy")) break block23;
                                                    var10_10.destroyBlock(var17_23, true);
                                                    break block21;
                                                }
                                                if (!var2_2[8].equals("keep")) break block24;
                                                if (var10_10.isAirBlock(var17_23)) break block21;
                                                break block25;
                                            }
                                            if (!var2_2[8].equals("replace") || var5_5.hasTileEntity()) break block21;
                                            if (var2_2.length <= 9) break block26;
                                            var18_24 = CommandBase.getBlockByText(var1_1, var2_2[9]);
                                            if (var10_10.getBlockState(var17_23).getBlock() != var18_24) break block25;
                                        }
                                        if (var2_2.length <= 10) break block21;
                                        var18_25 = CommandBase.parseInt(var2_2[10]);
                                        var19_26 = var10_10.getBlockState(var17_23);
                                        if (var19_26.getBlock().getMetaFromState(var19_26) == var18_25) break block21;
                                        break block25;
                                    }
                                    if (var16_20 != var7_7.getX() && var16_20 != var8_8.getX() && var15_18 != var7_7.getY() && var15_18 != var8_8.getY() && var14_16 != var7_7.getZ() && var14_16 != var8_8.getZ()) {
                                        if (var2_2[8].equals("hollow")) {
                                            var10_10.setBlockState(var17_23, Blocks.air.getDefaultState(), 2);
                                            var13_14.add(var17_23);
                                            ** GOTO lbl92
                                        } else {
                                            ** GOTO lbl76
                                        }
                                    }
                                    break block21;
lbl76:
                                    // 2 sources

                                    break block25;
                                }
                                if ((var18_24 = var10_10.getTileEntity(var17_23)) != null) {
                                    if (var18_24 instanceof IInventory) {
                                        ((IInventory)var18_24).clear();
                                    }
                                    var10_10.setBlockState(var17_23, Blocks.barrier.getDefaultState(), var5_5 == Blocks.barrier ? 2 : 4);
                                }
                                if (var10_10.setBlockState(var17_23, var19_26 = var5_5.getStateFromMeta(var6_6), 2)) {
                                    var13_14.add(var17_23);
                                    ++var9_9;
                                    if (var12_13 != 0 && (var20_27 = var10_10.getTileEntity(var17_23)) != null) {
                                        var11_12.setInteger("x", var17_23.getX());
                                        var11_12.setInteger("y", var17_23.getY());
                                        var11_12.setInteger("z", var17_23.getZ());
                                        var20_27.readFromNBT(var11_12);
                                    }
                                }
                            }
                            ++var16_20;
                        }
                        ++var15_18;
                    }
                    ++var14_16;
                }
                var15_19 = var13_14.iterator();
                while (var15_19.hasNext()) {
                    var14_17 = (BlockPos)var15_19.next();
                    var16_22 = var10_10.getBlockState(var14_17).getBlock();
                    var10_10.notifyNeighborsRespectDebug(var14_17, var16_22);
                }
                if (var9_9 <= 0) {
                    throw new CommandException("commands.fill.failed", new Object[0]);
                }
                break block27;
            }
            throw new CommandException("commands.fill.outOfWorld", new Object[0]);
        }
        var1_1.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, var9_9);
        CommandFill.notifyOperators(var1_1, (ICommand)this, "commands.fill.success", new Object[]{var9_9});
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length > 0 && stringArray.length <= 3 ? CommandFill.func_175771_a(stringArray, 0, blockPos) : (stringArray.length > 3 && stringArray.length <= 6 ? CommandFill.func_175771_a(stringArray, 3, blockPos) : (stringArray.length == 7 ? CommandFill.getListOfStringsMatchingLastWord(stringArray, Block.blockRegistry.getKeys()) : (stringArray.length == 9 ? CommandFill.getListOfStringsMatchingLastWord(stringArray, "replace", "destroy", "keep", "hollow", "outline") : (stringArray.length == 10 && "replace".equals(stringArray[8]) ? CommandFill.getListOfStringsMatchingLastWord(stringArray, Block.blockRegistry.getKeys()) : null))));
    }
}

