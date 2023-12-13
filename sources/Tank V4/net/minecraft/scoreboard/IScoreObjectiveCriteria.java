package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.util.EnumChatFormatting;

public interface IScoreObjectiveCriteria {
   IScoreObjectiveCriteria totalKillCount = new ScoreDummyCriteria("totalKillCount");
   Map INSTANCES = Maps.newHashMap();
   IScoreObjectiveCriteria DUMMY = new ScoreDummyCriteria("dummy");
   IScoreObjectiveCriteria health = new ScoreHealthCriteria("health");
   IScoreObjectiveCriteria playerKillCount = new ScoreDummyCriteria("playerKillCount");
   IScoreObjectiveCriteria[] field_178793_i = new IScoreObjectiveCriteria[]{new GoalColor("killedByTeam.", EnumChatFormatting.BLACK), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_BLUE), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_GREEN), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_AQUA), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_RED), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_PURPLE), new GoalColor("killedByTeam.", EnumChatFormatting.GOLD), new GoalColor("killedByTeam.", EnumChatFormatting.GRAY), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_GRAY), new GoalColor("killedByTeam.", EnumChatFormatting.BLUE), new GoalColor("killedByTeam.", EnumChatFormatting.GREEN), new GoalColor("killedByTeam.", EnumChatFormatting.AQUA), new GoalColor("killedByTeam.", EnumChatFormatting.RED), new GoalColor("killedByTeam.", EnumChatFormatting.LIGHT_PURPLE), new GoalColor("killedByTeam.", EnumChatFormatting.YELLOW), new GoalColor("killedByTeam.", EnumChatFormatting.WHITE)};
   IScoreObjectiveCriteria[] field_178792_h = new IScoreObjectiveCriteria[]{new GoalColor("teamkill.", EnumChatFormatting.BLACK), new GoalColor("teamkill.", EnumChatFormatting.DARK_BLUE), new GoalColor("teamkill.", EnumChatFormatting.DARK_GREEN), new GoalColor("teamkill.", EnumChatFormatting.DARK_AQUA), new GoalColor("teamkill.", EnumChatFormatting.DARK_RED), new GoalColor("teamkill.", EnumChatFormatting.DARK_PURPLE), new GoalColor("teamkill.", EnumChatFormatting.GOLD), new GoalColor("teamkill.", EnumChatFormatting.GRAY), new GoalColor("teamkill.", EnumChatFormatting.DARK_GRAY), new GoalColor("teamkill.", EnumChatFormatting.BLUE), new GoalColor("teamkill.", EnumChatFormatting.GREEN), new GoalColor("teamkill.", EnumChatFormatting.AQUA), new GoalColor("teamkill.", EnumChatFormatting.RED), new GoalColor("teamkill.", EnumChatFormatting.LIGHT_PURPLE), new GoalColor("teamkill.", EnumChatFormatting.YELLOW), new GoalColor("teamkill.", EnumChatFormatting.WHITE)};
   IScoreObjectiveCriteria TRIGGER = new ScoreDummyCriteria("trigger");
   IScoreObjectiveCriteria deathCount = new ScoreDummyCriteria("deathCount");

   IScoreObjectiveCriteria.EnumRenderType getRenderType();

   int func_96635_a(List var1);

   boolean isReadOnly();

   String getName();

   public static enum EnumRenderType {
      private static final Map field_178801_c = Maps.newHashMap();
      private static final IScoreObjectiveCriteria.EnumRenderType[] ENUM$VALUES = new IScoreObjectiveCriteria.EnumRenderType[]{INTEGER, HEARTS};
      HEARTS("hearts"),
      INTEGER("integer");

      private final String field_178798_d;

      public static IScoreObjectiveCriteria.EnumRenderType func_178795_a(String var0) {
         IScoreObjectiveCriteria.EnumRenderType var1 = (IScoreObjectiveCriteria.EnumRenderType)field_178801_c.get(var0);
         return var1 == null ? INTEGER : var1;
      }

      private EnumRenderType(String var3) {
         this.field_178798_d = var3;
      }

      static {
         IScoreObjectiveCriteria.EnumRenderType[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            IScoreObjectiveCriteria.EnumRenderType var0 = var3[var1];
            field_178801_c.put(var0.func_178796_a(), var0);
         }

      }

      public String func_178796_a() {
         return this.field_178798_d;
      }
   }
}
