/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandKill
extends CommandBase {
    @Override
    public String getCommandName() {
        return "kill";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.kill.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            EntityPlayerMP entityplayer = CommandKill.getCommandSenderAsPlayer(sender);
            entityplayer.onKillCommand();
            CommandKill.notifyOperators(sender, (ICommand)this, "commands.kill.successful", entityplayer.getDisplayName());
            return;
        }
        Entity entity = CommandKill.func_175768_b(sender, args[0]);
        entity.onKillCommand();
        CommandKill.notifyOperators(sender, (ICommand)this, "commands.kill.successful", entity.getDisplayName());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if (index != 0) return false;
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length != 1) return null;
        List<String> list = CommandKill.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        return list;
    }
}

