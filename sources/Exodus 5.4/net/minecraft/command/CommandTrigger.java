/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandTrigger
extends CommandBase {
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        Object object;
        EntityPlayerMP entityPlayerMP;
        if (stringArray.length < 3) {
            throw new WrongUsageException("commands.trigger.usage", new Object[0]);
        }
        if (iCommandSender instanceof EntityPlayerMP) {
            entityPlayerMP = (EntityPlayerMP)iCommandSender;
        } else {
            object = iCommandSender.getCommandSenderEntity();
            if (!(object instanceof EntityPlayerMP)) {
                throw new CommandException("commands.trigger.invalidPlayer", new Object[0]);
            }
            entityPlayerMP = (EntityPlayerMP)object;
        }
        object = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
        ScoreObjective scoreObjective = ((Scoreboard)object).getObjective(stringArray[0]);
        if (scoreObjective != null && scoreObjective.getCriteria() == IScoreObjectiveCriteria.TRIGGER) {
            int n = CommandTrigger.parseInt(stringArray[2]);
            if (!((Scoreboard)object).entityHasObjective(entityPlayerMP.getName(), scoreObjective)) {
                throw new CommandException("commands.trigger.invalidObjective", stringArray[0]);
            }
            Score score = ((Scoreboard)object).getValueFromObjective(entityPlayerMP.getName(), scoreObjective);
            if (score.isLocked()) {
                throw new CommandException("commands.trigger.disabled", stringArray[0]);
            }
            if ("set".equals(stringArray[1])) {
                score.setScorePoints(n);
            } else {
                if (!"add".equals(stringArray[1])) {
                    throw new CommandException("commands.trigger.invalidMode", stringArray[1]);
                }
                score.increseScore(n);
            }
            score.setLocked(true);
            if (entityPlayerMP.theItemInWorldManager.isCreative()) {
                CommandTrigger.notifyOperators(iCommandSender, (ICommand)this, "commands.trigger.success", stringArray[0], stringArray[1], stringArray[2]);
            }
        } else {
            throw new CommandException("commands.trigger.invalidObjective", stringArray[0]);
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.trigger.usage";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        if (stringArray.length == 1) {
            Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
            ArrayList arrayList = Lists.newArrayList();
            for (ScoreObjective scoreObjective : scoreboard.getScoreObjectives()) {
                if (scoreObjective.getCriteria() != IScoreObjectiveCriteria.TRIGGER) continue;
                arrayList.add(scoreObjective.getName());
            }
            return CommandTrigger.getListOfStringsMatchingLastWord(stringArray, arrayList.toArray(new String[arrayList.size()]));
        }
        return stringArray.length == 2 ? CommandTrigger.getListOfStringsMatchingLastWord(stringArray, "add", "set") : null;
    }

    @Override
    public String getCommandName() {
        return "trigger";
    }
}

