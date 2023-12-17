// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.server.management.UserList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandPardonPlayer extends CommandBase
{
    @Override
    public String getName() {
        return "pardon";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.unban.usage";
    }
    
    @Override
    public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
        return server.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(server, sender);
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 1 || args[0].length() <= 0) {
            throw new WrongUsageException("commands.unban.usage", new Object[0]);
        }
        final GameProfile gameprofile = server.getPlayerList().getBannedPlayers().getBannedProfile(args[0]);
        if (gameprofile == null) {
            throw new CommandException("commands.unban.failed", new Object[] { args[0] });
        }
        ((UserList<GameProfile, V>)server.getPlayerList().getBannedPlayers()).removeEntry(gameprofile);
        CommandBase.notifyCommandListener(sender, this, "commands.unban.success", args[0]);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getPlayerList().getBannedPlayers().getKeys()) : Collections.emptyList();
    }
}
