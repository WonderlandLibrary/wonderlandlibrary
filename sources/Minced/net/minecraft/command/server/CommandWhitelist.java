// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.command.ICommand;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandWhitelist extends CommandBase
{
    @Override
    public String getName() {
        return "whitelist";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.whitelist.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.whitelist.usage", new Object[0]);
        }
        if ("on".equals(args[0])) {
            server.getPlayerList().setWhiteListEnabled(true);
            CommandBase.notifyCommandListener(sender, this, "commands.whitelist.enabled", new Object[0]);
        }
        else if ("off".equals(args[0])) {
            server.getPlayerList().setWhiteListEnabled(false);
            CommandBase.notifyCommandListener(sender, this, "commands.whitelist.disabled", new Object[0]);
        }
        else if ("list".equals(args[0])) {
            sender.sendMessage(new TextComponentTranslation("commands.whitelist.list", new Object[] { server.getPlayerList().getWhitelistedPlayerNames().length, server.getPlayerList().getAvailablePlayerDat().length }));
            final String[] astring = server.getPlayerList().getWhitelistedPlayerNames();
            sender.sendMessage(new TextComponentString(CommandBase.joinNiceString(astring)));
        }
        else if ("add".equals(args[0])) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]);
            }
            final GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[1]);
            if (gameprofile == null) {
                throw new CommandException("commands.whitelist.add.failed", new Object[] { args[1] });
            }
            server.getPlayerList().addWhitelistedPlayer(gameprofile);
            CommandBase.notifyCommandListener(sender, this, "commands.whitelist.add.success", args[1]);
        }
        else if ("remove".equals(args[0])) {
            if (args.length < 2) {
                throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]);
            }
            final GameProfile gameprofile2 = server.getPlayerList().getWhitelistedPlayers().getByName(args[1]);
            if (gameprofile2 == null) {
                throw new CommandException("commands.whitelist.remove.failed", new Object[] { args[1] });
            }
            server.getPlayerList().removePlayerFromWhitelist(gameprofile2);
            CommandBase.notifyCommandListener(sender, this, "commands.whitelist.remove.success", args[1]);
        }
        else if ("reload".equals(args[0])) {
            server.getPlayerList().reloadWhitelist();
            CommandBase.notifyCommandListener(sender, this, "commands.whitelist.reloaded", new Object[0]);
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "on", "off", "list", "add", "remove", "reload");
        }
        if (args.length == 2) {
            if ("remove".equals(args[0])) {
                return CommandBase.getListOfStringsMatchingLastWord(args, server.getPlayerList().getWhitelistedPlayerNames());
            }
            if ("add".equals(args[0])) {
                return CommandBase.getListOfStringsMatchingLastWord(args, server.getPlayerProfileCache().getUsernames());
            }
        }
        return Collections.emptyList();
    }
}
