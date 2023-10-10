package net.minecraft.scoreboard;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import net.minecraft.util.EnumChatFormatting;

public class ScorePlayerTeam extends Team {
   private String teamNameSPT;
   private final Set membershipSet = Sets.newHashSet();
   private String colorSuffix = "";
   private boolean allowFriendlyFire = true;
   private final Scoreboard theScoreboard;
   private Team.EnumVisible deathMessageVisibility;
   private EnumChatFormatting chatFormat;
   private boolean canSeeFriendlyInvisibles = true;
   private String namePrefixSPT = "";
   private final String registeredName;
   private Team.EnumVisible nameTagVisibility;

   public void setTeamName(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Name cannot be null");
      } else {
         this.teamNameSPT = var1;
         this.theScoreboard.sendTeamUpdate(this);
      }
   }

   public ScorePlayerTeam(Scoreboard var1, String var2) {
      this.nameTagVisibility = Team.EnumVisible.ALWAYS;
      this.deathMessageVisibility = Team.EnumVisible.ALWAYS;
      this.chatFormat = EnumChatFormatting.RESET;
      this.theScoreboard = var1;
      this.registeredName = var2;
      this.teamNameSPT = var2;
   }

   public void func_98298_a(int var1) {
      this.setAllowFriendlyFire((var1 & 1) > 0);
      this.setSeeFriendlyInvisiblesEnabled((var1 & 2) > 0);
   }

   public void setAllowFriendlyFire(boolean var1) {
      this.allowFriendlyFire = var1;
      this.theScoreboard.sendTeamUpdate(this);
   }

   public Team.EnumVisible getDeathMessageVisibility() {
      return this.deathMessageVisibility;
   }

   public String getTeamName() {
      return this.teamNameSPT;
   }

   public int func_98299_i() {
      int var1 = 0;
      if (this.getAllowFriendlyFire()) {
         var1 |= 1;
      }

      if (this.getSeeFriendlyInvisiblesEnabled()) {
         var1 |= 2;
      }

      return var1;
   }

   public void setNameTagVisibility(Team.EnumVisible var1) {
      this.nameTagVisibility = var1;
      this.theScoreboard.sendTeamUpdate(this);
   }

   public EnumChatFormatting getChatFormat() {
      return this.chatFormat;
   }

   public String formatString(String var1) {
      return this.getColorPrefix() + var1 + this.getColorSuffix();
   }

   public void setSeeFriendlyInvisiblesEnabled(boolean var1) {
      this.canSeeFriendlyInvisibles = var1;
      this.theScoreboard.sendTeamUpdate(this);
   }

   public String getColorPrefix() {
      return this.namePrefixSPT;
   }

   public String getColorSuffix() {
      return this.colorSuffix;
   }

   public void setNamePrefix(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Prefix cannot be null");
      } else {
         this.namePrefixSPT = var1;
         this.theScoreboard.sendTeamUpdate(this);
      }
   }

   public Team.EnumVisible getNameTagVisibility() {
      return this.nameTagVisibility;
   }

   public static String formatPlayerName(Team var0, String var1) {
      return var0 == null ? var1 : var0.formatString(var1);
   }

   public void setDeathMessageVisibility(Team.EnumVisible var1) {
      this.deathMessageVisibility = var1;
      this.theScoreboard.sendTeamUpdate(this);
   }

   public boolean getAllowFriendlyFire() {
      return this.allowFriendlyFire;
   }

   public Collection getMembershipCollection() {
      return this.membershipSet;
   }

   public boolean getSeeFriendlyInvisiblesEnabled() {
      return this.canSeeFriendlyInvisibles;
   }

   public void setNameSuffix(String var1) {
      this.colorSuffix = var1;
      this.theScoreboard.sendTeamUpdate(this);
   }

   public String getRegisteredName() {
      return this.registeredName;
   }

   public void setChatFormat(EnumChatFormatting var1) {
      this.chatFormat = var1;
   }
}
