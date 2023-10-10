package net.minecraft.scoreboard;

import java.util.List;
import net.minecraft.util.EnumChatFormatting;

public class GoalColor implements IScoreObjectiveCriteria {
   private final String goalName;

   public boolean isReadOnly() {
      return false;
   }

   public int func_96635_a(List var1) {
      return 0;
   }

   public GoalColor(String var1, EnumChatFormatting var2) {
      this.goalName = var1 + var2.getFriendlyName();
      IScoreObjectiveCriteria.INSTANCES.put(this.goalName, this);
   }

   public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
      return IScoreObjectiveCriteria.EnumRenderType.INTEGER;
   }

   public String getName() {
      return this.goalName;
   }
}
