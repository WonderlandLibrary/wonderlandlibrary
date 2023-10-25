/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;

public class CommandPublishLocalServer
extends CommandBase {
    @Override
    public String getCommandName() {
        return "publish";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.publish.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String s = server.shareToLAN(GameType.SURVIVAL, false);
        if (s != null) {
            CommandPublishLocalServer.notifyCommandListener(sender, (ICommand)this, "commands.publish.started", s);
        } else {
            CommandPublishLocalServer.notifyCommandListener(sender, (ICommand)this, "commands.publish.failed", new Object[0]);
        }
    }
}

