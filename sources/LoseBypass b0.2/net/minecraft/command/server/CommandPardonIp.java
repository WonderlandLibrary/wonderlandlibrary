/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import java.util.List;
import java.util.regex.Matcher;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.server.CommandBanIp;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandPardonIp
extends CommandBase {
    @Override
    public String getCommandName() {
        return "pardon-ip";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        if (!MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer()) return false;
        if (!super.canCommandSenderUseCommand(sender)) return false;
        return true;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.unbanip.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) throw new WrongUsageException("commands.unbanip.usage", new Object[0]);
        if (args[0].length() <= 1) throw new WrongUsageException("commands.unbanip.usage", new Object[0]);
        Matcher matcher = CommandBanIp.field_147211_a.matcher(args[0]);
        if (!matcher.matches()) throw new SyntaxErrorException("commands.unbanip.invalid", new Object[0]);
        MinecraftServer.getServer().getConfigurationManager().getBannedIPs().removeEntry(args[0]);
        CommandPardonIp.notifyOperators(sender, (ICommand)this, "commands.unbanip.success", args[0]);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length != 1) return null;
        List<String> list = CommandPardonIp.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys());
        return list;
    }
}

