package javafx.scene.effect;

import javafx.scene.paint.Color;

/** @deprecated */
@Deprecated
public abstract class LightBuilder {
   private boolean __set;
   private Color color;

   protected LightBuilder() {
   }

   public void applyTo(Light var1) {
      if (this.__set) {
         var1.setColor(this.color);
      }

   }

   public LightBuilder color(Color var1) {
      this.color = var1;
      this.__set = true;
      return this;
   }
}
