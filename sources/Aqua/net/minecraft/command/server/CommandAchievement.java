package net.minecraft.command.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
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
    public String getCommandName() {
        return "achievement";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.achievement.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.achievement.usage", new Object[0]);
        }
        StatBase statbase = StatList.getOneShotStat((String)args[1]);
        if (statbase == null && !args[1].equals((Object)"*")) {
            throw new CommandException("commands.achievement.unknownAchievement", new Object[]{args[1]});
        }
        EntityPlayerMP entityplayermp = args.length >= 3 ? CommandAchievement.getPlayer((ICommandSender)sender, (String)args[2]) : CommandAchievement.getCommandSenderAsPlayer((ICommandSender)sender);
        boolean flag = args[0].equalsIgnoreCase("give");
        boolean flag1 = args[0].equalsIgnoreCase("take");
        if (flag || flag1) {
            if (statbase == null) {
                if (flag) {
                    for (Achievement achievement4 : AchievementList.achievementList) {
                        entityplayermp.triggerAchievement((StatBase)achievement4);
                    }
                    CommandAchievement.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.achievement.give.success.all", (Object[])new Object[]{entityplayermp.getName()});
                } else if (flag1) {
                    for (Achievement achievement5 : Lists.reverse((List)AchievementList.achievementList)) {
                        entityplayermp.func_175145_a((StatBase)achievement5);
                    }
                    CommandAchievement.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.achievement.take.success.all", (Object[])new Object[]{entityplayermp.getName()});
                }
            } else {
                if (statbase instanceof Achievement) {
                    Achievement achievement = (Achievement)statbase;
                    if (flag) {
                        if (entityplayermp.getStatFile().hasAchievementUnlocked(achievement)) {
                            throw new CommandException("commands.achievement.alreadyHave", new Object[]{entityplayermp.getName(), statbase.createChatComponent()});
                        }
                        ArrayList list = Lists.newArrayList();
                        while (achievement.parentAchievement != null && !entityplayermp.getStatFile().hasAchievementUnlocked(achievement.parentAchievement)) {
                            list.add((Object)achievement.parentAchievement);
                            achievement = achievement.parentAchievement;
                        }
                        for (Achievement achievement1 : Lists.reverse((List)list)) {
                            entityplayermp.triggerAchievement((StatBase)achievement1);
                        }
                    } else if (flag1) {
                        if (!entityplayermp.getStatFile().hasAchievementUnlocked(achievement)) {
                            throw new CommandException("commands.achievement.dontHave", new Object[]{entityplayermp.getName(), statbase.createChatComponent()});
                        }
                        ArrayList list1 = Lists.newArrayList((Iterator)Iterators.filter((Iterator)AchievementList.achievementList.iterator(), (Predicate)new /* Unavailable Anonymous Inner Class!! */));
                        ArrayList list2 = Lists.newArrayList((Iterable)list1);
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
                                list2.remove((Object)achievement2);
                                achievement3 = achievement3.parentAchievement;
                            }
                        }
                        for (Achievement achievement6 : list2) {
                            entityplayermp.func_175145_a((StatBase)achievement6);
                        }
                    }
                }
                if (flag) {
                    entityplayermp.triggerAchievement(statbase);
                    CommandAchievement.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.achievement.give.success.one", (Object[])new Object[]{entityplayermp.getName(), statbase.createChatComponent()});
                } else if (flag1) {
                    entityplayermp.func_175145_a(statbase);
                    CommandAchievement.notifyOperators((ICommandSender)sender, (ICommand)this, (String)"commands.achievement.take.success.one", (Object[])new Object[]{statbase.createChatComponent(), entityplayermp.getName()});
                }
            }
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return CommandAchievement.getListOfStringsMatchingLastWord((String[])args, (String[])new String[]{"give", "take"});
        }
        if (args.length != 2) {
            return args.length == 3 ? CommandAchievement.getListOfStringsMatchingLastWord((String[])args, (String[])MinecraftServer.getServer().getAllUsernames()) : null;
        }
        ArrayList list = Lists.newArrayList();
        for (StatBase statbase : StatList.allStats) {
            list.add((Object)statbase.statId);
        }
        return CommandAchievement.getListOfStringsMatchingLastWord((String[])args, (Collection)list);
    }

    public boolean isUsernameIndex(String[] args, int index) {
        return index == 2;
    }
}
