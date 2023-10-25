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
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.kick.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length > 0 && stringArray[0].length() > 1) {
            EntityPlayerMP entityPlayerMP = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(stringArray[0]);
            String string = "Kicked by an operator.";
            boolean bl = false;
            if (entityPlayerMP == null) {
                throw new PlayerNotFoundException();
            }
            if (stringArray.length >= 2) {
                string = CommandServerKick.getChatComponentFromNthArg(iCommandSender, stringArray, 1).getUnformattedText();
                bl = true;
            }
            entityPlayerMP.playerNetServerHandler.kickPlayerFromServer(string);
            if (bl) {
                CommandServerKick.notifyOperators(iCommandSender, (ICommand)this, "commands.kick.success.reason", entityPlayerMP.getName(), string);
            } else {
                CommandServerKick.notifyOperators(iCommandSender, (ICommand)this, "commands.kick.success", entityPlayerMP.getName());
            }
        } else {
            throw new WrongUsageException("commands.kick.usage", new Object[0]);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length >= 1 ? CommandServerKick.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public String getCommandName() {
        return "kick";
    }
}

