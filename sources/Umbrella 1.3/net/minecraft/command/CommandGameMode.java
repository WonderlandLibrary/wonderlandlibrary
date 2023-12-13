/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

public class CommandGameMode
extends CommandBase {
    private static final String __OBFID = "CL_00000448";

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
        WorldSettings.GameType var3 = this.getGameModeFromCommand(sender, args[0]);
        EntityPlayerMP var4 = args.length >= 2 ? CommandGameMode.getPlayer(sender, args[1]) : CommandGameMode.getCommandSenderAsPlayer(sender);
        var4.setGameType(var3);
        var4.fallDistance = 0.0f;
        if (sender.getEntityWorld().getGameRules().getGameRuleBooleanValue("sendCommandFeedback")) {
            var4.addChatMessage(new ChatComponentTranslation("gameMode.changed", new Object[0]));
        }
        ChatComponentTranslation var5 = new ChatComponentTranslation("gameMode." + var3.getName(), new Object[0]);
        if (var4 != sender) {
            CommandGameMode.notifyOperators(sender, (ICommand)this, 1, "commands.gamemode.success.other", var4.getName(), var5);
        } else {
            CommandGameMode.notifyOperators(sender, (ICommand)this, 1, "commands.gamemode.success.self", var5);
        }
    }

    protected WorldSettings.GameType getGameModeFromCommand(ICommandSender p_71539_1_, String p_71539_2_) throws CommandException {
        return !p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SURVIVAL.getName()) && !p_71539_2_.equalsIgnoreCase("s") ? (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.CREATIVE.getName()) && !p_71539_2_.equalsIgnoreCase("c") ? (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.ADVENTURE.getName()) && !p_71539_2_.equalsIgnoreCase("a") ? (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SPECTATOR.getName()) && !p_71539_2_.equalsIgnoreCase("sp") ? WorldSettings.getGameTypeById(CommandGameMode.parseInt(p_71539_2_, 0, WorldSettings.GameType.values().length - 2)) : WorldSettings.GameType.SPECTATOR) : WorldSettings.GameType.ADVENTURE) : WorldSettings.GameType.CREATIVE) : WorldSettings.GameType.SURVIVAL;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandGameMode.getListOfStringsMatchingLastWord(args, "survival", "creative", "adventure", "spectator") : (args.length == 2 ? CommandGameMode.getListOfStringsMatchingLastWord(args, this.getListOfPlayerUsernames()) : null);
    }

    protected String[] getListOfPlayerUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}

