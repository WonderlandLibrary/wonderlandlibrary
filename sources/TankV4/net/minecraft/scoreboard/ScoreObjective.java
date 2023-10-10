package net.minecraft.scoreboard;

public class ScoreObjective {
   private final String name;
   private final IScoreObjectiveCriteria objectiveCriteria;
   private final Scoreboard theScoreboard;
   private IScoreObjectiveCriteria.EnumRenderType renderType;
   private String displayName;

   public String getDisplayName() {
      return this.displayName;
   }

   public Scoreboard getScoreboard() {
      return this.theScoreboard;
   }

   public void setRenderType(IScoreObjectiveCriteria.EnumRenderType var1) {
      this.renderType = var1;
      this.theScoreboard.func_96532_b(this);
   }

   public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
      return this.renderType;
   }

   public String getName() {
      return this.name;
   }

   public IScoreObjectiveCriteria getCriteria() {
      return this.objectiveCriteria;
   }

   public void setDisplayName(String var1) {
      this.displayName = var1;
      this.theScoreboard.func_96532_b(this);
   }

   public ScoreObjective(Scoreboard var1, String var2, IScoreObjectiveCriteria var3) {
      this.theScoreboard = var1;
      this.name = var2;
      this.objectiveCriteria = var3;
      this.displayName = var2;
      this.renderType = var3.getRenderType();
   }
}
