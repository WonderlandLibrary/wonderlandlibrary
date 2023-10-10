package org.newdawn.slick;

import java.io.InputStream;
import org.newdawn.slick.opengl.Texture;

public class SpriteSheet extends Image {
   private int tw;
   private int th;
   private int margin;
   private Image[][] subImages;
   private int spacing;
   private Image target;

   public SpriteSheet(Image var1, int var2, int var3) {
      super(var1);
      this.margin = 0;
      this.target = var1;
      this.tw = var2;
      this.th = var3;
      this.initImpl();
   }

   public SpriteSheet(Image var1, int var2, int var3, int var4, int var5) {
      super(var1);
      this.margin = 0;
      this.target = var1;
      this.tw = var2;
      this.th = var3;
      this.spacing = var4;
      this.margin = var5;
      this.initImpl();
   }

   public SpriteSheet(Image var1, int var2, int var3, int var4) {
      this(var1, var2, var3, var4, 0);
   }

   public SpriteSheet(String var1, int var2, int var3, int var4) throws SlickException {
      this(var1, var2, var3, (Color)null, var4);
   }

   public SpriteSheet(String var1, int var2, int var3) throws SlickException {
      this(var1, var2, var3, (Color)null);
   }

   public SpriteSheet(String var1, int var2, int var3, Color var4) throws SlickException {
      this(var1, var2, var3, var4, 0);
   }

   public SpriteSheet(String var1, int var2, int var3, Color var4, int var5) throws SlickException {
      super(var1, false, 2, var4);
      this.margin = 0;
      this.target = this;
      this.tw = var2;
      this.th = var3;
      this.spacing = var5;
   }

   public SpriteSheet(String var1, InputStream var2, int var3, int var4) throws SlickException {
      super(var2, var1, false);
      this.margin = 0;
      this.target = this;
      this.tw = var3;
      this.th = var4;
   }

   protected void initImpl() {
      if (this.subImages == null) {
         int var1 = (this.getWidth() - this.margin * 2 - this.tw) / (this.tw + this.spacing) + 1;
         int var2 = (this.getHeight() - this.margin * 2 - this.th) / (this.th + this.spacing) + 1;
         if ((this.getHeight() - this.th) % (this.th + this.spacing) != 0) {
            ++var2;
         }

         this.subImages = new Image[var1][var2];

         for(int var3 = 0; var3 < var1; ++var3) {
            for(int var4 = 0; var4 < var2; ++var4) {
               this.subImages[var3][var4] = this.getSprite(var3, var4);
            }
         }

      }
   }

   public Image getSubImage(int var1, int var2) {
      this.init();
      if (var1 >= 0 && var1 < this.subImages.length) {
         if (var2 >= 0 && var2 < this.subImages[0].length) {
            return this.subImages[var1][var2];
         } else {
            throw new RuntimeException("SubImage out of sheet bounds: " + var1 + "," + var2);
         }
      } else {
         throw new RuntimeException("SubImage out of sheet bounds: " + var1 + "," + var2);
      }
   }

   public Image getSprite(int var1, int var2) {
      this.target.init();
      this.initImpl();
      if (var1 >= 0 && var1 < this.subImages.length) {
         if (var2 >= 0 && var2 < this.subImages[0].length) {
            return this.target.getSubImage(var1 * (this.tw + this.spacing) + this.margin, var2 * (this.th + this.spacing) + this.margin, this.tw, this.th);
         } else {
            throw new RuntimeException("SubImage out of sheet bounds: " + var1 + "," + var2);
         }
      } else {
         throw new RuntimeException("SubImage out of sheet bounds: " + var1 + "," + var2);
      }
   }

   public int getHorizontalCount() {
      this.target.init();
      this.initImpl();
      return this.subImages.length;
   }

   public int getVerticalCount() {
      this.target.init();
      this.initImpl();
      return this.subImages[0].length;
   }

   public void renderInUse(int var1, int var2, int var3, int var4) {
      this.subImages[var3][var4].drawEmbedded((float)var1, (float)var2, (float)this.tw, (float)this.th);
   }

   public void endUse() {
      if (this.target == this) {
         super.endUse();
      } else {
         this.target.endUse();
      }
   }

   public void startUse() {
      if (this.target == this) {
         super.startUse();
      } else {
         this.target.startUse();
      }
   }

   public void setTexture(Texture var1) {
      if (this.target == this) {
         super.setTexture(var1);
      } else {
         this.target.setTexture(var1);
      }
   }
}
