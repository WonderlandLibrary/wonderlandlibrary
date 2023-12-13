package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;

public class EffectFilter {
   private Effect effect;
   private NodeEffectInput nodeInput;

   EffectFilter(Effect var1, NGNode var2) {
      this.effect = var1;
      this.nodeInput = new NodeEffectInput(var2);
   }

   Effect getEffect() {
      return this.effect;
   }

   NodeEffectInput getNodeInput() {
      return this.nodeInput;
   }

   void dispose() {
      this.effect = null;
      this.nodeInput.setNode((NGNode)null);
      this.nodeInput = null;
   }

   BaseBounds getBounds(BaseBounds var1, BaseTransform var2) {
      BaseBounds var3 = this.getEffect().getBounds(var2, this.nodeInput);
      return var1.deriveWithNewBounds(var3);
   }

   void render(Graphics var1) {
      NodeEffectInput var2 = this.getNodeInput();
      PrEffectHelper.render(this.getEffect(), var1, 0.0F, 0.0F, var2);
      var2.flush();
   }
}
