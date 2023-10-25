/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBanPlayer
extends CommandBase {
    @Override
    public String getCommandName() {
        return "ban";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.ban.usage";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return server.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(server, sender);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 1 && args[0].length() > 0) {
            GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[0]);
            if (gameprofile == null) {
                throw new CommandException("commands.ban.failed", args[0]);
            }
            String s = null;
            if (args.length >= 2) {
                s = CommandBanPlayer.getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
            }
            UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, (Date)null, sender.getName(), (Date)null, s);
            server.getPlayerList().getBannedPlayers().addEntry(userlistbansentry);
            EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(args[0]);
            if (entityplayermp != null) {
                entityplayermp.connection.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.banned", new Object[0]));
            }
        } else {
            throw new WrongUsageException("commands.ban.usage", new Object[0]);
        }
        CommandBanPlayer.notifyCommandListener(sender, (ICommand)this, "commands.ban.success", args[0]);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length >= 1 ? CommandBanPlayer.getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
    }
}

