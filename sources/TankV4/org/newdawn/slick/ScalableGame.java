package org.newdawn.slick;

import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class ScalableGame implements Game {
   private static SGL GL = Renderer.get();
   private float normalWidth;
   private float normalHeight;
   private Game held;
   private boolean maintainAspect;
   private int targetWidth;
   private int targetHeight;
   private GameContainer container;

   public ScalableGame(Game var1, int var2, int var3) {
      this(var1, var2, var3, false);
   }

   public ScalableGame(Game var1, int var2, int var3, boolean var4) {
      this.held = var1;
      this.normalWidth = (float)var2;
      this.normalHeight = (float)var3;
      this.maintainAspect = var4;
   }

   public void init(GameContainer var1) throws SlickException {
      this.container = var1;
      this.recalculateScale();
      this.held.init(var1);
   }

   public void recalculateScale() throws SlickException {
      this.targetWidth = this.container.getWidth();
      this.targetHeight = this.container.getHeight();
      if (this.maintainAspect) {
         boolean var1 = (double)(this.normalWidth / this.normalHeight) > 1.6D;
         boolean var2 = (double)((float)this.targetWidth / (float)this.targetHeight) > 1.6D;
         float var3 = (float)this.targetWidth / this.normalWidth;
         float var4 = (float)this.targetHeight / this.normalHeight;
         float var5;
         if (var1 & var2) {
            var5 = var3 < var4 ? var3 : var4;
            this.targetWidth = (int)(this.normalWidth * var5);
            this.targetHeight = (int)(this.normalHeight * var5);
         } else if (var1 & !var2) {
            this.targetWidth = (int)(this.normalWidth * var3);
            this.targetHeight = (int)(this.normalHeight * var3);
         } else if (!var1 & var2) {
            this.targetWidth = (int)(this.normalWidth * var4);
            this.targetHeight = (int)(this.normalHeight * var4);
         } else {
            var5 = var3 < var4 ? var3 : var4;
            this.targetWidth = (int)(this.normalWidth * var5);
            this.targetHeight = (int)(this.normalHeight * var5);
         }
      }

      if (this.held instanceof InputListener) {
         this.container.getInput().addListener((InputListener)this.held);
      }

      this.container.getInput().setScale(this.normalWidth / (float)this.targetWidth, this.normalHeight / (float)this.targetHeight);
      int var6 = 0;
      int var7 = 0;
      if (this.targetHeight < this.container.getHeight()) {
         var6 = (this.container.getHeight() - this.targetHeight) / 2;
      }

      if (this.targetWidth < this.container.getWidth()) {
         var7 = (this.container.getWidth() - this.targetWidth) / 2;
      }

      this.container.getInput().setOffset((float)(-var7) / ((float)this.targetWidth / this.normalWidth), (float)(-var6) / ((float)this.targetHeight / this.normalHeight));
   }

   public void update(GameContainer var1, int var2) throws SlickException {
      if (this.targetHeight != var1.getHeight() || this.targetWidth != var1.getWidth()) {
         this.recalculateScale();
      }

      this.held.update(var1, var2);
   }

   public final void render(GameContainer var1, Graphics var2) throws SlickException {
      int var3 = 0;
      int var4 = 0;
      if (this.targetHeight < var1.getHeight()) {
         var3 = (var1.getHeight() - this.targetHeight) / 2;
      }

      if (this.targetWidth < var1.getWidth()) {
         var4 = (var1.getWidth() - this.targetWidth) / 2;
      }

      SlickCallable.enterSafeBlock();
      var2.setClip(var4, var3, this.targetWidth, this.targetHeight);
      GL.glTranslatef((float)var4, (float)var3, 0.0F);
      GL.glScalef((float)this.targetWidth / this.normalWidth, (float)this.targetHeight / this.normalHeight, 0.0F);
      GL.glPushMatrix();
      this.held.render(var1, var2);
      GL.glPopMatrix();
      var2.clearClip();
      SlickCallable.leaveSafeBlock();
      this.renderOverlay(var1, var2);
   }

   protected void renderOverlay(GameContainer var1, Graphics var2) {
   }

   public boolean closeRequested() {
      return this.held.closeRequested();
   }

   public String getTitle() {
      return this.held.getTitle();
   }
}
