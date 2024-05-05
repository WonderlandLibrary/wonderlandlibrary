/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;

public class CommandAchievement
extends CommandBase {
    @Override
    public String getCommandName() {
        return "achievement";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.achievement.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.achievement.usage", new Object[0]);
        }
        final StatBase statbase = StatList.getOneShotStat(args[1]);
        if (statbase == null && !args[1].equals("*")) {
            throw new CommandException("commands.achievement.unknownAchievement", args[1]);
        }
        final EntityPlayerMP entityplayermp = args.length >= 3 ? CommandAchievement.getPlayer(sender, args[2]) : CommandAchievement.getCommandSenderAsPlayer(sender);
        boolean flag = args[0].equalsIgnoreCase("give");
        boolean flag1 = args[0].equalsIgnoreCase("take");
        if (!flag) {
            if (!flag1) return;
        }
        if (statbase == null) {
            if (flag) {
                Iterator<Achievement> iterator = AchievementList.achievementList.iterator();
                while (true) {
                    if (!iterator.hasNext()) {
                        CommandAchievement.notifyOperators(sender, (ICommand)this, "commands.achievement.give.success.all", entityplayermp.getName());
                        return;
                    }
                    Achievement achievement4 = iterator.next();
                    entityplayermp.triggerAchievement(achievement4);
                }
            }
            if (!flag1) return;
            Iterator<Achievement> iterator = Lists.reverse(AchievementList.achievementList).iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    CommandAchievement.notifyOperators(sender, (ICommand)this, "commands.achievement.take.success.all", entityplayermp.getName());
                    return;
                }
                Achievement achievement5 = iterator.next();
                entityplayermp.func_175145_a(achievement5);
            }
        }
        if (statbase instanceof Achievement) {
            Achievement achievement = (Achievement)statbase;
            if (flag) {
                if (entityplayermp.getStatFile().hasAchievementUnlocked(achievement)) {
                    throw new CommandException("commands.achievement.alreadyHave", entityplayermp.getName(), statbase.func_150955_j());
                }
                ArrayList<Achievement> list = Lists.newArrayList();
                while (achievement.parentAchievement != null && !entityplayermp.getStatFile().hasAchievementUnlocked(achievement.parentAchievement)) {
                    list.add(achievement.parentAchievement);
                    achievement = achievement.parentAchievement;
                }
                for (Achievement achievement1 : Lists.reverse(list)) {
                    entityplayermp.triggerAchievement(achievement1);
                }
            } else if (flag1) {
                if (!entityplayermp.getStatFile().hasAchievementUnlocked(achievement)) {
                    throw new CommandException("commands.achievement.dontHave", entityplayermp.getName(), statbase.func_150955_j());
                }
                ArrayList<Achievement> list1 = Lists.newArrayList(Iterators.filter(AchievementList.achievementList.iterator(), new Predicate<Achievement>(){

                    @Override
                    public boolean apply(Achievement p_apply_1_) {
                        if (!entityplayermp.getStatFile().hasAchievementUnlocked(p_apply_1_)) return false;
                        if (p_apply_1_ == statbase) return false;
                        return true;
                    }
                }));
                ArrayList<Achievement> list2 = Lists.newArrayList(list1);
                Iterator iterator = list1.iterator();
                while (iterator.hasNext()) {
                    Achievement achievement2;
                    Achievement achievement3 = achievement2 = (Achievement)iterator.next();
                    boolean flag2 = false;
                    while (achievement3 != null) {
                        if (achievement3 == statbase) {
                            flag2 = true;
                        }
                        achievement3 = achievement3.parentAchievement;
                    }
                    if (flag2) continue;
                    achievement3 = achievement2;
                    while (achievement3 != null) {
                        list2.remove(achievement2);
                        achievement3 = achievement3.parentAchievement;
                    }
                }
                for (Achievement achievement6 : list2) {
                    entityplayermp.func_175145_a(achievement6);
                }
            }
        }
        if (flag) {
            entityplayermp.triggerAchievement(statbase);
            CommandAchievement.notifyOperators(sender, (ICommand)this, "commands.achievement.give.success.one", entityplayermp.getName(), statbase.func_150955_j());
            return;
        }
        if (!flag1) return;
        entityplayermp.func_175145_a(statbase);
        CommandAchievement.notifyOperators(sender, (ICommand)this, "commands.achievement.take.success.one", statbase.func_150955_j(), entityplayermp.getName());
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return CommandAchievement.getListOfStringsMatchingLastWord(args, "give", "take");
        }
        if (args.length != 2) {
            if (args.length != 3) return null;
            List<String> list = CommandAchievement.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
            return list;
        }
        ArrayList<String> list = Lists.newArrayList();
        Iterator<StatBase> iterator = StatList.allStats.iterator();
        while (iterator.hasNext()) {
            StatBase statbase = iterator.next();
            list.add(statbase.statId);
        }
        return CommandAchievement.getListOfStringsMatchingLastWord(args, list);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if (index != 2) return false;
        return true;
    }
}

