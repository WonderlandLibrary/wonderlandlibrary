// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.util.ChatComponentTranslation;

public class CommandDefaultGameMode extends CommandGameMode
{
    @Override
    public String getCommandName() {
        return "defaultgamemode";
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.defaultgamemode.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
        }
        final WorldSettings.GameType worldsettings$gametype = this.getGameModeFromCommand(sender, args[0]);
        this.setGameType(worldsettings$gametype);
        CommandBase.notifyOperators(sender, this, "commands.defaultgamemode.success", new ChatComponentTranslation("gameMode." + worldsettings$gametype.getName(), new Object[0]));
    }
    
    protected void setGameType(final WorldSettings.GameType gameMode) {
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        minecraftserver.setGameType(gameMode);
        if (minecraftserver.getForceGamemode()) {
            for (final EntityPlayerMP entityplayermp : MinecraftServer.getServer().getConfigurationManager().getPlayerList()) {
                entityplayermp.setGameType(gameMode);
                entityplayermp.fallDistance = 0.0f;
            }
        }
    }
}
