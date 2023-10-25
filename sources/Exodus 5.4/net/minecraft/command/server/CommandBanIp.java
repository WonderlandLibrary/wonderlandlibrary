/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.IPBanEntry;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class CommandBanIp
extends CommandBase {
    public static final Pattern field_147211_a = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length >= 1 && stringArray[0].length() > 1) {
            IChatComponent iChatComponent = stringArray.length >= 2 ? CommandBanIp.getChatComponentFromNthArg(iCommandSender, stringArray, 1) : null;
            Matcher matcher = field_147211_a.matcher(stringArray[0]);
            if (matcher.matches()) {
                this.func_147210_a(iCommandSender, stringArray[0], iChatComponent == null ? null : iChatComponent.getUnformattedText());
            } else {
                EntityPlayerMP entityPlayerMP = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(stringArray[0]);
                if (entityPlayerMP == null) {
                    throw new PlayerNotFoundException("commands.banip.invalid", new Object[0]);
                }
                this.func_147210_a(iCommandSender, entityPlayerMP.getPlayerIP(), iChatComponent == null ? null : iChatComponent.getUnformattedText());
            }
        } else {
            throw new WrongUsageException("commands.banip.usage", new Object[0]);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isLanServer() && super.canCommandSenderUseCommand(iCommandSender);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandBanIp.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.banip.usage";
    }

    @Override
    public String getCommandName() {
        return "ban-ip";
    }

    protected void func_147210_a(ICommandSender iCommandSender, String string, String string2) {
        IPBanEntry iPBanEntry = new IPBanEntry(string, null, iCommandSender.getName(), null, string2);
        MinecraftServer.getServer().getConfigurationManager().getBannedIPs().addEntry(iPBanEntry);
        List<EntityPlayerMP> list = MinecraftServer.getServer().getConfigurationManager().getPlayersMatchingAddress(string);
        Object[] objectArray = new String[list.size()];
        int n = 0;
        for (EntityPlayerMP entityPlayerMP : list) {
            entityPlayerMP.playerNetServerHandler.kickPlayerFromServer("You have been IP banned.");
            objectArray[n++] = entityPlayerMP.getName();
        }
        if (list.isEmpty()) {
            CommandBanIp.notifyOperators(iCommandSender, (ICommand)this, "commands.banip.success", string);
        } else {
            CommandBanIp.notifyOperators(iCommandSender, (ICommand)this, "commands.banip.success.players", string, CommandBanIp.joinNiceString(objectArray));
        }
    }
}

