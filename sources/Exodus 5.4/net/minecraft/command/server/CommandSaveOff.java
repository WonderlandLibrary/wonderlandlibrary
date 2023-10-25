/*
 * Decompiled with CFR 0.152.
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
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.save-off.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        MinecraftServer minecraftServer = MinecraftServer.getServer();
        boolean bl = false;
        int n = 0;
        while (n < minecraftServer.worldServers.length) {
            if (minecraftServer.worldServers[n] != null) {
                WorldServer worldServer = minecraftServer.worldServers[n];
                if (!worldServer.disableLevelSaving) {
                    worldServer.disableLevelSaving = true;
                    bl = true;
                }
            }
            ++n;
        }
        if (!bl) {
            throw new CommandException("commands.save-off.alreadyOff", new Object[0]);
        }
        CommandSaveOff.notifyOperators(iCommandSender, (ICommand)this, "commands.save.disabled", new Object[0]);
    }
}

