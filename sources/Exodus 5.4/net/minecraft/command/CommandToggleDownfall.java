/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandToggleDownfall
extends CommandBase {
    @Override
    public String getCommandName() {
        return "toggledownfall";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.downfall.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        this.toggleDownfall();
        CommandToggleDownfall.notifyOperators(iCommandSender, (ICommand)this, "commands.downfall.success", new Object[0]);
    }

    protected void toggleDownfall() {
        WorldInfo worldInfo;
        worldInfo.setRaining(!(worldInfo = MinecraftServer.getServer().worldServers[0].getWorldInfo()).isRaining());
    }
}

