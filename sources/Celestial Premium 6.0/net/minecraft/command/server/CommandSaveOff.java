/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOff
extends CommandBase {
    @Override
    public String getCommandName() {
        return "save-off";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.save-off.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        boolean flag = false;
        for (int i = 0; i < server.worldServers.length; ++i) {
            if (server.worldServers[i] == null) continue;
            WorldServer worldserver = server.worldServers[i];
            if (worldserver.disableLevelSaving) continue;
            worldserver.disableLevelSaving = true;
            flag = true;
        }
        if (!flag) {
            throw new CommandException("commands.save-off.alreadyOff", new Object[0]);
        }
        CommandSaveOff.notifyCommandListener(sender, (ICommand)this, "commands.save.disabled", new Object[0]);
    }
}

