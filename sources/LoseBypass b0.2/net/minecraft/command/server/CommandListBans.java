/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

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
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        if (!MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer()) {
            if (!MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isLanServer()) return false;
        }
        if (!super.canCommandSenderUseCommand(sender)) return false;
        return true;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.banlist.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 1 && args[0].equalsIgnoreCase("ips")) {
            sender.addChatMessage(new ChatComponentTranslation("commands.banlist.ips", MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys().length));
            sender.addChatMessage(new ChatComponentText(CommandListBans.joinNiceString(MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys())));
            return;
        }
        sender.addChatMessage(new ChatComponentTranslation("commands.banlist.players", MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys().length));
        sender.addChatMessage(new ChatComponentText(CommandListBans.joinNiceString(MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getKeys())));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length != 1) return null;
        List<String> list = CommandListBans.getListOfStringsMatchingLastWord(args, "players", "ips");
        return list;
    }
}

