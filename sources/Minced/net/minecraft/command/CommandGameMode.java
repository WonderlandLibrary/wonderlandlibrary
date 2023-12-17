// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.GameType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.server.MinecraftServer;

public class CommandGameMode extends CommandBase
{
    @Override
    public String getName() {
        return "gamemode";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.gamemode.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length <= 0) {
            throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
        }
        final GameType gametype = this.getGameModeFromCommand(sender, args[0]);
        final EntityPlayer entityplayer = (args.length >= 2) ? CommandBase.getPlayer(server, sender, args[1]) : CommandBase.getCommandSenderAsPlayer(sender);
        entityplayer.setGameType(gametype);
        final ITextComponent itextcomponent = new TextComponentTranslation("gameMode." + gametype.getName(), new Object[0]);
        if (sender.getEntityWorld().getGameRules().getBoolean("sendCommandFeedback")) {
            entityplayer.sendMessage(new TextComponentTranslation("gameMode.changed", new Object[] { itextcomponent }));
        }
        if (entityplayer == sender) {
            CommandBase.notifyCommandListener(sender, this, 1, "commands.gamemode.success.self", itextcomponent);
        }
        else {
            CommandBase.notifyCommandListener(sender, this, 1, "commands.gamemode.success.other", entityplayer.getName(), itextcomponent);
        }
    }
    
    protected GameType getGameModeFromCommand(final ICommandSender sender, final String gameModeString) throws CommandException, NumberInvalidException {
        final GameType gametype = GameType.parseGameTypeWithDefault(gameModeString, GameType.NOT_SET);
        return (gametype == GameType.NOT_SET) ? WorldSettings.getGameTypeById(CommandBase.parseInt(gameModeString, 0, GameType.values().length - 2)) : gametype;
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "survival", "creative", "adventure", "spectator");
        }
        return (args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 1;
    }
}
