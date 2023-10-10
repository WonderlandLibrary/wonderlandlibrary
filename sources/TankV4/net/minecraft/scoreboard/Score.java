package net.minecraft.scoreboard;

import java.util.Comparator;
import java.util.List;

public class Score {
   private int scorePoints;
   private boolean field_178818_g;
   private final Scoreboard theScoreboard;
   public static final Comparator scoreComparator = new Comparator() {
      public int compare(Object var1, Object var2) {
         return this.compare((Score)var1, (Score)var2);
      }

      public int compare(Score var1, Score var2) {
         return var1.getScorePoints() > var2.getScorePoints() ? 1 : (var1.getScorePoints() < var2.getScorePoints() ? -1 : var2.getPlayerName().compareToIgnoreCase(var1.getPlayerName()));
      }
   };
   private final String scorePlayerName;
   private boolean locked;
   private final ScoreObjective theScoreObjective;

   public ScoreObjective getObjective() {
      return this.theScoreObjective;
   }

   public void increseScore(int var1) {
      if (this.theScoreObjective.getCriteria().isReadOnly()) {
         throw new IllegalStateException("Cannot modify read-only score");
      } else {
         this.setScorePoints(this.getScorePoints() + var1);
      }
   }

   public void decreaseScore(int var1) {
      if (this.theScoreObjective.getCriteria().isReadOnly()) {
         throw new IllegalStateException("Cannot modify read-only score");
      } else {
         this.setScorePoints(this.getScorePoints() - var1);
      }
   }

   public int getScorePoints() {
      return this.scorePoints;
   }

   public void func_96648_a() {
      if (this.theScoreObjective.getCriteria().isReadOnly()) {
         throw new IllegalStateException("Cannot modify read-only score");
      } else {
         this.increseScore(1);
      }
   }

   public void setScorePoints(int var1) {
      int var2 = this.scorePoints;
      this.scorePoints = var1;
      if (var2 != var1 || this.field_178818_g) {
         this.field_178818_g = false;
         this.getScoreScoreboard().func_96536_a(this);
      }

   }

   public void func_96651_a(List var1) {
      this.setScorePoints(this.theScoreObjective.getCriteria().func_96635_a(var1));
   }

   public boolean isLocked() {
      return this.locked;
   }

   public void setLocked(boolean var1) {
      this.locked = var1;
   }

   public Score(Scoreboard var1, ScoreObjective var2, String var3) {
      this.theScoreboard = var1;
      this.theScoreObjective = var2;
      this.scorePlayerName = var3;
      this.field_178818_g = true;
   }

   public String getPlayerName() {
      return this.scorePlayerName;
   }

   public Scoreboard getScoreScoreboard() {
      return this.theScoreboard;
   }
}
