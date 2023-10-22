package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Image;

public final class ImagePattern extends Paint {
   private final Image image;
   private final float x;
   private final float y;
   private final float width;
   private final float height;
   private final BaseTransform patternTransform;

   public ImagePattern(Image var1, float var2, float var3, float var4, float var5, BaseTransform var6, boolean var7, boolean var8) {
      super(Paint.Type.IMAGE_PATTERN, var7, var8);
      if (var1 == null) {
         throw new IllegalArgumentException("Image must be non-null");
      } else {
         this.image = var1;
         this.x = var2;
         this.y = var3;
         this.width = var4;
         this.height = var5;
         if (var6 != null) {
            this.patternTransform = var6.copy();
         } else {
            this.patternTransform = BaseTransform.IDENTITY_TRANSFORM;
         }

      }
   }

   public ImagePattern(Image var1, float var2, float var3, float var4, float var5, boolean var6, boolean var7) {
      this(var1, var2, var3, var4, var5, (BaseTransform)null, var6, var7);
   }

   public Image getImage() {
      return this.image;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   public BaseTransform getPatternTransformNoClone() {
      return this.patternTransform;
   }

   public boolean isOpaque() {
      return this.image.isOpaque();
   }
}
