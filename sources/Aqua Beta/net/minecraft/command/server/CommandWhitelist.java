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
import net.minecraft.util.IChatComponent;

public class CommandWhitelist
extends CommandBase {
    public String getCommandName() {
        return "whitelist";
    }

    public int getRequiredPermissionLevel() {
        return 3;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.whitelist.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
        }
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        if (args[0].equals((Object)"on")) {
            minecraftserver.getConfigurationManager().setWhiteListEnabled(true);
            CommandWhitelist.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.whitelist.enabled", (Object[])new Object[0]);
        } else if (args[0].equals((Object)"off")) {
            minecraftserver.getConfigurationManager().setWhiteListEnabled(false);
            CommandWhitelist.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.whitelist.disabled", (Object[])new Object[0]);
        } else if (args[0].equals((Object)"list")) {
            sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.whitelist.list", new Object[]{minecraftserver.getConfigurationManager().getWhitelistedPlayerNames().length, minecraftserver.getConfigurationManager().getAvailablePlayerDat().length}));
            Object[] astring = minecraftserver.getConfigurationManager().getWhitelistedPlayerNames();
            sender.addChatMessage((IChatComponent)new ChatComponentText(CommandWhitelist.joinNiceString((Object[])astring)));
        } else if (args[0].equals((Object)"add")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
            }
            GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(args[1]);
            if (gameprofile == null) {
                throw new CommandException("commands.whitelist.add.failed", new Object[]{args[1]});
            }
            minecraftserver.getConfigurationManager().addWhitelistedPlayer(gameprofile);
            CommandWhitelist.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.whitelist.add.success", (Object[])new Object[]{args[1]});
        } else if (args[0].equals((Object)"remove")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
            }
            GameProfile gameprofile1 = minecraftserver.getConfigurationManager().getWhitelistedPlayers().getBannedProfile(args[1]);
            if (gameprofile1 == null) {
                throw new CommandException("commands.whitelist.remove.failed", new Object[]{args[1]});
            }
            minecraftserver.getConfigurationManager().removePlayerFromWhitelist(gameprofile1);
            CommandWhitelist.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.whitelist.remove.success", (Object[])new Object[]{args[1]});
        } else if (args[0].equals((Object)"reload")) {
            minecraftserver.getConfigurationManager().loadWhiteList();
            CommandWhitelist.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.whitelist.reloaded", (Object[])new Object[0]);
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return CommandWhitelist.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"on", "off", "list", "add", "remove", "reload"});
        }
        if (args.length == 2) {
            if (args[0].equals((Object)"remove")) {
                return CommandWhitelist.getListOfStringsMatchingLastWord((String[])args, (String[])MinecraftServer.getServer().getConfigurationManager().getWhitelistedPlayerNames());
            }
            if (args[0].equals((Object)"add")) {
                return CommandWhitelist.getListOfStringsMatchingLastWord((String[])args, (String[])MinecraftServer.getServer().getPlayerProfileCache().getUsernames());
            }
        }
        return null;
    }
}
