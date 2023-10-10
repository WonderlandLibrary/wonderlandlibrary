package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class RotateTransition implements Transition {
   private GameState prev;
   private float ang;
   private boolean finish;
   private float scale = 1.0F;
   private Color background;

   public RotateTransition() {
   }

   public RotateTransition(Color var1) {
      this.background = var1;
   }

   public void init(GameState var1, GameState var2) {
      this.prev = var2;
   }

   public boolean isComplete() {
      return this.finish;
   }

   public void postRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
      var3.translate((float)(var2.getWidth() / 2), (float)(var2.getHeight() / 2));
      var3.scale(this.scale, this.scale);
      var3.rotate(0.0F, 0.0F, this.ang);
      var3.translate((float)(-var2.getWidth() / 2), (float)(-var2.getHeight() / 2));
      if (this.background != null) {
         Color var4 = var3.getColor();
         var3.setColor(this.background);
         var3.fillRect(0.0F, 0.0F, (float)var2.getWidth(), (float)var2.getHeight());
         var3.setColor(var4);
      }

      this.prev.render(var2, var1, var3);
      var3.translate((float)(var2.getWidth() / 2), (float)(var2.getHeight() / 2));
      var3.rotate(0.0F, 0.0F, -this.ang);
      var3.scale(1.0F / this.scale, 1.0F / this.scale);
      var3.translate((float)(-var2.getWidth() / 2), (float)(-var2.getHeight() / 2));
   }

   public void preRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
   }

   public void update(StateBasedGame var1, GameContainer var2, int var3) throws SlickException {
      this.ang += (float)var3 * 0.5F;
      if (this.ang > 500.0F) {
         this.finish = true;
      }

      this.scale -= (float)var3 * 0.001F;
      if (this.scale < 0.0F) {
         this.scale = 0.0F;
      }

   }
}
