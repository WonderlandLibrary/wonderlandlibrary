package org.newdawn.slick.state.transition;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.MaskUtil;

public class BlobbyTransition implements Transition {
   protected static SGL GL = Renderer.get();
   private GameState prev;
   private boolean finish;
   private Color background;
   private ArrayList blobs = new ArrayList();
   private int timer = 1000;
   private int blobCount = 10;

   public BlobbyTransition() {
   }

   public BlobbyTransition(Color var1) {
      this.background = var1;
   }

   public void init(GameState var1, GameState var2) {
      this.prev = var2;
   }

   public boolean isComplete() {
      return this.finish;
   }

   public void postRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
      MaskUtil.resetMask();
   }

   public void preRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
      this.prev.render(var2, var1, var3);
      MaskUtil.defineMask();

      for(int var4 = 0; var4 < this.blobs.size(); ++var4) {
         ((BlobbyTransition.Blob)this.blobs.get(var4)).render(var3);
      }

      MaskUtil.finishDefineMask();
      MaskUtil.drawOnMask();
      if (this.background != null) {
         Color var5 = var3.getColor();
         var3.setColor(this.background);
         var3.fillRect(0.0F, 0.0F, (float)var2.getWidth(), (float)var2.getHeight());
         var3.setColor(var5);
      }

   }

   public void update(StateBasedGame var1, GameContainer var2, int var3) throws SlickException {
      int var4;
      if (this.blobs.size() == 0) {
         for(var4 = 0; var4 < this.blobCount; ++var4) {
            this.blobs.add(new BlobbyTransition.Blob(this, var2));
         }
      }

      for(var4 = 0; var4 < this.blobs.size(); ++var4) {
         ((BlobbyTransition.Blob)this.blobs.get(var4)).update(var3);
      }

      this.timer -= var3;
      if (this.timer < 0) {
         this.finish = true;
      }

   }

   private class Blob {
      private float x;
      private float y;
      private float growSpeed;
      private float rad;
      private final BlobbyTransition this$0;

      public Blob(BlobbyTransition var1, GameContainer var2) {
         this.this$0 = var1;
         this.x = (float)(Math.random() * (double)var2.getWidth());
         this.y = (float)(Math.random() * (double)var2.getWidth());
         this.growSpeed = (float)(1.0D + Math.random() * 1.0D);
      }

      public void update(int var1) {
         this.rad += this.growSpeed * (float)var1 * 0.4F;
      }

      public void render(Graphics var1) {
         var1.fillOval(this.x - this.rad, this.y - this.rad, this.rad * 2.0F, this.rad * 2.0F);
      }
   }
}
