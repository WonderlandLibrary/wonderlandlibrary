/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandGameMode;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class CommandDefaultGameMode
extends CommandGameMode {
    @Override
    public String getCommandName() {
        return "defaultgamemode";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.defaultgamemode.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
        }
        GameType gametype = this.getGameModeFromCommand(sender, args[0]);
        this.setDefaultGameType(gametype, server);
        CommandDefaultGameMode.notifyCommandListener(sender, (ICommand)this, "commands.defaultgamemode.success", new TextComponentTranslation("gameMode." + gametype.getName(), new Object[0]));
    }

    protected void setDefaultGameType(GameType gameType, MinecraftServer server) {
        server.setGameType(gameType);
        if (server.getForceGamemode()) {
            for (EntityPlayerMP entityplayermp : server.getPlayerList().getPlayerList()) {
                entityplayermp.setGameType(gameType);
            }
        }
    }
}

