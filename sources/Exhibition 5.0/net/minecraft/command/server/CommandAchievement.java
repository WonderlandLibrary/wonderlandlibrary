// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command.server;

import java.util.Collection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;
import java.util.Iterator;
import com.google.common.collect.Iterators;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.base.Predicate;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.command.ICommand;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.command.CommandException;
import net.minecraft.stats.StatList;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandAchievement extends CommandBase
{
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
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.achievement.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.achievement.usage", new Object[0]);
        }
        final StatBase var3 = StatList.getOneShotStat(args[1]);
        if (var3 == null && !args[1].equals("*")) {
            throw new CommandException("commands.achievement.unknownAchievement", new Object[] { args[1] });
        }
        final EntityPlayerMP var4 = (args.length >= 3) ? CommandBase.getPlayer(sender, args[2]) : CommandBase.getCommandSenderAsPlayer(sender);
        final boolean var5 = args[0].equalsIgnoreCase("give");
        final boolean var6 = args[0].equalsIgnoreCase("take");
        if (var5 || var6) {
            if (var3 == null) {
                if (var5) {
                    for (final Achievement var8 : AchievementList.achievementList) {
                        var4.triggerAchievement(var8);
                    }
                    CommandBase.notifyOperators(sender, this, "commands.achievement.give.success.all", var4.getName());
                }
                else if (var6) {
                    for (final Achievement var8 : Lists.reverse(AchievementList.achievementList)) {
                        var4.func_175145_a(var8);
                    }
                    CommandBase.notifyOperators(sender, this, "commands.achievement.take.success.all", var4.getName());
                }
            }
            else {
                if (var3 instanceof Achievement) {
                    Achievement var9 = (Achievement)var3;
                    if (var5) {
                        if (var4.getStatFile().hasAchievementUnlocked(var9)) {
                            throw new CommandException("commands.achievement.alreadyHave", new Object[] { var4.getName(), var3.func_150955_j() });
                        }
                        final ArrayList var10 = Lists.newArrayList();
                        while (var9.parentAchievement != null && !var4.getStatFile().hasAchievementUnlocked(var9.parentAchievement)) {
                            var10.add(var9.parentAchievement);
                            var9 = var9.parentAchievement;
                        }
                        for (final Achievement var12 : Lists.reverse((List)var10)) {
                            var4.triggerAchievement(var12);
                        }
                    }
                    else if (var6) {
                        if (!var4.getStatFile().hasAchievementUnlocked(var9)) {
                            throw new CommandException("commands.achievement.dontHave", new Object[] { var4.getName(), var3.func_150955_j() });
                        }
                        final ArrayList var10 = Lists.newArrayList((Iterator)Iterators.filter((Iterator)AchievementList.achievementList.iterator(), (Predicate)new Predicate() {
                            private static final String __OBFID = "CL_00002350";
                            
                            public boolean func_179605_a(final Achievement p_179605_1_) {
                                return var4.getStatFile().hasAchievementUnlocked(p_179605_1_) && p_179605_1_ != var3;
                            }
                            
                            public boolean apply(final Object p_apply_1_) {
                                return this.func_179605_a((Achievement)p_apply_1_);
                            }
                        }));
                        while (var9.parentAchievement != null && var4.getStatFile().hasAchievementUnlocked(var9.parentAchievement)) {
                            var10.remove(var9.parentAchievement);
                            var9 = var9.parentAchievement;
                        }
                        for (final Achievement var12 : var10) {
                            var4.func_175145_a(var12);
                        }
                    }
                }
                if (var5) {
                    var4.triggerAchievement(var3);
                    CommandBase.notifyOperators(sender, this, "commands.achievement.give.success.one", var4.getName(), var3.func_150955_j());
                }
                else if (var6) {
                    var4.func_175145_a(var3);
                    CommandBase.notifyOperators(sender, this, "commands.achievement.take.success.one", var3.func_150955_j(), var4.getName());
                }
            }
        }
    }
    
    @Override
    public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "give", "take");
        }
        if (args.length != 2) {
            return (args.length == 3) ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
        }
        final ArrayList var4 = Lists.newArrayList();
        for (final StatBase var6 : StatList.allStats) {
            var4.add(var6.statId);
        }
        return CommandBase.func_175762_a(args, var4);
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 2;
    }
}
