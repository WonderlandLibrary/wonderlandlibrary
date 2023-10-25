/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout
extends CommandBase {
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length != 1) {
            throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
        }
        int n = CommandSetPlayerTimeout.parseInt(stringArray[0], 0);
        MinecraftServer.getServer().setPlayerIdleTimeout(n);
        CommandSetPlayerTimeout.notifyOperators(iCommandSender, (ICommand)this, "commands.setidletimeout.success", n);
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.setidletimeout.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandName() {
        return "setidletimeout";
    }
}

