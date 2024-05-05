package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOff
extends CommandBase {
    public String getCommandName() {
        return "save-off";
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.save-off.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        boolean flag = false;
        for (int i = 0; i < minecraftserver.worldServers.length; ++i) {
            if (minecraftserver.worldServers[i] == null) continue;
            WorldServer worldserver = minecraftserver.worldServers[i];
            if (worldserver.disableLevelSaving) continue;
            worldserver.disableLevelSaving = true;
            flag = true;
        }
        if (!flag) {
            throw new CommandException("commands.save-off.alreadyOff", new Object[0]);
        }
        CommandSaveOff.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.save.disabled", (Object[])new Object[0]);
    }
}
