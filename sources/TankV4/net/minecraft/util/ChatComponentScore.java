package net.minecraft.util;

import java.util.Iterator;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;

public class ChatComponentScore extends ChatComponentStyle {
   private String value = "";
   private final String name;
   private final String objective;

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ChatComponentScore)) {
         return false;
      } else {
         ChatComponentScore var2 = (ChatComponentScore)var1;
         return this.name.equals(var2.name) && this.objective.equals(var2.objective) && super.equals(var1);
      }
   }

   public String toString() {
      return "ScoreComponent{name='" + this.name + '\'' + "objective='" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
   }

   public String getUnformattedTextForChat() {
      MinecraftServer var1 = MinecraftServer.getServer();
      if (var1 != null && var1.isAnvilFileSet() && StringUtils.isNullOrEmpty(this.value)) {
         Scoreboard var2 = var1.worldServerForDimension(0).getScoreboard();
         ScoreObjective var3 = var2.getObjective(this.objective);
         if (var2.entityHasObjective(this.name, var3)) {
            Score var4 = var2.getValueFromObjective(this.name, var3);
            this.setValue(String.format("%d", var4.getScorePoints()));
         } else {
            this.value = "";
         }
      }

      return this.value;
   }

   public String getObjective() {
      return this.objective;
   }

   public IChatComponent createCopy() {
      return this.createCopy();
   }

   public String getName() {
      return this.name;
   }

   public void setValue(String var1) {
      this.value = var1;
   }

   public ChatComponentScore createCopy() {
      ChatComponentScore var1 = new ChatComponentScore(this.name, this.objective);
      var1.setValue(this.value);
      var1.setChatStyle(this.getChatStyle().createShallowCopy());
      Iterator var3 = this.getSiblings().iterator();

      while(var3.hasNext()) {
         IChatComponent var2 = (IChatComponent)var3.next();
         var1.appendSibling(var2.createCopy());
      }

      return var1;
   }

   public ChatComponentScore(String var1, String var2) {
      this.name = var1;
      this.objective = var2;
   }
}
