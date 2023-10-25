/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
        }
        GameType gametype = this.getGameModeFromCommand(sender, args[0]);
        EntityPlayerMP entityplayer = args.length >= 2 ? CommandGameMode.getPlayer(server, sender, args[1]) : CommandGameMode.getCommandSenderAsPlayer(sender);
        ((EntityPlayer)entityplayer).setGameType(gametype);
        TextComponentTranslation itextcomponent = new TextComponentTranslation("gameMode." + gametype.getName(), new Object[0]);
        if (sender.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback")) {
            ((Entity)entityplayer).addChatMessage(new TextComponentTranslation("gameMode.changed", itextcomponent));
        }
        if (entityplayer == sender) {
            CommandGameMode.notifyCommandListener(sender, (ICommand)this, 1, "commands.gamemode.success.self", itextcomponent);
        } else {
            CommandGameMode.notifyCommandListener(sender, (ICommand)this, 1, "commands.gamemode.success.other", entityplayer.getName(), itextcomponent);
        }
    }

    protected GameType getGameModeFromCommand(ICommandSender sender, String gameModeString) throws CommandException, NumberInvalidException {
        GameType gametype = GameType.parseGameTypeWithDefault(gameModeString, GameType.NOT_SET);
        return gametype == GameType.NOT_SET ? WorldSettings.getGameTypeById(CommandGameMode.parseInt(gameModeString, 0, GameType.values().length - 2)) : gametype;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandGameMode.getListOfStringsMatchingLastWord(args, "survival", "creative", "adventure", "spectator");
        }
        return args.length == 2 ? CommandGameMode.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}

