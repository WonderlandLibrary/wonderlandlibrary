package net.minecraft.command.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;

public class CommandAchievement extends CommandBase {
   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 2;
   }

   public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
      if (var2.length < 2) {
         throw new WrongUsageException("commands.achievement.usage", new Object[0]);
      } else {
         StatBase var3 = StatList.getOneShotStat(var2[1]);
         if (var3 == null && !var2[1].equals("*")) {
            throw new CommandException("commands.achievement.unknownAchievement", new Object[]{var2[1]});
         } else {
            EntityPlayerMP var4 = var2.length >= 3 ? getPlayer(var1, var2[2]) : getCommandSenderAsPlayer(var1);
            boolean var5 = var2[0].equalsIgnoreCase("give");
            boolean var6 = var2[0].equalsIgnoreCase("take");
            if (var5 || var6) {
               Achievement var7;
               if (var3 == null) {
                  Iterator var8;
                  if (var5) {
                     var8 = AchievementList.achievementList.iterator();

                     while(var8.hasNext()) {
                        var7 = (Achievement)var8.next();
                        var4.triggerAchievement(var7);
                     }

                     notifyOperators(var1, this, "commands.achievement.give.success.all", new Object[]{var4.getName()});
                  } else if (var6) {
                     var8 = Lists.reverse(AchievementList.achievementList).iterator();

                     while(var8.hasNext()) {
                        var7 = (Achievement)var8.next();
                        var4.func_175145_a(var7);
                     }

                     notifyOperators(var1, this, "commands.achievement.take.success.all", new Object[]{var4.getName()});
                  }
               } else {
                  if (var3 instanceof Achievement) {
                     var7 = (Achievement)var3;
                     ArrayList var14;
                     if (var5) {
                        if (var4.getStatFile().hasAchievementUnlocked(var7)) {
                           throw new CommandException("commands.achievement.alreadyHave", new Object[]{var4.getName(), var3.func_150955_j()});
                        }

                        for(var14 = Lists.newArrayList(); var7.parentAchievement != null && !var4.getStatFile().hasAchievementUnlocked(var7.parentAchievement); var7 = var7.parentAchievement) {
                           var14.add(var7.parentAchievement);
                        }

                        Iterator var16 = Lists.reverse(var14).iterator();

                        while(var16.hasNext()) {
                           Achievement var15 = (Achievement)var16.next();
                           var4.triggerAchievement(var15);
                        }
                     } else if (var6) {
                        if (!var4.getStatFile().hasAchievementUnlocked(var7)) {
                           throw new CommandException("commands.achievement.dontHave", new Object[]{var4.getName(), var3.func_150955_j()});
                        }

                        var14 = Lists.newArrayList((Iterator)Iterators.filter(AchievementList.achievementList.iterator(), new Predicate(this, var4, var3) {
                           final CommandAchievement this$0;
                           private final StatBase val$statbase;
                           private final EntityPlayerMP val$entityplayermp;

                           {
                              this.this$0 = var1;
                              this.val$entityplayermp = var2;
                              this.val$statbase = var3;
                           }

                           public boolean apply(Achievement var1) {
                              return this.val$entityplayermp.getStatFile().hasAchievementUnlocked(var1) && var1 != this.val$statbase;
                           }

                           public boolean apply(Object var1) {
                              return this.apply((Achievement)var1);
                           }
                        }));
                        ArrayList var9 = Lists.newArrayList((Iterable)var14);
                        Iterator var11 = var14.iterator();

                        label110:
                        while(true) {
                           Achievement var10;
                           Achievement var12;
                           boolean var13;
                           do {
                              if (!var11.hasNext()) {
                                 var11 = var9.iterator();

                                 while(true) {
                                    if (!var11.hasNext()) {
                                       break label110;
                                    }

                                    var10 = (Achievement)var11.next();
                                    var4.func_175145_a(var10);
                                 }
                              }

                              var10 = (Achievement)var11.next();
                              var12 = var10;

                              for(var13 = false; var12 != null; var12 = var12.parentAchievement) {
                                 if (var12 == var3) {
                                    var13 = true;
                                 }
                              }
                           } while(var13);

                           for(var12 = var10; var12 != null; var12 = var12.parentAchievement) {
                              var9.remove(var10);
                           }
                        }
                     }
                  }

                  if (var5) {
                     var4.triggerAchievement(var3);
                     notifyOperators(var1, this, "commands.achievement.give.success.one", new Object[]{var4.getName(), var3.func_150955_j()});
                  } else if (var6) {
                     var4.func_175145_a(var3);
                     notifyOperators(var1, this, "commands.achievement.take.success.one", new Object[]{var3.func_150955_j(), var4.getName()});
                  }
               }
            }

         }
      }
   }

   public int getRequiredPermissionLevel() {
      return 2;
   }

   public String getCommandUsage(ICommandSender var1) {
      return "commands.achievement.usage";
   }

   public String getCommandName() {
      return "achievement";
   }

   public List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3) {
      if (var2.length == 1) {
         return getListOfStringsMatchingLastWord(var2, new String[]{"give", "take"});
      } else if (var2.length != 2) {
         return var2.length == 3 ? getListOfStringsMatchingLastWord(var2, MinecraftServer.getServer().getAllUsernames()) : null;
      } else {
         ArrayList var4 = Lists.newArrayList();
         Iterator var6 = StatList.allStats.iterator();

         while(var6.hasNext()) {
            StatBase var5 = (StatBase)var6.next();
            var4.add(var5.statId);
         }

         return getListOfStringsMatchingLastWord(var2, var4);
      }
   }
}
