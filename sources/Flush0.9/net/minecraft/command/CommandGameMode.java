package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class CommandGameMode extends CommandBase {
    /**
     * Gets the name of the command
     */
    public String getCommandName() {
        return "gamemode";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel() {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender) {
        return "commands.gamemode.usage";
    }

    /**
     * Callback when the command is invoked
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0)
            throw new WrongUsageException("commands.gamemode.usage");
        else {
            WorldSettings.GameType gameType = getGameModeFromCommand(args[0]);
            EntityPlayer entityplayer = args.length >= 2 ? getPlayer(sender, args[1]) : getCommandSenderAsPlayer(sender);
            entityplayer.setGameType(gameType);
            entityplayer.fallDistance = 0.0F;

            if (sender.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback"))
                entityplayer.addChatMessage(new ChatComponentTranslation("gameMode.changed"));

            IChatComponent ichatcomponent = new ChatComponentTranslation("gameMode." + gameType.getName());

            if (entityplayer != sender)
                notifyOperators(sender, this, 1, "commands.gamemode.success.other", entityplayer.getName(), ichatcomponent);
            else
                notifyOperators(sender, this, 1, "commands.gamemode.success.self", ichatcomponent);
        }
    }

    /**
     * Gets the Game Mode specified in the command.
     */
    protected WorldSettings.GameType getGameModeFromCommand(String s) throws CommandException {
        return !s.equalsIgnoreCase(WorldSettings.GameType.SURVIVAL.getName()) && !s.equalsIgnoreCase("s") ?
                (!s.equalsIgnoreCase(WorldSettings.GameType.CREATIVE.getName()) && !s.equalsIgnoreCase("c") ?
                        (!s.equalsIgnoreCase(WorldSettings.GameType.ADVENTURE.getName()) && !s.equalsIgnoreCase("a") ?
                                (!s.equalsIgnoreCase(WorldSettings.GameType.SPECTATOR.getName()) && !s.equalsIgnoreCase("sp") ?
                                        WorldSettings.getGameTypeById(parseInt(s, 0, WorldSettings.GameType.values().length - 2)) :
                                        WorldSettings.GameType.SPECTATOR) : WorldSettings.GameType.ADVENTURE) : WorldSettings.GameType.CREATIVE)
                : WorldSettings.GameType.SURVIVAL;
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "survival", "creative", "adventure", "spectator") :
                (args.length == 2 ? getListOfStringsMatchingLastWord(args, getListOfPlayerUsernames()) : null);
    }

    /**
     * Returns String array containing all player usernames in the server.
     */
    protected String[] getListOfPlayerUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}