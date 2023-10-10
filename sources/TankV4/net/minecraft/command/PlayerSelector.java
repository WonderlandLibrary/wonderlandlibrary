package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public class PlayerSelector {
   private static final Pattern intListPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
   private static final Pattern tokenPattern = Pattern.compile("^@([pare])(?:\\[([\\w=,!-]*)\\])?$");
   private static final Pattern keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");
   private static final Set WORLD_BINDING_ARGS = Sets.newHashSet((Object[])("x", "y", "z", "dx", "dy", "dz", "rm", "r"));

   public static Entity matchOneEntity(ICommandSender var0, String var1, Class var2) {
      List var3 = matchEntities(var0, var1, var2);
      return var3.size() == 1 ? (Entity)var3.get(0) : null;
   }

   private static List func_179663_a(Map var0, String var1) {
      ArrayList var2 = Lists.newArrayList();
      String var3 = func_179651_b(var0, "type");
      boolean var4 = var3 != null && var3.startsWith("!");
      if (var4) {
         var3 = var3.substring(1);
      }

      boolean var5 = !var1.equals("e");
      boolean var6 = var1.equals("r") && var3 != null;
      if ((var3 == null || !var1.equals("e")) && !var6) {
         if (var5) {
            var2.add(new Predicate() {
               public boolean apply(Object var1) {
                  return this.apply((Entity)var1);
               }

               public boolean apply(Entity var1) {
                  return var1 instanceof EntityPlayer;
               }
            });
         }
      } else {
         var2.add(new Predicate(var3, var4) {
            private final boolean val$flag;
            private final String val$s_f;

            {
               this.val$s_f = var1;
               this.val$flag = var2;
            }

            public boolean apply(Entity var1) {
               return EntityList.isStringEntityName(var1, this.val$s_f) ^ this.val$flag;
            }

            public boolean apply(Object var1) {
               return this.apply((Entity)var1);
            }
         });
      }

      return var2;
   }

   private static List func_179658_a(List var0, Map var1, ICommandSender var2, Class var3, String var4, BlockPos var5) {
      int var6 = parseIntWithDefault(var1, "c", !var4.equals("a") && !var4.equals("e") ? 1 : 0);
      if (!var4.equals("p") && !var4.equals("a") && !var4.equals("e")) {
         if (var4.equals("r")) {
            Collections.shuffle((List)var0);
         }
      } else if (var5 != null) {
         Collections.sort((List)var0, new Comparator(var5) {
            private final BlockPos val$p_179658_5_;

            public int compare(Entity var1, Entity var2) {
               return ComparisonChain.start().compare(var1.getDistanceSq(this.val$p_179658_5_), var2.getDistanceSq(this.val$p_179658_5_)).result();
            }

            public int compare(Object var1, Object var2) {
               return this.compare((Entity)var1, (Entity)var2);
            }

            {
               this.val$p_179658_5_ = var1;
            }
         });
      }

      Entity var7 = var2.getCommandSenderEntity();
      if (var7 != null && var3.isAssignableFrom(var7.getClass()) && var6 == 1 && ((List)var0).contains(var7) && !"r".equals(var4)) {
         var0 = Lists.newArrayList((Object[])(var7));
      }

      if (var6 != 0) {
         if (var6 < 0) {
            Collections.reverse((List)var0);
         }

         var0 = ((List)var0).subList(0, Math.min(Math.abs(var6), ((List)var0).size()));
      }

      return (List)var0;
   }

   public static List matchEntities(ICommandSender var0, String var1, Class var2) {
      Matcher var3 = tokenPattern.matcher(var1);
      if (var3.matches() && var0.canCommandSenderUseCommand(1, "@")) {
         Map var4 = getArgumentMap(var3.group(2));
         if (var4 != false) {
            return Collections.emptyList();
         } else {
            String var5 = var3.group(1);
            BlockPos var6 = func_179664_b(var4, var0.getPosition());
            List var7 = getWorlds(var0, var4);
            ArrayList var8 = Lists.newArrayList();
            Iterator var10 = var7.iterator();

            while(var10.hasNext()) {
               World var9 = (World)var10.next();
               if (var9 != null) {
                  ArrayList var11 = Lists.newArrayList();
                  var11.addAll(func_179663_a(var4, var5));
                  var11.addAll(func_179648_b(var4));
                  var11.addAll(func_179649_c(var4));
                  var11.addAll(func_179659_d(var4));
                  var11.addAll(func_179657_e(var4));
                  var11.addAll(func_179647_f(var4));
                  var11.addAll(func_180698_a(var4, var6));
                  var11.addAll(func_179662_g(var4));
                  var8.addAll(filterResults(var4, var2, var11, var5, var9, var6));
               }
            }

            return func_179658_a(var8, var4, var0, var2, var5, var6);
         }
      } else {
         return Collections.emptyList();
      }
   }

   private static List func_179659_d(Map var0) {
      ArrayList var1 = Lists.newArrayList();
      String var2 = func_179651_b(var0, "team");
      boolean var3 = var2 != null && var2.startsWith("!");
      if (var3) {
         var2 = var2.substring(1);
      }

      if (var2 != null) {
         var1.add(new Predicate(var2, var3) {
            private final boolean val$flag;
            private final String val$s_f;

            public boolean apply(Entity var1) {
               if (!(var1 instanceof EntityLivingBase)) {
                  return false;
               } else {
                  EntityLivingBase var2 = (EntityLivingBase)var1;
                  Team var3 = var2.getTeam();
                  String var4 = var3 == null ? "" : var3.getRegisteredName();
                  return var4.equals(this.val$s_f) ^ this.val$flag;
               }
            }

            {
               this.val$s_f = var1;
               this.val$flag = var2;
            }

            public boolean apply(Object var1) {
               return this.apply((Entity)var1);
            }
         });
      }

      return var1;
   }

   private static List filterResults(Map var0, Class var1, List var2, String var3, World var4, BlockPos var5) {
      ArrayList var6 = Lists.newArrayList();
      String var7 = func_179651_b(var0, "type");
      var7 = var7 != null && var7.startsWith("!") ? var7.substring(1) : var7;
      boolean var8 = !var3.equals("e");
      boolean var9 = var3.equals("r") && var7 != null;
      int var10 = parseIntWithDefault(var0, "dx", 0);
      int var11 = parseIntWithDefault(var0, "dy", 0);
      int var12 = parseIntWithDefault(var0, "dz", 0);
      int var13 = parseIntWithDefault(var0, "r", -1);
      Predicate var14 = Predicates.and((Iterable)var2);
      Predicate var15 = Predicates.and(EntitySelectors.selectAnything, var14);
      if (var5 != null) {
         int var16 = var4.playerEntities.size();
         int var17 = var4.loadedEntityList.size();
         boolean var18 = var16 < var17 / 16;
         AxisAlignedBB var19;
         if (!var0.containsKey("dx") && !var0.containsKey("dy") && !var0.containsKey("dz")) {
            if (var13 >= 0) {
               var19 = new AxisAlignedBB((double)(var5.getX() - var13), (double)(var5.getY() - var13), (double)(var5.getZ() - var13), (double)(var5.getX() + var13 + 1), (double)(var5.getY() + var13 + 1), (double)(var5.getZ() + var13 + 1));
               if (var8 && var18 && !var9) {
                  var6.addAll(var4.getPlayers(var1, var15));
               } else {
                  var6.addAll(var4.getEntitiesWithinAABB(var1, var19, var15));
               }
            } else if (var3.equals("a")) {
               var6.addAll(var4.getPlayers(var1, var14));
            } else if (var3.equals("p") || var3.equals("r") && !var9) {
               var6.addAll(var4.getPlayers(var1, var15));
            } else {
               var6.addAll(var4.getEntities(var1, var15));
            }
         } else {
            var19 = func_179661_a(var5, var10, var11, var12);
            if (var8 && var18 && !var9) {
               Predicate var20 = new Predicate(var19) {
                  private final AxisAlignedBB val$axisalignedbb;

                  public boolean apply(Object var1) {
                     return this.apply((Entity)var1);
                  }

                  {
                     this.val$axisalignedbb = var1;
                  }

                  public boolean apply(Entity var1) {
                     return var1.posX >= this.val$axisalignedbb.minX && var1.posY >= this.val$axisalignedbb.minY && var1.posZ >= this.val$axisalignedbb.minZ ? var1.posX < this.val$axisalignedbb.maxX && var1.posY < this.val$axisalignedbb.maxY && var1.posZ < this.val$axisalignedbb.maxZ : false;
                  }
               };
               var6.addAll(var4.getPlayers(var1, Predicates.and(var15, var20)));
            } else {
               var6.addAll(var4.getEntitiesWithinAABB(var1, var19, var15));
            }
         }
      } else if (var3.equals("a")) {
         var6.addAll(var4.getPlayers(var1, var14));
      } else if (var3.equals("p") || var3.equals("r") && !var9) {
         var6.addAll(var4.getPlayers(var1, var15));
      } else {
         var6.addAll(var4.getEntities(var1, var15));
      }

      return var6;
   }

   private static List getWorlds(ICommandSender param0, Map param1) {
      // $FF: Couldn't be decompiled
   }

   private static List func_179648_b(Map var0) {
      ArrayList var1 = Lists.newArrayList();
      int var2 = parseIntWithDefault(var0, "lm", -1);
      int var3 = parseIntWithDefault(var0, "l", -1);
      if (var2 > -1 || var3 > -1) {
         var1.add(new Predicate(var2, var3) {
            private final int val$j;
            private final int val$i;

            {
               this.val$i = var1;
               this.val$j = var2;
            }

            public boolean apply(Entity var1) {
               if (!(var1 instanceof EntityPlayerMP)) {
                  return false;
               } else {
                  EntityPlayerMP var2 = (EntityPlayerMP)var1;
                  return (this.val$i <= -1 || var2.experienceLevel >= this.val$i) && (this.val$j <= -1 || var2.experienceLevel <= this.val$j);
               }
            }

            public boolean apply(Object var1) {
               return this.apply((Entity)var1);
            }
         });
      }

      return var1;
   }

   private static List func_179662_g(Map var0) {
      ArrayList var1 = Lists.newArrayList();
      int var2;
      int var3;
      if (var0.containsKey("rym") || var0.containsKey("ry")) {
         var2 = func_179650_a(parseIntWithDefault(var0, "rym", 0));
         var3 = func_179650_a(parseIntWithDefault(var0, "ry", 359));
         var1.add(new Predicate(var2, var3) {
            private final int val$i;
            private final int val$j;

            {
               this.val$i = var1;
               this.val$j = var2;
            }

            public boolean apply(Object var1) {
               return this.apply((Entity)var1);
            }

            public boolean apply(Entity var1) {
               int var2 = PlayerSelector.func_179650_a((int)Math.floor((double)var1.rotationYaw));
               return this.val$i > this.val$j ? var2 >= this.val$i || var2 <= this.val$j : var2 >= this.val$i && var2 <= this.val$j;
            }
         });
      }

      if (var0.containsKey("rxm") || var0.containsKey("rx")) {
         var2 = func_179650_a(parseIntWithDefault(var0, "rxm", 0));
         var3 = func_179650_a(parseIntWithDefault(var0, "rx", 359));
         var1.add(new Predicate(var2, var3) {
            private final int val$k;
            private final int val$l;

            {
               this.val$k = var1;
               this.val$l = var2;
            }

            public boolean apply(Entity var1) {
               int var2 = PlayerSelector.func_179650_a((int)Math.floor((double)var1.rotationPitch));
               return this.val$k > this.val$l ? var2 >= this.val$k || var2 <= this.val$l : var2 >= this.val$k && var2 <= this.val$l;
            }

            public boolean apply(Object var1) {
               return this.apply((Entity)var1);
            }
         });
      }

      return var1;
   }

   public static boolean hasArguments(String var0) {
      return tokenPattern.matcher(var0).matches();
   }

   public static IChatComponent matchEntitiesToChatComponent(ICommandSender var0, String var1) {
      List var2 = matchEntities(var0, var1, Entity.class);
      if (var2.isEmpty()) {
         return null;
      } else {
         ArrayList var3 = Lists.newArrayList();
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            Entity var4 = (Entity)var5.next();
            var3.add(var4.getDisplayName());
         }

         return CommandBase.join(var3);
      }
   }

   public static Map func_96560_a(Map var0) {
      HashMap var1 = Maps.newHashMap();
      Iterator var3 = var0.keySet().iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         if (var2.startsWith("score_") && var2.length() > "score_".length()) {
            var1.put(var2.substring("score_".length()), MathHelper.parseIntWithDefault((String)var0.get(var2), 1));
         }
      }

      return var1;
   }

   private static List func_179657_e(Map var0) {
      ArrayList var1 = Lists.newArrayList();
      Map var2 = func_96560_a(var0);
      if (var2 != null && var2.size() > 0) {
         var1.add(new Predicate(var2) {
            private final Map val$map;

            public boolean apply(Entity var1) {
               Scoreboard var2 = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
               Iterator var4 = this.val$map.entrySet().iterator();

               Entry var3;
               boolean var6;
               int var10;
               do {
                  if (!var4.hasNext()) {
                     return true;
                  }

                  var3 = (Entry)var4.next();
                  String var5 = (String)var3.getKey();
                  var6 = false;
                  if (var5.endsWith("_min") && var5.length() > 4) {
                     var6 = true;
                     var5 = var5.substring(0, var5.length() - 4);
                  }

                  ScoreObjective var7 = var2.getObjective(var5);
                  if (var7 == null) {
                     return false;
                  }

                  String var8 = var1 instanceof EntityPlayerMP ? var1.getName() : var1.getUniqueID().toString();
                  if (!var2.entityHasObjective(var8, var7)) {
                     return false;
                  }

                  Score var9 = var2.getValueFromObjective(var8, var7);
                  var10 = var9.getScorePoints();
                  if (var10 < (Integer)var3.getValue() && var6) {
                     return false;
                  }
               } while(var10 <= (Integer)var3.getValue() || var6);

               return false;
            }

            {
               this.val$map = var1;
            }

            public boolean apply(Object var1) {
               return this.apply((Entity)var1);
            }
         });
      }

      return var1;
   }

   private static AxisAlignedBB func_179661_a(BlockPos var0, int var1, int var2, int var3) {
      boolean var4 = var1 < 0;
      boolean var5 = var2 < 0;
      boolean var6 = var3 < 0;
      int var7 = var0.getX() + (var4 ? var1 : 0);
      int var8 = var0.getY() + (var5 ? var2 : 0);
      int var9 = var0.getZ() + (var6 ? var3 : 0);
      int var10 = var0.getX() + (var4 ? 0 : var1) + 1;
      int var11 = var0.getY() + (var5 ? 0 : var2) + 1;
      int var12 = var0.getZ() + (var6 ? 0 : var3) + 1;
      return new AxisAlignedBB((double)var7, (double)var8, (double)var9, (double)var10, (double)var11, (double)var12);
   }

   private static List func_180698_a(Map var0, BlockPos var1) {
      ArrayList var2 = Lists.newArrayList();
      int var3 = parseIntWithDefault(var0, "rm", -1);
      int var4 = parseIntWithDefault(var0, "r", -1);
      if (var1 != null && (var3 >= 0 || var4 >= 0)) {
         int var5 = var3 * var3;
         int var6 = var4 * var4;
         var2.add(new Predicate(var1, var3, var5, var4, var6) {
            private final int val$i;
            private final int val$l;
            private final int val$k;
            private final BlockPos val$p_180698_1_;
            private final int val$j;

            public boolean apply(Object var1) {
               return this.apply((Entity)var1);
            }

            public boolean apply(Entity var1) {
               int var2 = (int)var1.getDistanceSqToCenter(this.val$p_180698_1_);
               return (this.val$i < 0 || var2 >= this.val$k) && (this.val$j < 0 || var2 <= this.val$l);
            }

            {
               this.val$p_180698_1_ = var1;
               this.val$i = var2;
               this.val$k = var3;
               this.val$j = var4;
               this.val$l = var5;
            }
         });
      }

      return var2;
   }

   private static List func_179649_c(Map var0) {
      ArrayList var1 = Lists.newArrayList();
      int var2 = parseIntWithDefault(var0, "m", WorldSettings.GameType.NOT_SET.getID());
      if (var2 != WorldSettings.GameType.NOT_SET.getID()) {
         var1.add(new Predicate(var2) {
            private final int val$i;

            {
               this.val$i = var1;
            }

            public boolean apply(Object var1) {
               return this.apply((Entity)var1);
            }

            public boolean apply(Entity var1) {
               if (!(var1 instanceof EntityPlayerMP)) {
                  return false;
               } else {
                  EntityPlayerMP var2 = (EntityPlayerMP)var1;
                  return var2.theItemInWorldManager.getGameType().getID() == this.val$i;
               }
            }
         });
      }

      return var1;
   }

   public static boolean matchesMultiplePlayers(String var0) {
      Matcher var1 = tokenPattern.matcher(var0);
      if (!var1.matches()) {
         return false;
      } else {
         Map var2 = getArgumentMap(var1.group(2));
         String var3 = var1.group(1);
         int var4 = !"a".equals(var3) && !"e".equals(var3) ? 1 : 0;
         return parseIntWithDefault(var2, "c", var4) != 1;
      }
   }

   public static int func_179650_a(int var0) {
      var0 %= 360;
      if (var0 >= 160) {
         var0 -= 360;
      }

      if (var0 < 0) {
         var0 += 360;
      }

      return var0;
   }

   private static int parseIntWithDefault(Map var0, String var1, int var2) {
      return var0.containsKey(var1) ? MathHelper.parseIntWithDefault((String)var0.get(var1), var2) : var2;
   }

   private static List func_179647_f(Map var0) {
      ArrayList var1 = Lists.newArrayList();
      String var2 = func_179651_b(var0, "name");
      boolean var3 = var2 != null && var2.startsWith("!");
      if (var3) {
         var2 = var2.substring(1);
      }

      if (var2 != null) {
         var1.add(new Predicate(var2, var3) {
            private final boolean val$flag;
            private final String val$s_f;

            public boolean apply(Object var1) {
               return this.apply((Entity)var1);
            }

            public boolean apply(Entity var1) {
               return var1.getName().equals(this.val$s_f) ^ this.val$flag;
            }

            {
               this.val$s_f = var1;
               this.val$flag = var2;
            }
         });
      }

      return var1;
   }

   private static String func_179651_b(Map var0, String var1) {
      return (String)var0.get(var1);
   }

   public static EntityPlayerMP matchOnePlayer(ICommandSender var0, String var1) {
      return (EntityPlayerMP)matchOneEntity(var0, var1, EntityPlayerMP.class);
   }

   private static BlockPos func_179664_b(Map var0, BlockPos var1) {
      return new BlockPos(parseIntWithDefault(var0, "x", var1.getX()), parseIntWithDefault(var0, "y", var1.getY()), parseIntWithDefault(var0, "z", var1.getZ()));
   }

   private static Map getArgumentMap(String var0) {
      HashMap var1 = Maps.newHashMap();
      if (var0 == null) {
         return var1;
      } else {
         int var2 = 0;
         int var3 = -1;

         Matcher var4;
         for(var4 = intListPattern.matcher(var0); var4.find(); var3 = var4.end()) {
            String var5 = null;
            switch(var2++) {
            case 0:
               var5 = "x";
               break;
            case 1:
               var5 = "y";
               break;
            case 2:
               var5 = "z";
               break;
            case 3:
               var5 = "r";
            }

            if (var5 != null && var4.group(1).length() > 0) {
               var1.put(var5, var4.group(1));
            }
         }

         if (var3 < var0.length()) {
            var4 = keyValueListPattern.matcher(var3 == -1 ? var0 : var0.substring(var3));

            while(var4.find()) {
               var1.put(var4.group(1), var4.group(2));
            }
         }

         return var1;
      }
   }
}
