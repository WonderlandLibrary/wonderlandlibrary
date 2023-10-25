/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface ICommand
extends Comparable<ICommand> {
    public String getCommandName();

    public String getCommandUsage(ICommandSender var1);

    public List<String> getCommandAliases();

    public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException;

    public boolean checkPermission(MinecraftServer var1, ICommandSender var2);

    public List<String> getTabCompletionOptions(MinecraftServer var1, ICommandSender var2, String[] var3, BlockPos var4);

    public boolean isUsernameIndex(String[] var1, int var2);
}

