package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class HorizontalSplitTransition implements Transition {
   protected static SGL GL = Renderer.get();
   private GameState prev;
   private float offset;
   private boolean finish;
   private Color background;

   public HorizontalSplitTransition() {
   }

   public HorizontalSplitTransition(Color var1) {
      this.background = var1;
   }

   public void init(GameState var1, GameState var2) {
      this.prev = var2;
   }

   public boolean isComplete() {
      return this.finish;
   }

   public void postRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
      var3.translate(-this.offset, 0.0F);
      var3.setClip((int)(-this.offset), 0, var2.getWidth() / 2, var2.getHeight());
      Color var4;
      if (this.background != null) {
         var4 = var3.getColor();
         var3.setColor(this.background);
         var3.fillRect(0.0F, 0.0F, (float)var2.getWidth(), (float)var2.getHeight());
         var3.setColor(var4);
      }

      GL.glPushMatrix();
      this.prev.render(var2, var1, var3);
      GL.glPopMatrix();
      var3.clearClip();
      var3.translate(this.offset * 2.0F, 0.0F);
      var3.setClip((int)((float)(var2.getWidth() / 2) + this.offset), 0, var2.getWidth() / 2, var2.getHeight());
      if (this.background != null) {
         var4 = var3.getColor();
         var3.setColor(this.background);
         var3.fillRect(0.0F, 0.0F, (float)var2.getWidth(), (float)var2.getHeight());
         var3.setColor(var4);
      }

      GL.glPushMatrix();
      this.prev.render(var2, var1, var3);
      GL.glPopMatrix();
      var3.clearClip();
      var3.translate(-this.offset, 0.0F);
   }

   public void preRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
   }

   public void update(StateBasedGame var1, GameContainer var2, int var3) throws SlickException {
      this.offset += (float)var3 * 1.0F;
      if (this.offset > (float)(var2.getWidth() / 2)) {
         this.finish = true;
      }

   }
}
