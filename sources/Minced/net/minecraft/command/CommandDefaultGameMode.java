// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.GameType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.server.MinecraftServer;

public class CommandDefaultGameMode extends CommandGameMode
{
    @Override
    public String getName() {
        return "defaultgamemode";
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.defaultgamemode.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
        }
        final GameType gametype = this.getGameModeFromCommand(sender, args[0]);
        this.setDefaultGameType(gametype, server);
        CommandBase.notifyCommandListener(sender, this, "commands.defaultgamemode.success", new TextComponentTranslation("gameMode." + gametype.getName(), new Object[0]));
    }
    
    protected void setDefaultGameType(final GameType gameType, final MinecraftServer server) {
        server.setGameType(gameType);
        if (server.getForceGamemode()) {
            for (final EntityPlayerMP entityplayermp : server.getPlayerList().getPlayers()) {
                entityplayermp.setGameType(gameType);
            }
        }
    }
}
