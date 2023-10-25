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
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandName() {
        return "testforblocks";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.compare.usage";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 9) {
            throw new WrongUsageException("commands.compare.usage", new Object[0]);
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockPos = CommandCompare.parseBlockPos(iCommandSender, stringArray, 0, false);
        BlockPos blockPos2 = CommandCompare.parseBlockPos(iCommandSender, stringArray, 3, false);
        BlockPos blockPos3 = CommandCompare.parseBlockPos(iCommandSender, stringArray, 6, false);
        StructureBoundingBox structureBoundingBox = new StructureBoundingBox(blockPos, blockPos2);
        StructureBoundingBox structureBoundingBox2 = new StructureBoundingBox(blockPos3, blockPos3.add(structureBoundingBox.func_175896_b()));
        int n = structureBoundingBox.getXSize() * structureBoundingBox.getYSize() * structureBoundingBox.getZSize();
        if (n > 524288) {
            throw new CommandException("commands.compare.tooManyBlocks", n, 524288);
        }
        if (structureBoundingBox.minY < 0 || structureBoundingBox.maxY >= 256 || structureBoundingBox2.minY < 0 || structureBoundingBox2.maxY >= 256) throw new CommandException("commands.compare.outOfWorld", new Object[0]);
        World world = iCommandSender.getEntityWorld();
        if (!world.isAreaLoaded(structureBoundingBox) || !world.isAreaLoaded(structureBoundingBox2)) throw new CommandException("commands.compare.outOfWorld", new Object[0]);
        boolean bl = false;
        if (stringArray.length > 9 && stringArray[9].equals("masked")) {
            bl = true;
        }
        n = 0;
        BlockPos blockPos4 = new BlockPos(structureBoundingBox2.minX - structureBoundingBox.minX, structureBoundingBox2.minY - structureBoundingBox.minY, structureBoundingBox2.minZ - structureBoundingBox.minZ);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();
        int n2 = structureBoundingBox.minZ;
        while (n2 <= structureBoundingBox.maxZ) {
            int n3 = structureBoundingBox.minY;
            while (n3 <= structureBoundingBox.maxY) {
                int n4 = structureBoundingBox.minX;
                while (n4 <= structureBoundingBox.maxX) {
                    mutableBlockPos.func_181079_c(n4, n3, n2);
                    mutableBlockPos2.func_181079_c(n4 + blockPos4.getX(), n3 + blockPos4.getY(), n2 + blockPos4.getZ());
                    boolean bl2 = false;
                    IBlockState iBlockState = world.getBlockState(mutableBlockPos);
                    if (!bl || iBlockState.getBlock() != Blocks.air) {
                        if (iBlockState == world.getBlockState(mutableBlockPos2)) {
                            TileEntity tileEntity = world.getTileEntity(mutableBlockPos);
                            TileEntity tileEntity2 = world.getTileEntity(mutableBlockPos2);
                            if (tileEntity != null && tileEntity2 != null) {
                                NBTTagCompound nBTTagCompound = new NBTTagCompound();
                                tileEntity.writeToNBT(nBTTagCompound);
                                nBTTagCompound.removeTag("x");
                                nBTTagCompound.removeTag("y");
                                nBTTagCompound.removeTag("z");
                                NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
                                tileEntity2.writeToNBT(nBTTagCompound2);
                                nBTTagCompound2.removeTag("x");
                                nBTTagCompound2.removeTag("y");
                                nBTTagCompound2.removeTag("z");
                                if (!nBTTagCompound.equals(nBTTagCompound2)) {
                                    bl2 = true;
                                }
                            } else if (tileEntity != null) {
                                bl2 = true;
                            }
                        } else {
                            bl2 = true;
                        }
                        ++n;
                        if (bl2) {
                            throw new CommandException("commands.compare.failed", new Object[0]);
                        }
                    }
                    ++n4;
                }
                ++n3;
            }
            ++n2;
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, n);
        CommandCompare.notifyOperators(iCommandSender, (ICommand)this, "commands.compare.success", n);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length > 0 && stringArray.length <= 3 ? CommandCompare.func_175771_a(stringArray, 0, blockPos) : (stringArray.length > 3 && stringArray.length <= 6 ? CommandCompare.func_175771_a(stringArray, 3, blockPos) : (stringArray.length > 6 && stringArray.length <= 9 ? CommandCompare.func_175771_a(stringArray, 6, blockPos) : (stringArray.length == 10 ? CommandCompare.getListOfStringsMatchingLastWord(stringArray, "masked", "all") : null)));
    }
}

