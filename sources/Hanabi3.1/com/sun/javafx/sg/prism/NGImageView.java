package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.RectBounds;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CachingCompoundImage;
import com.sun.prism.image.CompoundCoords;
import com.sun.prism.image.Coords;
import com.sun.prism.image.ViewPort;

public class NGImageView extends NGNode {
   private Image image;
   private CachingCompoundImage compoundImage;
   private CompoundCoords compoundCoords;
   private float x;
   private float y;
   private float w;
   private float h;
   private Coords coords;
   private ViewPort reqviewport;
   private ViewPort imgviewport;
   private boolean renderable = false;
   private boolean coordsOK = false;
   static final int MAX_SIZE_OVERRIDE = 0;

   private void invalidate() {
      this.coordsOK = false;
      this.coords = null;
      this.compoundCoords = null;
      this.imgviewport = null;
      this.geometryChanged();
   }

   public void setViewport(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (var3 > 0.0F && var4 > 0.0F) {
         this.reqviewport = new ViewPort(var1, var2, var3, var4);
      } else {
         this.reqviewport = null;
      }

      this.w = var5;
      this.h = var6;
      this.invalidate();
   }

   private void calculatePositionAndClipping() {
      this.renderable = false;
      this.coordsOK = true;
      if (this.reqviewport != null && this.image != null) {
         float var1 = (float)this.image.getWidth();
         float var2 = (float)this.image.getHeight();
         if (var1 != 0.0F && var2 != 0.0F) {
            this.imgviewport = this.reqviewport.getScaledVersion(this.image.getPixelScale());
            this.coords = this.imgviewport.getClippedCoords(var1, var2, this.w, this.h);
            this.renderable = this.coords != null;
         }
      } else {
         this.renderable = this.image != null;
      }
   }

   protected void doRender(Graphics var1) {
      if (!this.coordsOK) {
         this.calculatePositionAndClipping();
      }

      if (this.renderable) {
         super.doRender(var1);
      }

   }

   private int maxSizeWrapper(ResourceFactory var1) {
      return var1.getMaximumTextureSize();
   }

   protected void renderContent(Graphics var1) {
      int var2 = this.image.getWidth();
      int var3 = this.image.getHeight();
      ResourceFactory var4 = var1.getResourceFactory();
      int var5 = this.maxSizeWrapper(var4);
      if (var2 <= var5 && var3 <= var5) {
         Texture var6 = var4.getCachedTexture(this.image, Texture.WrapMode.CLAMP_TO_EDGE);
         if (this.coords == null) {
            var1.drawTexture(var6, this.x, this.y, this.x + this.w, this.y + this.h, 0.0F, 0.0F, (float)var2, (float)var3);
         } else {
            this.coords.draw(var6, var1, this.x, this.y);
         }

         var6.unlock();
      } else {
         if (this.compoundImage == null) {
            this.compoundImage = new CachingCompoundImage(this.image, var5);
         }

         if (this.coords == null) {
            this.coords = new Coords(this.w, this.h, new ViewPort(0.0F, 0.0F, (float)var2, (float)var3));
         }

         if (this.compoundCoords == null) {
            this.compoundCoords = new CompoundCoords(this.compoundImage, this.coords);
         }

         this.compoundCoords.draw(var1, this.compoundImage, this.x, this.y);
      }

   }

   protected boolean hasOverlappingContents() {
      return false;
   }

   public void setImage(Object var1) {
      Image var2 = (Image)var1;
      if (this.image != var2) {
         boolean var3 = var2 == null || this.image == null || this.image.getPixelScale() != var2.getPixelScale() || this.image.getHeight() != var2.getHeight() || this.image.getWidth() != var2.getWidth();
         this.image = var2;
         this.compoundImage = null;
         if (var3) {
            this.invalidate();
         }

      }
   }

   public void setX(float var1) {
      if (this.x != var1) {
         this.x = var1;
         this.geometryChanged();
      }

   }

   public void setY(float var1) {
      if (this.y != var1) {
         this.y = var1;
         this.geometryChanged();
      }

   }

   public void setSmooth(boolean var1) {
   }

   protected boolean supportsOpaqueRegions() {
      return true;
   }

   protected boolean hasOpaqueRegion() {
      assert this.image == null || this.image.getWidth() >= 1 && this.image.getHeight() >= 1;

      return super.hasOpaqueRegion() && this.w >= 1.0F && this.h >= 1.0F && this.image != null && this.image.isOpaque();
   }

   protected RectBounds computeOpaqueRegion(RectBounds var1) {
      return (RectBounds)var1.deriveWithNewBounds(this.x, this.y, 0.0F, this.x + this.w, this.y + this.h, 0.0F);
   }
}
