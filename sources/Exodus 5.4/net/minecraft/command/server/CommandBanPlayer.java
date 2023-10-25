/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.BlockPos;

public class CommandBanPlayer
extends CommandBase {
    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.ban.usage";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        return MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer() && super.canCommandSenderUseCommand(iCommandSender);
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length >= 1 && stringArray[0].length() > 0) {
            MinecraftServer minecraftServer = MinecraftServer.getServer();
            GameProfile gameProfile = minecraftServer.getPlayerProfileCache().getGameProfileForUsername(stringArray[0]);
            if (gameProfile == null) {
                throw new CommandException("commands.ban.failed", stringArray[0]);
            }
            String string = null;
            if (stringArray.length >= 2) {
                string = CommandBanPlayer.getChatComponentFromNthArg(iCommandSender, stringArray, 1).getUnformattedText();
            }
            UserListBansEntry userListBansEntry = new UserListBansEntry(gameProfile, null, iCommandSender.getName(), null, string);
            minecraftServer.getConfigurationManager().getBannedPlayers().addEntry(userListBansEntry);
            EntityPlayerMP entityPlayerMP = minecraftServer.getConfigurationManager().getPlayerByUsername(stringArray[0]);
            if (entityPlayerMP != null) {
                entityPlayerMP.playerNetServerHandler.kickPlayerFromServer("You are banned from this server.");
            }
        } else {
            throw new WrongUsageException("commands.ban.usage", new Object[0]);
        }
        CommandBanPlayer.notifyOperators(iCommandSender, (ICommand)this, "commands.ban.success", stringArray[0]);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length >= 1 ? CommandBanPlayer.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public String getCommandName() {
        return "ban";
    }
}

