package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class CommandGameMode
extends CommandBase {
    public String getCommandName() {
        return "gamemode";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.gamemode.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
        }
        WorldSettings.GameType worldsettings$gametype = this.getGameModeFromCommand(sender, args[0]);
        EntityPlayerMP entityplayer = args.length >= 2 ? CommandGameMode.getPlayer((ICommandSender)sender, (String)args[1]) : CommandGameMode.getCommandSenderAsPlayer((ICommandSender)sender);
        entityplayer.setGameType(worldsettings$gametype);
        entityplayer.fallDistance = 0.0f;
        if (sender.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback")) {
            entityplayer.addChatMessage((IChatComponent)new ChatComponentTranslation("gameMode.changed", new Object[0]));
        }
        ChatComponentTranslation ichatcomponent = new ChatComponentTranslation("gameMode." + worldsettings$gametype.getName(), new Object[0]);
        if (entityplayer != sender) {
            CommandGameMode.notifyOperators((ICommandSender)sender, (ICommand)this, (int)1, (String)"commands.gamemode.success.other", (Object[])new Object[]{entityplayer.getName(), ichatcomponent});
        } else {
            CommandGameMode.notifyOperators((ICommandSender)sender, (ICommand)this, (int)1, (String)"commands.gamemode.success.self", (Object[])new Object[]{ichatcomponent});
        }
    }

    protected WorldSettings.GameType getGameModeFromCommand(ICommandSender p_71539_1_, String p_71539_2_) throws CommandException, NumberInvalidException {
        return !p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SURVIVAL.getName()) && !p_71539_2_.equalsIgnoreCase("s") ? (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.CREATIVE.getName()) && !p_71539_2_.equalsIgnoreCase("c") ? (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.ADVENTURE.getName()) && !p_71539_2_.equalsIgnoreCase("a") ? (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SPECTATOR.getName()) && !p_71539_2_.equalsIgnoreCase("sp") ? WorldSettings.getGameTypeById((int)CommandGameMode.parseInt((String)p_71539_2_, (int)0, (int)(WorldSettings.GameType.values().length - 2))) : WorldSettings.GameType.SPECTATOR) : WorldSettings.GameType.ADVENTURE) : WorldSettings.GameType.CREATIVE) : WorldSettings.GameType.SURVIVAL;
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandGameMode.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"survival", "creative", "adventure", "spectator"}) : (args.length == 2 ? CommandGameMode.getListOfStringsMatchingLastWord((String[])args, (String[])this.getListOfPlayerUsernames()) : null);
    }

    protected String[] getListOfPlayerUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}
