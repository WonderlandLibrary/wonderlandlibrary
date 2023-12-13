/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000747";

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
        return MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer() && super.canCommandSenderUseCommand(sender);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        GameProfile var4;
        MinecraftServer var3;
        if (args.length == 1 && args[0].length() > 0) {
            var3 = MinecraftServer.getServer();
            var4 = var3.getConfigurationManager().getBannedPlayers().isUsernameBanned(args[0]);
            if (var4 == null) {
                throw new CommandException("commands.unban.failed", args[0]);
            }
        } else {
            throw new WrongUsageException("commands.unban.usage", new Object[0]);
        }
        var3.getConfigurationManager().getBannedPlayers().removeEntry((Object)var4);
        CommandPardonPlayer.notifyOperators(sender, (ICommand)this, "commands.unban.success", args[0]);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? CommandPardonPlayer.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys()) : null;
    }
}

