// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.server.management.UserList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.server.management.UserListBansEntry;
import java.util.Date;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandBanPlayer extends CommandBase
{
    @Override
    public String getName() {
        return "ban";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.ban.usage";
    }
    
    @Override
    public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
        return server.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(server, sender);
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1 || args[0].length() <= 0) {
            throw new WrongUsageException("commands.ban.usage", new Object[0]);
        }
        final GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[0]);
        if (gameprofile == null) {
            throw new CommandException("commands.ban.failed", new Object[] { args[0] });
        }
        String s = null;
        if (args.length >= 2) {
            s = CommandBase.getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
        }
        final UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, null, sender.getName(), null, s);
        ((UserList<K, UserListBansEntry>)server.getPlayerList().getBannedPlayers()).addEntry(userlistbansentry);
        final EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(args[0]);
        if (entityplayermp != null) {
            entityplayermp.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.banned", new Object[0]));
        }
        CommandBase.notifyCommandListener(sender, this, "commands.ban.success", args[0]);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length >= 1) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
