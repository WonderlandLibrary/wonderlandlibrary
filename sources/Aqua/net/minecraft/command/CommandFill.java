package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
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
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandFill
extends CommandBase {
    public String getCommandName() {
        return "fill";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.fill.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        int j;
        block24: {
            block19: {
                if (args.length < 7) {
                    throw new WrongUsageException("commands.fill.usage", new Object[0]);
                }
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
                BlockPos blockpos = CommandFill.parseBlockPos((ICommandSender)sender, (String[])args, (int)0, (boolean)false);
                BlockPos blockpos1 = CommandFill.parseBlockPos((ICommandSender)sender, (String[])args, (int)3, (boolean)false);
                Block block = CommandBase.getBlockByText((ICommandSender)sender, (String)args[6]);
                int i = 0;
                if (args.length >= 8) {
                    i = CommandFill.parseInt((String)args[7], (int)0, (int)15);
                }
                BlockPos blockpos2 = new BlockPos(Math.min((int)blockpos.getX(), (int)blockpos1.getX()), Math.min((int)blockpos.getY(), (int)blockpos1.getY()), Math.min((int)blockpos.getZ(), (int)blockpos1.getZ()));
                BlockPos blockpos3 = new BlockPos(Math.max((int)blockpos.getX(), (int)blockpos1.getX()), Math.max((int)blockpos.getY(), (int)blockpos1.getY()), Math.max((int)blockpos.getZ(), (int)blockpos1.getZ()));
                j = (blockpos3.getX() - blockpos2.getX() + 1) * (blockpos3.getY() - blockpos2.getY() + 1) * (blockpos3.getZ() - blockpos2.getZ() + 1);
                if (j > 32768) {
                    throw new CommandException("commands.fill.tooManyBlocks", new Object[]{j, 32768});
                }
                if (blockpos2.getY() < 0 || blockpos3.getY() >= 256) break block19;
                World world = sender.getEntityWorld();
                for (int k = blockpos2.getZ(); k < blockpos3.getZ() + 16; k += 16) {
                    for (int l = blockpos2.getX(); l < blockpos3.getX() + 16; l += 16) {
                        if (world.isBlockLoaded(new BlockPos(l, blockpos3.getY() - blockpos2.getY(), k))) continue;
                        throw new CommandException("commands.fill.outOfWorld", new Object[0]);
                    }
                }
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                boolean flag = false;
                if (args.length >= 10 && block.hasTileEntity()) {
                    String s = CommandFill.getChatComponentFromNthArg((ICommandSender)sender, (String[])args, (int)9).getUnformattedText();
                    try {
                        nbttagcompound = JsonToNBT.getTagFromJson((String)s);
                        flag = true;
                    }
                    catch (NBTException nbtexception) {
                        throw new CommandException("commands.fill.tagError", new Object[]{nbtexception.getMessage()});
                    }
                }
                ArrayList list = Lists.newArrayList();
                j = 0;
                for (int i1 = blockpos2.getZ(); i1 <= blockpos3.getZ(); ++i1) {
                    for (int j1 = blockpos2.getY(); j1 <= blockpos3.getY(); ++j1) {
                        for (int k1 = blockpos2.getX(); k1 <= blockpos3.getX(); ++k1) {
                            TileEntity tileentity;
                            IBlockState iblockstate1;
                            TileEntity tileentity1;
                            BlockPos blockpos4;
                            block20: {
                                block21: {
                                    block23: {
                                        block22: {
                                            blockpos4 = new BlockPos(k1, j1, i1);
                                            if (args.length < 9) break block20;
                                            if (args[8].equals((Object)"outline") || args[8].equals((Object)"hollow")) break block21;
                                            if (!args[8].equals((Object)"destroy")) break block22;
                                            world.destroyBlock(blockpos4, true);
                                            break block20;
                                        }
                                        if (!args[8].equals((Object)"keep")) break block23;
                                        if (!world.isAirBlock(blockpos4)) {
                                            continue;
                                        }
                                        break block20;
                                    }
                                    if (!args[8].equals((Object)"replace") || block.hasTileEntity()) break block20;
                                    if (args.length > 9) {
                                        Block block1 = CommandBase.getBlockByText((ICommandSender)sender, (String)args[9]);
                                        if (world.getBlockState(blockpos4).getBlock() != block1) continue;
                                    }
                                    if (args.length <= 10) break block20;
                                    int l1 = CommandBase.parseInt((String)args[10]);
                                    IBlockState iblockstate = world.getBlockState(blockpos4);
                                    if (iblockstate.getBlock().getMetaFromState(iblockstate) != l1) {
                                        continue;
                                    }
                                    break block20;
                                }
                                if (k1 != blockpos2.getX() && k1 != blockpos3.getX() && j1 != blockpos2.getY() && j1 != blockpos3.getY() && i1 != blockpos2.getZ() && i1 != blockpos3.getZ()) {
                                    if (!args[8].equals((Object)"hollow")) continue;
                                    world.setBlockState(blockpos4, Blocks.air.getDefaultState(), 2);
                                    list.add((Object)blockpos4);
                                    continue;
                                }
                            }
                            if ((tileentity1 = world.getTileEntity(blockpos4)) != null) {
                                if (tileentity1 instanceof IInventory) {
                                    ((IInventory)tileentity1).clear();
                                }
                                world.setBlockState(blockpos4, Blocks.barrier.getDefaultState(), block == Blocks.barrier ? 2 : 4);
                            }
                            if (!world.setBlockState(blockpos4, iblockstate1 = block.getStateFromMeta(i), 2)) continue;
                            list.add((Object)blockpos4);
                            ++j;
                            if (!flag || (tileentity = world.getTileEntity(blockpos4)) == null) continue;
                            nbttagcompound.setInteger("x", blockpos4.getX());
                            nbttagcompound.setInteger("y", blockpos4.getY());
                            nbttagcompound.setInteger("z", blockpos4.getZ());
                            tileentity.readFromNBT(nbttagcompound);
                        }
                    }
                }
                for (BlockPos blockpos5 : list) {
                    Block block2 = world.getBlockState(blockpos5).getBlock();
                    world.notifyNeighborsRespectDebug(blockpos5, block2);
                }
                if (j <= 0) {
                    throw new CommandException("commands.fill.failed", new Object[0]);
                }
                break block24;
            }
            throw new CommandException("commands.fill.outOfWorld", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, j);
        CommandFill.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.fill.success", (Object[])new Object[]{j});
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length > 0 && args.length <= 3 ? CommandFill.func_175771_a((String[])args, (int)0, (BlockPos)pos) : (args.length > 3 && args.length <= 6 ? CommandFill.func_175771_a((String[])args, (int)3, (BlockPos)pos) : (args.length == 7 ? CommandFill.getListOfStringsMatchingLastWord((String[])args, (Collection)Block.blockRegistry.getKeys()) : (args.length == 9 ? CommandFill.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"replace", "destroy", "keep", "hollow", "outline"}) : (args.length == 10 && "replace".equals((Object)args[8]) ? CommandFill.getListOfStringsMatchingLastWord((String[])args, (Collection)Block.blockRegistry.getKeys()) : null))));
    }
}
