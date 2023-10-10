package net.minecraft.scoreboard;

import java.util.List;

public class ScoreDummyCriteria implements IScoreObjectiveCriteria {
   private final String dummyName;

   public ScoreDummyCriteria(String var1) {
      this.dummyName = var1;
      IScoreObjectiveCriteria.INSTANCES.put(var1, this);
   }

   public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
      return IScoreObjectiveCriteria.EnumRenderType.INTEGER;
   }

   public String getName() {
      return this.dummyName;
   }

   public boolean isReadOnly() {
      return false;
   }

   public int func_96635_a(List var1) {
      return 0;
   }
}
