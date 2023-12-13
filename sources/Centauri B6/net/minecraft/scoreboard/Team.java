package net.minecraft.scoreboard;

import java.util.Collection;
import net.minecraft.scoreboard.Team.EnumVisible;

public abstract class Team {
   public abstract String formatString(String var1);

   public abstract boolean getAllowFriendlyFire();

   public abstract boolean getSeeFriendlyInvisiblesEnabled();

   public boolean isSameTeam(Team other) {
      return other == null?false:this == other;
   }

   public abstract Collection getMembershipCollection();

   public abstract String getRegisteredName();

   public abstract EnumVisible getNameTagVisibility();

   public abstract EnumVisible getDeathMessageVisibility();
}
