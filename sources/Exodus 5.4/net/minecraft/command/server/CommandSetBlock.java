/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

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

public class CommandSetBlock
extends CommandBase {
    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.setblock.usage";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length > 0 && stringArray.length <= 3 ? CommandSetBlock.func_175771_a(stringArray, 0, blockPos) : (stringArray.length == 4 ? CommandSetBlock.getListOfStringsMatchingLastWord(stringArray, Block.blockRegistry.getKeys()) : (stringArray.length == 6 ? CommandSetBlock.getListOfStringsMatchingLastWord(stringArray, "replace", "destroy", "keep") : null));
    }

    @Override
    public String getCommandName() {
        return "setblock";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        TileEntity tileEntity;
        IBlockState iBlockState;
        Object object;
        World world;
        if (stringArray.length < 4) {
            throw new WrongUsageException("commands.setblock.usage", new Object[0]);
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockPos = CommandSetBlock.parseBlockPos(iCommandSender, stringArray, 0, false);
        Block block = CommandBase.getBlockByText(iCommandSender, stringArray[3]);
        int n = 0;
        if (stringArray.length >= 5) {
            n = CommandSetBlock.parseInt(stringArray[4], 0, 15);
        }
        if (!(world = iCommandSender.getEntityWorld()).isBlockLoaded(blockPos)) {
            throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
        }
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        boolean bl = false;
        if (stringArray.length >= 7 && block.hasTileEntity()) {
            object = CommandSetBlock.getChatComponentFromNthArg(iCommandSender, stringArray, 6).getUnformattedText();
            try {
                nBTTagCompound = JsonToNBT.getTagFromJson((String)object);
                bl = true;
            }
            catch (NBTException nBTException) {
                throw new CommandException("commands.setblock.tagError", nBTException.getMessage());
            }
        }
        if (stringArray.length >= 6) {
            if (stringArray[5].equals("destroy")) {
                world.destroyBlock(blockPos, true);
                if (block == Blocks.air) {
                    CommandSetBlock.notifyOperators(iCommandSender, (ICommand)this, "commands.setblock.success", new Object[0]);
                    return;
                }
            } else if (stringArray[5].equals("keep") && !world.isAirBlock(blockPos)) {
                throw new CommandException("commands.setblock.noChange", new Object[0]);
            }
        }
        if ((object = world.getTileEntity(blockPos)) != null) {
            if (object instanceof IInventory) {
                ((IInventory)object).clear();
            }
            world.setBlockState(blockPos, Blocks.air.getDefaultState(), block == Blocks.air ? 2 : 4);
        }
        if (!world.setBlockState(blockPos, iBlockState = block.getStateFromMeta(n), 2)) {
            throw new CommandException("commands.setblock.noChange", new Object[0]);
        }
        if (bl && (tileEntity = world.getTileEntity(blockPos)) != null) {
            nBTTagCompound.setInteger("x", blockPos.getX());
            nBTTagCompound.setInteger("y", blockPos.getY());
            nBTTagCompound.setInteger("z", blockPos.getZ());
            tileEntity.readFromNBT(nBTTagCompound);
        }
        world.notifyNeighborsRespectDebug(blockPos, iBlockState.getBlock());
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandSetBlock.notifyOperators(iCommandSender, (ICommand)this, "commands.setblock.success", new Object[0]);
    }
}

