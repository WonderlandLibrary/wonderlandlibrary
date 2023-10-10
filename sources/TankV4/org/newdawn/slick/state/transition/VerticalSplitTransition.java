package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class VerticalSplitTransition implements Transition {
   protected static SGL GL = Renderer.get();
   private GameState prev;
   private float offset;
   private boolean finish;
   private Color background;

   public VerticalSplitTransition() {
   }

   public VerticalSplitTransition(Color var1) {
      this.background = var1;
   }

   public void init(GameState var1, GameState var2) {
      this.prev = var2;
   }

   public boolean isComplete() {
      return this.finish;
   }

   public void postRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
      var3.translate(0.0F, -this.offset);
      var3.setClip(0, (int)(-this.offset), var2.getWidth(), var2.getHeight() / 2);
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
      var3.resetTransform();
      var3.translate(0.0F, this.offset);
      var3.setClip(0, (int)((float)(var2.getHeight() / 2) + this.offset), var2.getWidth(), var2.getHeight() / 2);
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
      var3.translate(0.0F, -this.offset);
   }

   public void preRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
   }

   public void update(StateBasedGame var1, GameContainer var2, int var3) throws SlickException {
      this.offset += (float)var3 * 1.0F;
      if (this.offset > (float)(var2.getHeight() / 2)) {
         this.finish = true;
      }

   }
}
