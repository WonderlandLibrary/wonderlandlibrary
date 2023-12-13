/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.List;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandEntityData
extends CommandBase {
    private static final String __OBFID = "CL_00002345";

    @Override
    public String getCommandName() {
        return "entitydata";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.entitydata.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        NBTTagCompound var6;
        if (args.length < 2) {
            throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
        }
        Entity var3 = CommandEntityData.func_175768_b(sender, args[0]);
        if (var3 instanceof EntityPlayer) {
            throw new CommandException("commands.entitydata.noPlayers", var3.getDisplayName());
        }
        NBTTagCompound var4 = new NBTTagCompound();
        var3.writeToNBT(var4);
        NBTTagCompound var5 = (NBTTagCompound)var4.copy();
        try {
            var6 = JsonToNBT.func_180713_a(CommandEntityData.getChatComponentFromNthArg(sender, args, 1).getUnformattedText());
        }
        catch (NBTException var8) {
            throw new CommandException("commands.entitydata.tagError", var8.getMessage());
        }
        var6.removeTag("UUIDMost");
        var6.removeTag("UUIDLeast");
        var4.merge(var6);
        if (var4.equals(var5)) {
            throw new CommandException("commands.entitydata.failed", var4.toString());
        }
        var3.readFromNBT(var4);
        CommandEntityData.notifyOperators(sender, (ICommand)this, "commands.entitydata.success", var4.toString());
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandEntityData.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}

