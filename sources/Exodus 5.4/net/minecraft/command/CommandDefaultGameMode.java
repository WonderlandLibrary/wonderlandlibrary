/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandGameMode;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

public class CommandDefaultGameMode
extends CommandGameMode {
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length <= 0) {
            throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
        }
        WorldSettings.GameType gameType = this.getGameModeFromCommand(iCommandSender, stringArray[0]);
        this.setGameType(gameType);
        CommandDefaultGameMode.notifyOperators(iCommandSender, (ICommand)this, "commands.defaultgamemode.success", new ChatComponentTranslation("gameMode." + gameType.getName(), new Object[0]));
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.defaultgamemode.usage";
    }

    @Override
    public String getCommandName() {
        return "defaultgamemode";
    }

    protected void setGameType(WorldSettings.GameType gameType) {
        MinecraftServer minecraftServer = MinecraftServer.getServer();
        minecraftServer.setGameType(gameType);
        if (minecraftServer.getForceGamemode()) {
            for (EntityPlayerMP entityPlayerMP : MinecraftServer.getServer().getConfigurationManager().func_181057_v()) {
                entityPlayerMP.setGameType(gameType);
                entityPlayerMP.fallDistance = 0.0f;
            }
        }
    }
}

