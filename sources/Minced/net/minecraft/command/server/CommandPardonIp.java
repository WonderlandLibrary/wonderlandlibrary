// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import net.minecraft.server.management.UserList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.CommandException;
import java.util.regex.Matcher;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.ICommand;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.CommandBase;

public class CommandPardonIp extends CommandBase
{
    @Override
    public String getName() {
        return "pardon-ip";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
        return server.getPlayerList().getBannedIPs().isLanServer() && super.checkPermission(server, sender);
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.unbanip.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length != 1 || args[0].length() <= 1) {
            throw new WrongUsageException("commands.unbanip.usage", new Object[0]);
        }
        final Matcher matcher = CommandBanIp.IP_PATTERN.matcher(args[0]);
        if (matcher.matches()) {
            ((UserList<String, V>)server.getPlayerList().getBannedIPs()).removeEntry(args[0]);
            CommandBase.notifyCommandListener(sender, this, "commands.unbanip.success", args[0]);
            return;
        }
        throw new SyntaxErrorException("commands.unbanip.invalid", new Object[0]);
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, server.getPlayerList().getBannedIPs().getKeys()) : Collections.emptyList();
    }
}
