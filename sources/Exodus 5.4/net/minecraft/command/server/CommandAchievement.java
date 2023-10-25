/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Lists
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
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return n == 2;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandName() {
        return "achievement";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.achievement.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (stringArray.length < 2) {
            throw new WrongUsageException("commands.achievement.usage", new Object[0]);
        }
        final StatBase statBase = StatList.getOneShotStat(stringArray[1]);
        if (statBase == null && !stringArray[1].equals("*")) {
            throw new CommandException("commands.achievement.unknownAchievement", stringArray[1]);
        }
        final EntityPlayerMP entityPlayerMP = stringArray.length >= 3 ? CommandAchievement.getPlayer(iCommandSender, stringArray[2]) : CommandAchievement.getCommandSenderAsPlayer(iCommandSender);
        boolean bl = stringArray[0].equalsIgnoreCase("give");
        boolean bl2 = stringArray[0].equalsIgnoreCase("take");
        if (bl || bl2) {
            if (statBase == null) {
                if (bl) {
                    for (Achievement achievement : AchievementList.achievementList) {
                        entityPlayerMP.triggerAchievement(achievement);
                    }
                    CommandAchievement.notifyOperators(iCommandSender, (ICommand)this, "commands.achievement.give.success.all", entityPlayerMP.getName());
                } else if (bl2) {
                    for (Achievement achievement : Lists.reverse(AchievementList.achievementList)) {
                        entityPlayerMP.func_175145_a(achievement);
                    }
                    CommandAchievement.notifyOperators(iCommandSender, (ICommand)this, "commands.achievement.take.success.all", entityPlayerMP.getName());
                }
            } else {
                if (statBase instanceof Achievement) {
                    Achievement achievement = (Achievement)statBase;
                    if (bl) {
                        if (entityPlayerMP.getStatFile().hasAchievementUnlocked(achievement)) {
                            throw new CommandException("commands.achievement.alreadyHave", entityPlayerMP.getName(), statBase.func_150955_j());
                        }
                        ArrayList arrayList = Lists.newArrayList();
                        while (achievement.parentAchievement != null && !entityPlayerMP.getStatFile().hasAchievementUnlocked(achievement.parentAchievement)) {
                            arrayList.add(achievement.parentAchievement);
                            achievement = achievement.parentAchievement;
                        }
                        for (Achievement achievement2 : Lists.reverse((List)arrayList)) {
                            entityPlayerMP.triggerAchievement(achievement2);
                        }
                    } else if (bl2) {
                        if (!entityPlayerMP.getStatFile().hasAchievementUnlocked(achievement)) {
                            throw new CommandException("commands.achievement.dontHave", entityPlayerMP.getName(), statBase.func_150955_j());
                        }
                        ArrayList arrayList = Lists.newArrayList((Iterator)Iterators.filter(AchievementList.achievementList.iterator(), (Predicate)new Predicate<Achievement>(){

                            public boolean apply(Achievement achievement) {
                                return entityPlayerMP.getStatFile().hasAchievementUnlocked(achievement) && achievement != statBase;
                            }
                        }));
                        ArrayList arrayList2 = Lists.newArrayList((Iterable)arrayList);
                        Iterator iterator = arrayList.iterator();
                        while (iterator.hasNext()) {
                            Achievement achievement3;
                            Achievement achievement4 = achievement3 = (Achievement)iterator.next();
                            boolean bl3 = false;
                            while (achievement4 != null) {
                                if (achievement4 == statBase) {
                                    bl3 = true;
                                }
                                achievement4 = achievement4.parentAchievement;
                            }
                            if (bl3) continue;
                            achievement4 = achievement3;
                            while (achievement4 != null) {
                                arrayList2.remove(achievement3);
                                achievement4 = achievement4.parentAchievement;
                            }
                        }
                        for (Achievement achievement3 : arrayList2) {
                            entityPlayerMP.func_175145_a(achievement3);
                        }
                    }
                }
                if (bl) {
                    entityPlayerMP.triggerAchievement(statBase);
                    CommandAchievement.notifyOperators(iCommandSender, (ICommand)this, "commands.achievement.give.success.one", entityPlayerMP.getName(), statBase.func_150955_j());
                } else if (bl2) {
                    entityPlayerMP.func_175145_a(statBase);
                    CommandAchievement.notifyOperators(iCommandSender, (ICommand)this, "commands.achievement.take.success.one", statBase.func_150955_j(), entityPlayerMP.getName());
                }
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        if (stringArray.length == 1) {
            return CommandAchievement.getListOfStringsMatchingLastWord(stringArray, "give", "take");
        }
        if (stringArray.length != 2) {
            return stringArray.length == 3 ? CommandAchievement.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames()) : null;
        }
        ArrayList arrayList = Lists.newArrayList();
        for (StatBase statBase : StatList.allStats) {
            arrayList.add(statBase.statId);
        }
        return CommandAchievement.getListOfStringsMatchingLastWord(stringArray, arrayList);
    }
}

