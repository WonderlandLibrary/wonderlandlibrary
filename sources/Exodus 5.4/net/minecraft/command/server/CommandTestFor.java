/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandTestFor
extends CommandBase {
    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.testfor.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 1) {
            throw new WrongUsageException("commands.testfor.usage", new Object[0]);
        }
        Entity entity = CommandTestFor.func_175768_b(iCommandSender, stringArray[0]);
        NBTTagCompound nBTTagCompound = null;
        if (stringArray.length >= 2) {
            try {
                nBTTagCompound = JsonToNBT.getTagFromJson(CommandTestFor.buildString(stringArray, 1));
            }
            catch (NBTException nBTException) {
                throw new CommandException("commands.testfor.tagError", nBTException.getMessage());
            }
        }
        if (nBTTagCompound != null) {
            NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
            entity.writeToNBT(nBTTagCompound2);
            if (!NBTUtil.func_181123_a(nBTTagCompound, nBTTagCompound2, true)) {
                throw new CommandException("commands.testfor.failure", entity.getName());
            }
        }
        CommandTestFor.notifyOperators(iCommandSender, (ICommand)this, "commands.testfor.success", entity.getName());
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandTestFor.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }

    @Override
    public String getCommandName() {
        return "testfor";
    }
}

