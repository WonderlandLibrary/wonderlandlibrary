/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandWhitelist
extends CommandBase {
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
        }
        if ("on".equals(args[0])) {
            server.getPlayerList().setWhiteListEnabled(true);
            CommandWhitelist.notifyCommandListener(sender, (ICommand)this, "commands.whitelist.enabled", new Object[0]);
        } else if ("off".equals(args[0])) {
            server.getPlayerList().setWhiteListEnabled(false);
            CommandWhitelist.notifyCommandListener(sender, (ICommand)this, "commands.whitelist.disabled", new Object[0]);
        } else if ("list".equals(args[0])) {
            sender.addChatMessage(new TextComponentTranslation("commands.whitelist.list", server.getPlayerList().getWhitelistedPlayerNames().length, server.getPlayerList().getAvailablePlayerDat().length));
            Object[] astring = server.getPlayerList().getWhitelistedPlayerNames();
            sender.addChatMessage(new TextComponentString(CommandWhitelist.joinNiceString(astring)));
        } else if ("add".equals(args[0])) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
            }
            GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[1]);
            if (gameprofile == null) {
                throw new CommandException("commands.whitelist.add.failed", args[1]);
            }
            server.getPlayerList().addWhitelistedPlayer(gameprofile);
            CommandWhitelist.notifyCommandListener(sender, (ICommand)this, "commands.whitelist.add.success", args[1]);
        } else if ("remove".equals(args[0])) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
            }
            GameProfile gameprofile1 = server.getPlayerList().getWhitelistedPlayers().getByName(args[1]);
            if (gameprofile1 == null) {
                throw new CommandException("commands.whitelist.remove.failed", args[1]);
            }
            server.getPlayerList().removePlayerFromWhitelist(gameprofile1);
            CommandWhitelist.notifyCommandListener(sender, (ICommand)this, "commands.whitelist.remove.success", args[1]);
        } else if ("reload".equals(args[0])) {
            server.getPlayerList().reloadWhitelist();
            CommandWhitelist.notifyCommandListener(sender, (ICommand)this, "commands.whitelist.reloaded", new Object[0]);
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return CommandWhitelist.getListOfStringsMatchingLastWord(args, "on", "off", "list", "add", "remove", "reload");
        }
        if (args.length == 2) {
            if ("remove".equals(args[0])) {
                return CommandWhitelist.getListOfStringsMatchingLastWord(args, server.getPlayerList().getWhitelistedPlayerNames());
            }
            if ("add".equals(args[0])) {
                return CommandWhitelist.getListOfStringsMatchingLastWord(args, server.getPlayerProfileCache().getUsernames());
            }
        }
        return Collections.emptyList();
    }
}

