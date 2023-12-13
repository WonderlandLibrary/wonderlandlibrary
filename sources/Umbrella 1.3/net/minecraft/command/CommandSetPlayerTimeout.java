/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000999";

    @Override
    public String getCommandName() {
        return "setidletimeout";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.setidletimeout.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
        }
        int var3 = CommandSetPlayerTimeout.parseInt(args[0], 0);
        MinecraftServer.getServer().setPlayerIdleTimeout(var3);
        CommandSetPlayerTimeout.notifyOperators(sender, (ICommand)this, "commands.setidletimeout.success", var3);
    }
}

