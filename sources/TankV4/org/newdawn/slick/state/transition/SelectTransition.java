package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class SelectTransition implements Transition {
   protected static SGL GL = Renderer.get();
   private GameState prev;
   private boolean finish;
   private Color background;
   private float scale1 = 1.0F;
   private float xp1 = 0.0F;
   private float yp1 = 0.0F;
   private float scale2 = 0.4F;
   private float xp2 = 0.0F;
   private float yp2 = 0.0F;
   private boolean init = false;
   private boolean moveBackDone = false;
   private int pause = 300;

   public SelectTransition() {
   }

   public SelectTransition(Color var1) {
      this.background = var1;
   }

   public void init(GameState var1, GameState var2) {
      this.prev = var2;
   }

   public boolean isComplete() {
      return this.finish;
   }

   public void postRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
      var3.resetTransform();
      if (!this.moveBackDone) {
         var3.translate(this.xp1, this.yp1);
         var3.scale(this.scale1, this.scale1);
         var3.setClip((int)this.xp1, (int)this.yp1, (int)(this.scale1 * (float)var2.getWidth()), (int)(this.scale1 * (float)var2.getHeight()));
         this.prev.render(var2, var1, var3);
         var3.resetTransform();
         var3.clearClip();
      }

   }

   public void preRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
      if (this.moveBackDone) {
         var3.translate(this.xp1, this.yp1);
         var3.scale(this.scale1, this.scale1);
         var3.setClip((int)this.xp1, (int)this.yp1, (int)(this.scale1 * (float)var2.getWidth()), (int)(this.scale1 * (float)var2.getHeight()));
         this.prev.render(var2, var1, var3);
         var3.resetTransform();
         var3.clearClip();
      }

      var3.translate(this.xp2, this.yp2);
      var3.scale(this.scale2, this.scale2);
      var3.setClip((int)this.xp2, (int)this.yp2, (int)(this.scale2 * (float)var2.getWidth()), (int)(this.scale2 * (float)var2.getHeight()));
   }

   public void update(StateBasedGame var1, GameContainer var2, int var3) throws SlickException {
      if (!this.init) {
         this.init = true;
         this.xp2 = (float)(var2.getWidth() / 2 + 50);
         this.yp2 = (float)(var2.getHeight() / 4);
      }

      if (!this.moveBackDone) {
         if (this.scale1 > 0.4F) {
            this.scale1 -= (float)var3 * 0.002F;
            if (this.scale1 <= 0.4F) {
               this.scale1 = 0.4F;
            }

            this.xp1 += (float)var3 * 0.3F;
            if (this.xp1 > 50.0F) {
               this.xp1 = 50.0F;
            }

            this.yp1 += (float)var3 * 0.5F;
            if (this.yp1 > (float)(var2.getHeight() / 4)) {
               this.yp1 = (float)(var2.getHeight() / 4);
            }
         } else {
            this.moveBackDone = true;
         }
      } else {
         this.pause -= var3;
         if (this.pause > 0) {
            return;
         }

         if (this.scale2 < 1.0F) {
            this.scale2 += (float)var3 * 0.002F;
            if (this.scale2 >= 1.0F) {
               this.scale2 = 1.0F;
            }

            this.xp2 -= (float)var3 * 1.5F;
            if (this.xp2 < 0.0F) {
               this.xp2 = 0.0F;
            }

            this.yp2 -= (float)var3 * 0.5F;
            if (this.yp2 < 0.0F) {
               this.yp2 = 0.0F;
            }
         } else {
            this.finish = true;
         }
      }

   }
}
