package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class FadeOutTransition implements Transition {
   private Color color;
   private int fadeTime;

   public FadeOutTransition() {
      this(Color.black, 500);
   }

   public FadeOutTransition(Color var1) {
      this(var1, 500);
   }

   public FadeOutTransition(Color var1, int var2) {
      this.color = new Color(var1);
      this.color.a = 0.0F;
      this.fadeTime = var2;
   }

   public boolean isComplete() {
      return this.color.a >= 1.0F;
   }

   public void postRender(StateBasedGame var1, GameContainer var2, Graphics var3) {
      Color var4 = var3.getColor();
      var3.setColor(this.color);
      var3.fillRect(0.0F, 0.0F, (float)var2.getWidth(), (float)var2.getHeight());
      var3.setColor(var4);
   }

   public void update(StateBasedGame var1, GameContainer var2, int var3) {
      Color var10000 = this.color;
      var10000.a += (float)var3 * (1.0F / (float)this.fadeTime);
      if (this.color.a > 1.0F) {
         this.color.a = 1.0F;
      }

   }

   public void preRender(StateBasedGame var1, GameContainer var2, Graphics var3) {
   }

   public void init(GameState var1, GameState var2) {
   }
}
