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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandPardonPlayer
extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        GameProfile gameProfile;
        MinecraftServer minecraftServer;
        if (stringArray.length == 1 && stringArray[0].length() > 0) {
            minecraftServer = MinecraftServer.getServer();
            gameProfile = minecraftServer.getConfigurationManager().getBannedPlayers().isUsernameBanned(stringArray[0]);
            if (gameProfile == null) {
                throw new CommandException("commands.unban.failed", stringArray[0]);
            }
        } else {
            throw new WrongUsageException("commands.unban.usage", new Object[0]);
        }
        minecraftServer.getConfigurationManager().getBannedPlayers().removeEntry(gameProfile);
        CommandPardonPlayer.notifyOperators(iCommandSender, (ICommand)this, "commands.unban.success", stringArray[0]);
    }

    @Override
    public String getCommandName() {
        return "pardon";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        return MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer() && super.canCommandSenderUseCommand(iCommandSender);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandPardonPlayer.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys()) : null;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.unban.usage";
    }
}

