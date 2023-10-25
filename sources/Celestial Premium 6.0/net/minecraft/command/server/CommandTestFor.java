/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
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
import net.minecraft.util.math.BlockPos;

public class CommandTestFor
extends CommandBase {
    @Override
    public String getCommandName() {
        return "testfor";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.testfor.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        NBTTagCompound nbttagcompound1;
        if (args.length < 1) {
            throw new WrongUsageException("commands.testfor.usage", new Object[0]);
        }
        Entity entity = CommandTestFor.getEntity(server, sender, args[0]);
        NBTTagCompound nbttagcompound = null;
        if (args.length >= 2) {
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(CommandTestFor.buildString(args, 1));
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.testfor.tagError", nbtexception.getMessage());
            }
        }
        if (nbttagcompound != null && !NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1 = CommandTestFor.entityToNBT(entity), true)) {
            throw new CommandException("commands.testfor.failure", entity.getName());
        }
        CommandTestFor.notifyCommandListener(sender, (ICommand)this, "commands.testfor.success", entity.getName());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length == 1 ? CommandTestFor.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }
}

