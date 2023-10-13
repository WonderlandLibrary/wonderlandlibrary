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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class CommandWhitelist
extends CommandBase {
    private static final String __OBFID = "CL_00001186";

    @Override
    public String getCommandName() {
        return "whitelist";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.whitelist.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
        }
        MinecraftServer var3 = MinecraftServer.getServer();
        if (args[0].equals("on")) {
            var3.getConfigurationManager().setWhiteListEnabled(true);
            CommandWhitelist.notifyOperators(sender, (ICommand)this, "commands.whitelist.enabled", new Object[0]);
        } else if (args[0].equals("off")) {
            var3.getConfigurationManager().setWhiteListEnabled(false);
            CommandWhitelist.notifyOperators(sender, (ICommand)this, "commands.whitelist.disabled", new Object[0]);
        } else if (args[0].equals("list")) {
            sender.addChatMessage(new ChatComponentTranslation("commands.whitelist.list", var3.getConfigurationManager().getWhitelistedPlayerNames().length, var3.getConfigurationManager().getAvailablePlayerDat().length));
            Object[] var4 = var3.getConfigurationManager().getWhitelistedPlayerNames();
            sender.addChatMessage(new ChatComponentText(CommandWhitelist.joinNiceString(var4)));
        } else if (args[0].equals("add")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
            }
            GameProfile var5 = var3.getPlayerProfileCache().getGameProfileForUsername(args[1]);
            if (var5 == null) {
                throw new CommandException("commands.whitelist.add.failed", args[1]);
            }
            var3.getConfigurationManager().addWhitelistedPlayer(var5);
            CommandWhitelist.notifyOperators(sender, (ICommand)this, "commands.whitelist.add.success", args[1]);
        } else if (args[0].equals("remove")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
            }
            GameProfile var5 = var3.getConfigurationManager().getWhitelistedPlayers().func_152706_a(args[1]);
            if (var5 == null) {
                throw new CommandException("commands.whitelist.remove.failed", args[1]);
            }
            var3.getConfigurationManager().removePlayerFromWhitelist(var5);
            CommandWhitelist.notifyOperators(sender, (ICommand)this, "commands.whitelist.remove.success", args[1]);
        } else if (args[0].equals("reload")) {
            var3.getConfigurationManager().loadWhiteList();
            CommandWhitelist.notifyOperators(sender, (ICommand)this, "commands.whitelist.reloaded", new Object[0]);
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return CommandWhitelist.getListOfStringsMatchingLastWord(args, "on", "off", "list", "add", "remove", "reload");
        }
        if (args.length == 2) {
            if (args[0].equals("remove")) {
                return CommandWhitelist.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getWhitelistedPlayerNames());
            }
            if (args[0].equals("add")) {
                return CommandWhitelist.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getPlayerProfileCache().func_152654_a());
            }
        }
        return null;
    }
}

