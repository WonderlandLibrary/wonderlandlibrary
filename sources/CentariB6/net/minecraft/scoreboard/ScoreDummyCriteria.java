package net.minecraft.scoreboard;

import java.util.List;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.IScoreObjectiveCriteria.EnumRenderType;

public class ScoreDummyCriteria implements IScoreObjectiveCriteria {
   private final String dummyName;

   public ScoreDummyCriteria(String name) {
      this.dummyName = name;
      IScoreObjectiveCriteria.INSTANCES.put(name, this);
   }

   public String getName() {
      return this.dummyName;
   }

   public boolean isReadOnly() {
      return false;
   }

   public EnumRenderType getRenderType() {
      return EnumRenderType.INTEGER;
   }

   public int func_96635_a(List p_96635_1_) {
      return 0;
   }
}
