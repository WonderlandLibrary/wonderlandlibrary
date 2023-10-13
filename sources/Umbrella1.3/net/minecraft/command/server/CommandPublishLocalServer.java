/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSettings;

public class CommandPublishLocalServer
extends CommandBase {
    private static final String __OBFID = "CL_00000799";

    @Override
    public String getCommandName() {
        return "publish";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.publish.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String var3 = MinecraftServer.getServer().shareToLAN(WorldSettings.GameType.SURVIVAL, false);
        if (var3 != null) {
            CommandPublishLocalServer.notifyOperators(sender, (ICommand)this, "commands.publish.started", var3);
        } else {
            CommandPublishLocalServer.notifyOperators(sender, (ICommand)this, "commands.publish.failed", new Object[0]);
        }
    }
}

