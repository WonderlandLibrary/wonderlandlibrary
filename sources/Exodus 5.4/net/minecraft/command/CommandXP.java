/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandXP
extends CommandBase {
    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 1;
    }

    protected String[] getAllUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public String getCommandName() {
        return "xp";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.xp.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        return stringArray.length == 2 ? CommandXP.getListOfStringsMatchingLastWord(stringArray, this.getAllUsernames()) : null;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        EntityPlayerMP entityPlayerMP;
        int n;
        boolean bl;
        boolean bl2;
        if (stringArray.length <= 0) {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
        }
        String string = stringArray[0];
        boolean bl3 = bl2 = string.endsWith("l") || string.endsWith("L");
        if (bl2 && string.length() > 1) {
            string = string.substring(0, string.length() - 1);
        }
        boolean bl4 = bl = (n = CommandXP.parseInt(string)) < 0;
        if (bl) {
            n *= -1;
        }
        EntityPlayerMP entityPlayerMP2 = entityPlayerMP = stringArray.length > 1 ? CommandXP.getPlayer(iCommandSender, stringArray[1]) : CommandXP.getCommandSenderAsPlayer(iCommandSender);
        if (bl2) {
            iCommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityPlayerMP.experienceLevel);
            if (bl) {
                ((EntityPlayer)entityPlayerMP).addExperienceLevel(-n);
                CommandXP.notifyOperators(iCommandSender, (ICommand)this, "commands.xp.success.negative.levels", n, entityPlayerMP.getName());
            } else {
                ((EntityPlayer)entityPlayerMP).addExperienceLevel(n);
                CommandXP.notifyOperators(iCommandSender, (ICommand)this, "commands.xp.success.levels", n, entityPlayerMP.getName());
            }
        } else {
            iCommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, entityPlayerMP.experienceTotal);
            if (bl) {
                throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]);
            }
            entityPlayerMP.addExperience(n);
            CommandXP.notifyOperators(iCommandSender, (ICommand)this, "commands.xp.success", n, entityPlayerMP.getName());
        }
    }
}

