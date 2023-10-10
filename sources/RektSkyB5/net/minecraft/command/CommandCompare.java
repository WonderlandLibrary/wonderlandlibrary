/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandCompare
extends CommandBase {
    @Override
    public String getCommandName() {
        return "testforblocks";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.compare.usage";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 9) {
            throw new WrongUsageException("commands.compare.usage", new Object[0]);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockpos = CommandCompare.parseBlockPos(sender, args, 0, false);
        BlockPos blockpos1 = CommandCompare.parseBlockPos(sender, args, 3, false);
        BlockPos blockpos2 = CommandCompare.parseBlockPos(sender, args, 6, false);
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos1);
        StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockpos2, blockpos2.add(structureboundingbox.func_175896_b()));
        int i2 = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();
        if (i2 > 524288) {
            throw new CommandException("commands.compare.tooManyBlocks", i2, 524288);
        }
        if (structureboundingbox.minY < 0 || structureboundingbox.maxY >= 256 || structureboundingbox1.minY < 0 || structureboundingbox1.maxY >= 256) throw new CommandException("commands.compare.outOfWorld", new Object[0]);
        World world = sender.getEntityWorld();
        if (!world.isAreaLoaded(structureboundingbox) || !world.isAreaLoaded(structureboundingbox1)) throw new CommandException("commands.compare.outOfWorld", new Object[0]);
        boolean flag = false;
        if (args.length > 9 && args[9].equals("masked")) {
            flag = true;
        }
        i2 = 0;
        BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
        for (int j2 = structureboundingbox.minZ; j2 <= structureboundingbox.maxZ; ++j2) {
            for (int k2 = structureboundingbox.minY; k2 <= structureboundingbox.maxY; ++k2) {
                for (int l2 = structureboundingbox.minX; l2 <= structureboundingbox.maxX; ++l2) {
                    blockpos$mutableblockpos.func_181079_c(l2, k2, j2);
                    blockpos$mutableblockpos1.func_181079_c(l2 + blockpos3.getX(), k2 + blockpos3.getY(), j2 + blockpos3.getZ());
                    boolean flag1 = false;
                    IBlockState iblockstate = world.getBlockState(blockpos$mutableblockpos);
                    if (flag && iblockstate.getBlock() == Blocks.air) continue;
                    if (iblockstate == world.getBlockState(blockpos$mutableblockpos1)) {
                        TileEntity tileentity = world.getTileEntity(blockpos$mutableblockpos);
                        TileEntity tileentity1 = world.getTileEntity(blockpos$mutableblockpos1);
                        if (tileentity != null && tileentity1 != null) {
                            NBTTagCompound nbttagcompound = new NBTTagCompound();
                            tileentity.writeToNBT(nbttagcompound);
                            nbttagcompound.removeTag("x");
                            nbttagcompound.removeTag("y");
                            nbttagcompound.removeTag("z");
                            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                            tileentity1.writeToNBT(nbttagcompound1);
                            nbttagcompound1.removeTag("x");
                            nbttagcompound1.removeTag("y");
                            nbttagcompound1.removeTag("z");
                            if (!nbttagcompound.equals(nbttagcompound1)) {
                                flag1 = true;
                            }
                        } else if (tileentity != null) {
                            flag1 = true;
                        }
                    } else {
                        flag1 = true;
                    }
                    ++i2;
                    if (!flag1) continue;
                    throw new CommandException("commands.compare.failed", new Object[0]);
                }
            }
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i2);
        CommandCompare.notifyOperators(sender, (ICommand)this, "commands.compare.success", i2);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length > 0 && args.length <= 3 ? CommandCompare.func_175771_a(args, 0, pos) : (args.length > 3 && args.length <= 6 ? CommandCompare.func_175771_a(args, 3, pos) : (args.length > 6 && args.length <= 9 ? CommandCompare.func_175771_a(args, 6, pos) : (args.length == 10 ? CommandCompare.getListOfStringsMatchingLastWord(args, "masked", "all") : null)));
    }
}

