/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000113";

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
        final StatBase var3 = StatList.getOneShotStat(args[1]);
        if (var3 == null && !args[1].equals("*")) {
            throw new CommandException("commands.achievement.unknownAchievement", args[1]);
        }
        final EntityPlayerMP var4 = args.length >= 3 ? CommandAchievement.getPlayer(sender, args[2]) : CommandAchievement.getCommandSenderAsPlayer(sender);
        boolean var5 = args[0].equalsIgnoreCase("give");
        boolean var6 = args[0].equalsIgnoreCase("take");
        if (var5 || var6) {
            if (var3 == null) {
                if (var5) {
                    for (Achievement var12 : AchievementList.achievementList) {
                        var4.triggerAchievement(var12);
                    }
                    CommandAchievement.notifyOperators(sender, (ICommand)this, "commands.achievement.give.success.all", var4.getName());
                } else if (var6) {
                    for (Achievement var12 : Lists.reverse((List)AchievementList.achievementList)) {
                        var4.func_175145_a(var12);
                    }
                    CommandAchievement.notifyOperators(sender, (ICommand)this, "commands.achievement.take.success.all", var4.getName());
                }
            } else {
                if (var3 instanceof Achievement) {
                    Achievement var7 = (Achievement)var3;
                    if (var5) {
                        if (var4.getStatFile().hasAchievementUnlocked(var7)) {
                            throw new CommandException("commands.achievement.alreadyHave", var4.getName(), var3.func_150955_j());
                        }
                        ArrayList var8 = Lists.newArrayList();
                        while (var7.parentAchievement != null && !var4.getStatFile().hasAchievementUnlocked(var7.parentAchievement)) {
                            var8.add(var7.parentAchievement);
                            var7 = var7.parentAchievement;
                        }
                        for (Achievement var10 : Lists.reverse((List)var8)) {
                            var4.triggerAchievement(var10);
                        }
                    } else if (var6) {
                        if (!var4.getStatFile().hasAchievementUnlocked(var7)) {
                            throw new CommandException("commands.achievement.dontHave", var4.getName(), var3.func_150955_j());
                        }
                        ArrayList var8 = Lists.newArrayList((Iterator)Iterators.filter(AchievementList.achievementList.iterator(), (Predicate)new Predicate(){
                            private static final String __OBFID = "CL_00002350";

                            public boolean func_179605_a(Achievement p_179605_1_) {
                                return var4.getStatFile().hasAchievementUnlocked(p_179605_1_) && p_179605_1_ != var3;
                            }

                            public boolean apply(Object p_apply_1_) {
                                return this.func_179605_a((Achievement)p_apply_1_);
                            }
                        }));
                        while (var7.parentAchievement != null && var4.getStatFile().hasAchievementUnlocked(var7.parentAchievement)) {
                            var8.remove(var7.parentAchievement);
                            var7 = var7.parentAchievement;
                        }
                        for (Achievement var10 : var8) {
                            var4.func_175145_a(var10);
                        }
                    }
                }
                if (var5) {
                    var4.triggerAchievement(var3);
                    CommandAchievement.notifyOperators(sender, (ICommand)this, "commands.achievement.give.success.one", var4.getName(), var3.func_150955_j());
                } else if (var6) {
                    var4.func_175145_a(var3);
                    CommandAchievement.notifyOperators(sender, (ICommand)this, "commands.achievement.take.success.one", var3.func_150955_j(), var4.getName());
                }
            }
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return CommandAchievement.getListOfStringsMatchingLastWord(args, "give", "take");
        }
        if (args.length != 2) {
            return args.length == 3 ? CommandAchievement.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
        }
        ArrayList var4 = Lists.newArrayList();
        for (StatBase var6 : StatList.allStats) {
            var4.add(var6.statId);
        }
        return CommandAchievement.func_175762_a(args, var4);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 2;
    }
}

