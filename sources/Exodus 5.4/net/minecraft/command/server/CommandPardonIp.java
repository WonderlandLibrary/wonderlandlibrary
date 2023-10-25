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
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.unbanip.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length == 1 && stringArray[0].length() > 1) {
            Matcher matcher = CommandBanIp.field_147211_a.matcher(stringArray[0]);
            if (!matcher.matches()) {
                throw new SyntaxErrorException("commands.unbanip.invalid", new Object[0]);
            }
        } else {
            throw new WrongUsageException("commands.unbanip.usage", new Object[0]);
        }
        MinecraftServer.getServer().getConfigurationManager().getBannedIPs().removeEntry(stringArray[0]);
        CommandPardonIp.notifyOperators(iCommandSender, (ICommand)this, "commands.unbanip.success", stringArray[0]);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer() && super.canCommandSenderUseCommand(iCommandSender);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandPardonIp.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getKeys()) : null;
    }
}

