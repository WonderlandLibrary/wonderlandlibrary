package net.minecraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout
extends CommandBase {
    public String getCommandName() {
        return "setidletimeout";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.setidletimeout.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
        }
        int i = CommandSetPlayerTimeout.parseInt((String)args[0], (int)0);
        MinecraftServer.getServer().setPlayerIdleTimeout(i);
        CommandSetPlayerTimeout.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.setidletimeout.success", (Object[])new Object[]{i});
    }
}
