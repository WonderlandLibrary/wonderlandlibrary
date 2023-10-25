/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

public class CommandGameMode
extends CommandBase {
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length <= 0) {
            throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
        }
        WorldSettings.GameType gameType = this.getGameModeFromCommand(iCommandSender, stringArray[0]);
        EntityPlayerMP entityPlayerMP = stringArray.length >= 2 ? CommandGameMode.getPlayer(iCommandSender, stringArray[1]) : CommandGameMode.getCommandSenderAsPlayer(iCommandSender);
        ((EntityPlayer)entityPlayerMP).setGameType(gameType);
        entityPlayerMP.fallDistance = 0.0f;
        if (iCommandSender.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback")) {
            ((Entity)entityPlayerMP).addChatMessage(new ChatComponentTranslation("gameMode.changed", new Object[0]));
        }
        ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("gameMode." + gameType.getName(), new Object[0]);
        if (entityPlayerMP != iCommandSender) {
            CommandGameMode.notifyOperators(iCommandSender, (ICommand)this, 1, "commands.gamemode.success.other", entityPlayerMP.getName(), chatComponentTranslation);
        } else {
            CommandGameMode.notifyOperators(iCommandSender, (ICommand)this, 1, "commands.gamemode.success.self", chatComponentTranslation);
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 1;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandGameMode.getListOfStringsMatchingLastWord(stringArray, "survival", "creative", "adventure", "spectator") : (stringArray.length == 2 ? CommandGameMode.getListOfStringsMatchingLastWord(stringArray, this.getListOfPlayerUsernames()) : null);
    }

    protected WorldSettings.GameType getGameModeFromCommand(ICommandSender iCommandSender, String string) throws NumberInvalidException, CommandException {
        return !string.equalsIgnoreCase(WorldSettings.GameType.SURVIVAL.getName()) && !string.equalsIgnoreCase("s") ? (!string.equalsIgnoreCase(WorldSettings.GameType.CREATIVE.getName()) && !string.equalsIgnoreCase("c") ? (!string.equalsIgnoreCase(WorldSettings.GameType.ADVENTURE.getName()) && !string.equalsIgnoreCase("a") ? (!string.equalsIgnoreCase(WorldSettings.GameType.SPECTATOR.getName()) && !string.equalsIgnoreCase("sp") ? WorldSettings.getGameTypeById(CommandGameMode.parseInt(string, 0, WorldSettings.GameType.values().length - 2)) : WorldSettings.GameType.SPECTATOR) : WorldSettings.GameType.ADVENTURE) : WorldSettings.GameType.CREATIVE) : WorldSettings.GameType.SURVIVAL;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.gamemode.usage";
    }

    protected String[] getListOfPlayerUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public String getCommandName() {
        return "gamemode";
    }
}

