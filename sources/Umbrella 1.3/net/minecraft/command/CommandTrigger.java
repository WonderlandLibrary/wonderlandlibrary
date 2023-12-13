/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandTrigger
extends CommandBase {
    private static final String __OBFID = "CL_00002337";

    @Override
    public String getCommandName() {
        return "trigger";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.trigger.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP var3;
        if (args.length < 3) {
            throw new WrongUsageException("commands.trigger.usage", new Object[0]);
        }
        if (sender instanceof EntityPlayerMP) {
            var3 = (EntityPlayerMP)sender;
        } else {
            Entity var4 = sender.getCommandSenderEntity();
            if (!(var4 instanceof EntityPlayerMP)) {
                throw new CommandException("commands.trigger.invalidPlayer", new Object[0]);
            }
            var3 = (EntityPlayerMP)var4;
        }
        Scoreboard var8 = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
        ScoreObjective var5 = var8.getObjective(args[0]);
        if (var5 != null && var5.getCriteria() == IScoreObjectiveCriteria.field_178791_c) {
            int var6 = CommandTrigger.parseInt(args[2]);
            if (!var8.func_178819_b(var3.getName(), var5)) {
                throw new CommandException("commands.trigger.invalidObjective", args[0]);
            }
            Score var7 = var8.getValueFromObjective(var3.getName(), var5);
            if (var7.func_178816_g()) {
                throw new CommandException("commands.trigger.disabled", args[0]);
            }
            if ("set".equals(args[1])) {
                var7.setScorePoints(var6);
            } else {
                if (!"add".equals(args[1])) {
                    throw new CommandException("commands.trigger.invalidMode", args[1]);
                }
                var7.increseScore(var6);
            }
            var7.func_178815_a(true);
            if (var3.theItemInWorldManager.isCreative()) {
                CommandTrigger.notifyOperators(sender, (ICommand)this, "commands.trigger.success", args[0], args[1], args[2]);
            }
        } else {
            throw new CommandException("commands.trigger.invalidObjective", args[0]);
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            Scoreboard var4 = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
            ArrayList var5 = Lists.newArrayList();
            for (ScoreObjective var7 : var4.getScoreObjectives()) {
                if (var7.getCriteria() != IScoreObjectiveCriteria.field_178791_c) continue;
                var5.add(var7.getName());
            }
            return CommandTrigger.getListOfStringsMatchingLastWord(args, var5.toArray(new String[var5.size()]));
        }
        return args.length == 2 ? CommandTrigger.getListOfStringsMatchingLastWord(args, "add", "set") : null;
    }
}

