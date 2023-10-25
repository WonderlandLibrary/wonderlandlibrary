/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandListBans
extends CommandBase {
    @Override
    public String getCommandName() {
        return "banlist";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return (server.getPlayerList().getBannedIPs().isLanServer() || server.getPlayerList().getBannedPlayers().isLanServer()) && super.checkPermission(server, sender);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.banlist.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 1 && "ips".equalsIgnoreCase(args[0])) {
            sender.addChatMessage(new TextComponentTranslation("commands.banlist.ips", server.getPlayerList().getBannedIPs().getKeys().length));
            sender.addChatMessage(new TextComponentString(CommandListBans.joinNiceString(server.getPlayerList().getBannedIPs().getKeys())));
        } else {
            sender.addChatMessage(new TextComponentTranslation("commands.banlist.players", server.getPlayerList().getBannedPlayers().getKeys().length));
            sender.addChatMessage(new TextComponentString(CommandListBans.joinNiceString(server.getPlayerList().getBannedPlayers().getKeys())));
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length == 1 ? CommandListBans.getListOfStringsMatchingLastWord(args, "players", "ips") : Collections.emptyList();
    }
}

