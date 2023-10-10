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
    public String getCommandName() {
        return "pardon";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.unban.usage";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        if (!MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer()) return false;
        if (!super.canCommandSenderUseCommand(sender)) return false;
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) throw new WrongUsageException("commands.unban.usage", new Object[0]);
        if (args[0].length() <= 0) throw new WrongUsageException("commands.unban.usage", new Object[0]);
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        GameProfile gameprofile = minecraftserver.getConfigurationManager().getBannedPlayers().isUsernameBanned(args[0]);
        if (gameprofile == null) {
            throw new CommandException("commands.unban.failed", args[0]);
        }
        minecraftserver.getConfigurationManager().getBannedPlayers().removeEntry(gameprofile);
        CommandPardonPlayer.notifyOperators(sender, (ICommand)this, "commands.unban.success", args[0]);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length != 1) return null;
        List<String> list = CommandPardonPlayer.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys());
        return list;
    }
}

