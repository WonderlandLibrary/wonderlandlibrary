/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CommandBlockData
extends CommandBase {
    @Override
    public String getCommandName() {
        return "blockdata";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        NBTTagCompound nBTTagCompound;
        if (stringArray.length < 4) {
            throw new WrongUsageException("commands.blockdata.usage", new Object[0]);
        }
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
        BlockPos blockPos = CommandBlockData.parseBlockPos(iCommandSender, stringArray, 0, false);
        World world = iCommandSender.getEntityWorld();
        if (!world.isBlockLoaded(blockPos)) {
            throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
        }
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity == null) {
            throw new CommandException("commands.blockdata.notValid", new Object[0]);
        }
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        tileEntity.writeToNBT(nBTTagCompound2);
        NBTTagCompound nBTTagCompound3 = (NBTTagCompound)nBTTagCompound2.copy();
        try {
            nBTTagCompound = JsonToNBT.getTagFromJson(CommandBlockData.getChatComponentFromNthArg(iCommandSender, stringArray, 3).getUnformattedText());
        }
        catch (NBTException nBTException) {
            throw new CommandException("commands.blockdata.tagError", nBTException.getMessage());
        }
        nBTTagCompound2.merge(nBTTagCompound);
        nBTTagCompound2.setInteger("x", blockPos.getX());
        nBTTagCompound2.setInteger("y", blockPos.getY());
        nBTTagCompound2.setInteger("z", blockPos.getZ());
        if (nBTTagCompound2.equals(nBTTagCompound3)) {
            throw new CommandException("commands.blockdata.failed", nBTTagCompound2.toString());
        }
        tileEntity.readFromNBT(nBTTagCompound2);
        tileEntity.markDirty();
        world.markBlockForUpdate(blockPos);
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
        CommandBlockData.notifyOperators(iCommandSender, (ICommand)this, "commands.blockdata.success", nBTTagCompound2.toString());
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length > 0 && stringArray.length <= 3 ? CommandBlockData.func_175771_a(stringArray, 0, blockPos) : null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.blockdata.usage";
    }
}

