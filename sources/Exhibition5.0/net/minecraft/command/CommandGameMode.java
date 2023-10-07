// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldSettings;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;

public class CommandGameMode extends CommandBase
{
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
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.gamemode.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
        }
        final WorldSettings.GameType var3 = this.getGameModeFromCommand(sender, args[0]);
        final EntityPlayerMP var4 = (args.length >= 2) ? CommandBase.getPlayer(sender, args[1]) : CommandBase.getCommandSenderAsPlayer(sender);
        var4.setGameType(var3);
        var4.fallDistance = 0.0f;
        if (sender.getEntityWorld().getGameRules().getGameRuleBooleanValue("sendCommandFeedback")) {
            var4.addChatMessage(new ChatComponentTranslation("gameMode.changed", new Object[0]));
        }
        final ChatComponentTranslation var5 = new ChatComponentTranslation("gameMode." + var3.getName(), new Object[0]);
        if (var4 != sender) {
            CommandBase.notifyOperators(sender, this, 1, "commands.gamemode.success.other", var4.getName(), var5);
        }
        else {
            CommandBase.notifyOperators(sender, this, 1, "commands.gamemode.success.self", var5);
        }
    }
    
    protected WorldSettings.GameType getGameModeFromCommand(final ICommandSender p_71539_1_, final String p_71539_2_) throws CommandException {
        return (!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SURVIVAL.getName()) && !p_71539_2_.equalsIgnoreCase("s")) ? ((!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.CREATIVE.getName()) && !p_71539_2_.equalsIgnoreCase("c")) ? ((!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.ADVENTURE.getName()) && !p_71539_2_.equalsIgnoreCase("a")) ? ((!p_71539_2_.equalsIgnoreCase(WorldSettings.GameType.SPECTATOR.getName()) && !p_71539_2_.equalsIgnoreCase("sp")) ? WorldSettings.getGameTypeById(CommandBase.parseInt(p_71539_2_, 0, WorldSettings.GameType.values().length - 2)) : WorldSettings.GameType.SPECTATOR) : WorldSettings.GameType.ADVENTURE) : WorldSettings.GameType.CREATIVE) : WorldSettings.GameType.SURVIVAL;
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "survival", "creative", "adventure", "spectator") : ((args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, this.getListOfPlayerUsernames()) : null);
    }
    
    protected String[] getListOfPlayerUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 1;
    }
}
