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
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length == 0) {
            EntityPlayerMP entityPlayerMP = CommandKill.getCommandSenderAsPlayer(iCommandSender);
            entityPlayerMP.onKillCommand();
            CommandKill.notifyOperators(iCommandSender, (ICommand)this, "commands.kill.successful", entityPlayerMP.getDisplayName());
        } else {
            Entity entity = CommandKill.func_175768_b(iCommandSender, stringArray[0]);
            entity.onKillCommand();
            CommandKill.notifyOperators(iCommandSender, (ICommand)this, "commands.kill.successful", entity.getDisplayName());
        }
    }

    @Override
    public String getCommandName() {
        return "kill";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 1 ? CommandKill.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.kill.usage";
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 0;
    }
}

