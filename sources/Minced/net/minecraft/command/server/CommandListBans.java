// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.CommandBase;

public class CommandListBans extends CommandBase
{
    @Override
    public String getName() {
        return "banlist";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
        return (server.getPlayerList().getBannedIPs().isLanServer() || server.getPlayerList().getBannedPlayers().isLanServer()) && super.checkPermission(server, sender);
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.banlist.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length >= 1 && "ips".equalsIgnoreCase(args[0])) {
            sender.sendMessage(new TextComponentTranslation("commands.banlist.ips", new Object[] { server.getPlayerList().getBannedIPs().getKeys().length }));
            sender.sendMessage(new TextComponentString(CommandBase.joinNiceString(server.getPlayerList().getBannedIPs().getKeys())));
        }
        else {
            sender.sendMessage(new TextComponentTranslation("commands.banlist.players", new Object[] { server.getPlayerList().getBannedPlayers().getKeys().length }));
            sender.sendMessage(new TextComponentString(CommandBase.joinNiceString(server.getPlayerList().getBannedPlayers().getKeys())));
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, "players", "ips") : Collections.emptyList();
    }
}
