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
    public String getCommandName() {
        return "gamemode";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.gamemode.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
        }
        WorldSettings.GameType worldsettings$gametype = this.getGameModeFromCommand(sender, args[0]);
        EntityPlayerMP entityplayer = args.length >= 2 ? CommandGameMode.getPlayer(sender, args[1]) : CommandGameMode.getCommandSenderAsPlayer(sender);
        ((EntityPlayer)entityplayer).setGameType(worldsettings$gametype);
        entityplayer.fallDistance = 0.0f;
        if (sender.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback")) {
            ((Entity)entityplayer).addChatMessage(new ChatComponentTranslation("gameMode.changed", new Object[0]));
        }
        ChatComponentTranslation ichatcomponent = new ChatComponentTranslation("gameMode." + worldsettings$gametype.getName(), new Object[0]);
        if (entityplayer != sender) {
            CommandGameMode.notifyOperators(sender, (ICommand)this, 1, "commands.gamemode.success.other", entityplayer.getName(), ichatcomponent);
            return;
        }
        CommandGameMode.notifyOperators(sender, (ICommand)this, 1, "commands.gamemode.success.self", ichatcomponent);
    }

    protected WorldSettings.GameType getGameModeFromCommand(ICommandSender p_71539_1_, String p_71539_2_) throws CommandException, NumberInvalidException {
        WorldSettings.GameType gameType;
        if (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SURVIVAL.getName()) && !p_71539_2_.equalsIgnoreCase("s")) {
            if (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.CREATIVE.getName()) && !p_71539_2_.equalsIgnoreCase("c")) {
                if (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.ADVENTURE.getName()) && !p_71539_2_.equalsIgnoreCase("a")) {
                    if (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SPECTATOR.getName()) && !p_71539_2_.equalsIgnoreCase("sp")) {
                        gameType = WorldSettings.getGameTypeById(CommandGameMode.parseInt(p_71539_2_, 0, WorldSettings.GameType.values().length - 2));
                        return gameType;
                    }
                    gameType = WorldSettings.GameType.SPECTATOR;
                    return gameType;
                }
                gameType = WorldSettings.GameType.ADVENTURE;
                return gameType;
            }
            gameType = WorldSettings.GameType.CREATIVE;
            return gameType;
        }
        gameType = WorldSettings.GameType.SURVIVAL;
        return gameType;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> list;
        if (args.length == 1) {
            list = CommandGameMode.getListOfStringsMatchingLastWord(args, "survival", "creative", "adventure", "spectator");
            return list;
        }
        if (args.length != 2) return null;
        list = CommandGameMode.getListOfStringsMatchingLastWord(args, this.getListOfPlayerUsernames());
        return list;
    }

    protected String[] getListOfPlayerUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if (index != 1) return false;
        return true;
    }
}

