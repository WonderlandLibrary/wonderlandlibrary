/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandServerKick
extends CommandBase {
    @Override
    public String getCommandName() {
        return "kick";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.kick.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) throw new WrongUsageException("commands.kick.usage", new Object[0]);
        if (args[0].length() <= 1) throw new WrongUsageException("commands.kick.usage", new Object[0]);
        EntityPlayerMP entityplayermp = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[0]);
        String s = "Kicked by an operator.";
        boolean flag = false;
        if (entityplayermp == null) {
            throw new PlayerNotFoundException();
        }
        if (args.length >= 2) {
            s = CommandServerKick.getChatComponentFromNthArg(sender, args, 1).getUnformattedText();
            flag = true;
        }
        entityplayermp.playerNetServerHandler.kickPlayerFromServer(s);
        if (flag) {
            CommandServerKick.notifyOperators(sender, (ICommand)this, "commands.kick.success.reason", entityplayermp.getName(), s);
            return;
        }
        CommandServerKick.notifyOperators(sender, (ICommand)this, "commands.kick.success", entityplayermp.getName());
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length < 1) return null;
        List<String> list = CommandServerKick.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        return list;
    }
}

