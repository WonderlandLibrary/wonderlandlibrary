// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.server;

import java.util.List;
import net.minecraft.util.BlockPos;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandWhitelist extends CommandBase
{
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
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.whitelist.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
        }
        final MinecraftServer var3 = MinecraftServer.getServer();
        if (args[0].equals("on")) {
            var3.getConfigurationManager().setWhiteListEnabled(true);
            CommandBase.notifyOperators(sender, this, "commands.whitelist.enabled", new Object[0]);
        }
        else if (args[0].equals("off")) {
            var3.getConfigurationManager().setWhiteListEnabled(false);
            CommandBase.notifyOperators(sender, this, "commands.whitelist.disabled", new Object[0]);
        }
        else if (args[0].equals("list")) {
            sender.addChatMessage(new ChatComponentTranslation("commands.whitelist.list", new Object[] { var3.getConfigurationManager().getWhitelistedPlayerNames().length, var3.getConfigurationManager().getAvailablePlayerDat().length }));
            final String[] var4 = var3.getConfigurationManager().getWhitelistedPlayerNames();
            sender.addChatMessage(new ChatComponentText(CommandBase.joinNiceString(var4)));
        }
        else if (args[0].equals("add")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
            }
            final GameProfile var5 = var3.getPlayerProfileCache().getGameProfileForUsername(args[1]);
            if (var5 == null) {
                throw new CommandException("commands.whitelist.add.failed", new Object[] { args[1] });
            }
            var3.getConfigurationManager().addWhitelistedPlayer(var5);
            CommandBase.notifyOperators(sender, this, "commands.whitelist.add.success", args[1]);
        }
        else if (args[0].equals("remove")) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
            }
            final GameProfile var5 = var3.getConfigurationManager().getWhitelistedPlayers().func_152706_a(args[1]);
            if (var5 == null) {
                throw new CommandException("commands.whitelist.remove.failed", new Object[] { args[1] });
            }
            var3.getConfigurationManager().removePlayerFromWhitelist(var5);
            CommandBase.notifyOperators(sender, this, "commands.whitelist.remove.success", args[1]);
        }
        else if (args[0].equals("reload")) {
            var3.getConfigurationManager().loadWhiteList();
            CommandBase.notifyOperators(sender, this, "commands.whitelist.reloaded", new Object[0]);
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "on", "off", "list", "add", "remove", "reload");
        }
        if (args.length == 2) {
            if (args[0].equals("remove")) {
                return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getConfigurationManager().getWhitelistedPlayerNames());
            }
            if (args[0].equals("add")) {
                return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getPlayerProfileCache().func_152654_a());
            }
        }
        return null;
    }
}
