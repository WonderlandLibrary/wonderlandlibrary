/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class CommandEntityData
extends CommandBase {
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        NBTTagCompound nBTTagCompound;
        if (stringArray.length < 2) {
            throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
        }
        Entity entity = CommandEntityData.func_175768_b(iCommandSender, stringArray[0]);
        if (entity instanceof EntityPlayer) {
            throw new CommandException("commands.entitydata.noPlayers", entity.getDisplayName());
        }
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        entity.writeToNBT(nBTTagCompound2);
        NBTTagCompound nBTTagCompound3 = (NBTTagCompound)nBTTagCompound2.copy();
        try {
            nBTTagCompound = JsonToNBT.getTagFromJson(CommandEntityData.getChatComponentFromNthArg(iCommandSender, stringArray, 1).getUnformattedText());
        }
        catch (NBTException nBTException) {
            throw new CommandException("commands.entitydata.tagError", nBTException.getMessage());
        }
        nBTTagCompound.removeTag("UUIDMost");
        nBTTagCompound.removeTag("UUIDLeast");
        nBTTagCompound2.merge(nBTTagCompound);
        if (nBTTagCompound2.equals(nBTTagCompound3)) {
            throw new CommandException("commands.entitydata.failed", nBTTagCompound2.toString());
        }
        entity.readFromNBT(nBTTagCompound2);
        CommandEntityData.notifyOperators(iCommandSender, (ICommand)this, "commands.entitydata.success", nBTTagCompound2.toString());
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }

    @Override
    public String getCommandName() {
        return "entitydata";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.entitydata.usage";
    }
}

