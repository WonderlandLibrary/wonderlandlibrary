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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class CommandWhitelist
extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandName() {
        return "whitelist";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 1) {
            throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
        }
        MinecraftServer minecraftServer = MinecraftServer.getServer();
        if (stringArray[0].equals("on")) {
            minecraftServer.getConfigurationManager().setWhiteListEnabled(true);
            CommandWhitelist.notifyOperators(iCommandSender, (ICommand)this, "commands.whitelist.enabled", new Object[0]);
        } else if (stringArray[0].equals("off")) {
            minecraftServer.getConfigurationManager().setWhiteListEnabled(false);
            CommandWhitelist.notifyOperators(iCommandSender, (ICommand)this, "commands.whitelist.disabled", new Object[0]);
        } else if (stringArray[0].equals("list")) {
            iCommandSender.addChatMessage(new ChatComponentTranslation("commands.whitelist.list", minecraftServer.getConfigurationManager().getWhitelistedPlayerNames().length, minecraftServer.getConfigurationManager().getAvailablePlayerDat().length));
            Object[] objectArray = minecraftServer.getConfigurationManager().getWhitelistedPlayerNames();
            iCommandSender.addChatMessage(new ChatComponentText(CommandWhitelist.joinNiceString(objectArray)));
        } else if (stringArray[0].equals("add")) {
            if (stringArray.length < 2) {
                throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
            }
            GameProfile gameProfile = minecraftServer.getPlayerProfileCache().getGameProfileForUsername(stringArray[1]);
            if (gameProfile == null) {
                throw new CommandException("commands.whitelist.add.failed", stringArray[1]);
            }
            minecraftServer.getConfigurationManager().addWhitelistedPlayer(gameProfile);
            CommandWhitelist.notifyOperators(iCommandSender, (ICommand)this, "commands.whitelist.add.success", stringArray[1]);
        } else if (stringArray[0].equals("remove")) {
            if (stringArray.length < 2) {
                throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
            }
            GameProfile gameProfile = minecraftServer.getConfigurationManager().getWhitelistedPlayers().func_152706_a(stringArray[1]);
            if (gameProfile == null) {
                throw new CommandException("commands.whitelist.remove.failed", stringArray[1]);
            }
            minecraftServer.getConfigurationManager().removePlayerFromWhitelist(gameProfile);
            CommandWhitelist.notifyOperators(iCommandSender, (ICommand)this, "commands.whitelist.remove.success", stringArray[1]);
        } else if (stringArray[0].equals("reload")) {
            minecraftServer.getConfigurationManager().loadWhiteList();
            CommandWhitelist.notifyOperators(iCommandSender, (ICommand)this, "commands.whitelist.reloaded", new Object[0]);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        if (stringArray.length == 1) {
            return CommandWhitelist.getListOfStringsMatchingLastWord(stringArray, "on", "off", "list", "add", "remove", "reload");
        }
        if (stringArray.length == 2) {
            if (stringArray[0].equals("remove")) {
                return CommandWhitelist.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getConfigurationManager().getWhitelistedPlayerNames());
            }
            if (stringArray[0].equals("add")) {
                return CommandWhitelist.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getPlayerProfileCache().getUsernames());
            }
        }
        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.whitelist.usage";
    }
}

