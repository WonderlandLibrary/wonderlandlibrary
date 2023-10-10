package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;

public abstract class Team {
   public abstract String getRegisteredName();

   public abstract boolean getSeeFriendlyInvisiblesEnabled();

   public abstract Collection getMembershipCollection();

   public abstract Team.EnumVisible getDeathMessageVisibility();

   public boolean isSameTeam(Team var1) {
      return var1 == null ? false : this == var1;
   }

   public abstract String formatString(String var1);

   public abstract Team.EnumVisible getNameTagVisibility();

   public abstract boolean getAllowFriendlyFire();

   public static enum EnumVisible {
      private static Map field_178828_g = Maps.newHashMap();
      HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3),
      NEVER("never", 1),
      ALWAYS("always", 0);

      public final int field_178827_f;
      private static final Team.EnumVisible[] ENUM$VALUES = new Team.EnumVisible[]{ALWAYS, NEVER, HIDE_FOR_OTHER_TEAMS, HIDE_FOR_OWN_TEAM};
      public final String field_178830_e;
      HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2);

      public static Team.EnumVisible func_178824_a(String var0) {
         return (Team.EnumVisible)field_178828_g.get(var0);
      }

      public static String[] func_178825_a() {
         return (String[])field_178828_g.keySet().toArray(new String[field_178828_g.size()]);
      }

      static {
         Team.EnumVisible[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            Team.EnumVisible var0 = var3[var1];
            field_178828_g.put(var0.field_178830_e, var0);
         }

      }

      private EnumVisible(String var3, int var4) {
         this.field_178830_e = var3;
         this.field_178827_f = var4;
      }
   }
}
