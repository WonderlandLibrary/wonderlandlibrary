package net.minecraft.scoreboard;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class ScoreHealthCriteria extends ScoreDummyCriteria {
   public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
      return IScoreObjectiveCriteria.EnumRenderType.HEARTS;
   }

   public int func_96635_a(List var1) {
      float var2 = 0.0F;

      EntityPlayer var3;
      for(Iterator var4 = var1.iterator(); var4.hasNext(); var2 += var3.getHealth() + var3.getAbsorptionAmount()) {
         var3 = (EntityPlayer)var4.next();
      }

      if (var1.size() > 0) {
         var2 /= (float)var1.size();
      }

      return MathHelper.ceiling_float_int(var2);
   }

   public boolean isReadOnly() {
      return true;
   }

   public ScoreHealthCriteria(String var1) {
      super(var1);
   }
}
