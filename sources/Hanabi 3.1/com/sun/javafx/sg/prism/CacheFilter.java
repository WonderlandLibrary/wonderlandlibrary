package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.logging.PulseLogger;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CacheFilter {
   private static final Rectangle TEMP_RECT = new Rectangle();
   private static final DirtyRegionContainer TEMP_CONTAINER = new DirtyRegionContainer(1);
   private static final Affine3D TEMP_CACHEFILTER_TRANSFORM = new Affine3D();
   private static final RectBounds TEMP_BOUNDS = new RectBounds();
   private static final double EPSILON = 1.0E-7;
   private RTTexture tempTexture;
   private double lastXDelta;
   private double lastYDelta;
   private ScrollCacheState scrollCacheState;
   private ImageData cachedImageData;
   private Rectangle cacheBounds;
   private final Affine2D cachedXform;
   private double cachedScaleX;
   private double cachedScaleY;
   private double cachedRotate;
   private double cachedX;
   private double cachedY;
   private NGNode node;
   private final Affine2D screenXform;
   private boolean scaleHint;
   private boolean rotateHint;
   private CacheHint cacheHint;
   private boolean wasUnsupported;

   private Rectangle computeDirtyRegionForTranslate() {
      if (this.lastXDelta != 0.0) {
         if (this.lastXDelta > 0.0) {
            TEMP_RECT.setBounds(0, 0, (int)this.lastXDelta, this.cacheBounds.height);
         } else {
            TEMP_RECT.setBounds(this.cacheBounds.width + (int)this.lastXDelta, 0, -((int)this.lastXDelta), this.cacheBounds.height);
         }
      } else if (this.lastYDelta > 0.0) {
         TEMP_RECT.setBounds(0, 0, this.cacheBounds.width, (int)this.lastYDelta);
      } else {
         TEMP_RECT.setBounds(0, this.cacheBounds.height + (int)this.lastYDelta, this.cacheBounds.width, -((int)this.lastYDelta));
      }

      return TEMP_RECT;
   }

   protected CacheFilter(NGNode var1, CacheHint var2) {
      this.scrollCacheState = CacheFilter.ScrollCacheState.CHECKING_PRECONDITIONS;
      this.cacheBounds = new Rectangle();
      this.cachedXform = new Affine2D();
      this.screenXform = new Affine2D();
      this.wasUnsupported = false;
      this.node = var1;
      this.scrollCacheState = CacheFilter.ScrollCacheState.CHECKING_PRECONDITIONS;
      this.setHint(var2);
   }

   public void setHint(CacheHint var1) {
      this.cacheHint = var1;
      this.scaleHint = var1 == CacheHint.SPEED || var1 == CacheHint.SCALE || var1 == CacheHint.SCALE_AND_ROTATE;
      this.rotateHint = var1 == CacheHint.SPEED || var1 == CacheHint.ROTATE || var1 == CacheHint.SCALE_AND_ROTATE;
   }

   final boolean isScaleHint() {
      return this.scaleHint;
   }

   final boolean isRotateHint() {
      return this.rotateHint;
   }

   boolean matchesHint(CacheHint var1) {
      return this.cacheHint == var1;
   }

   boolean unsupported(double[] var1) {
      double var2 = var1[0];
      double var4 = var1[1];
      double var6 = var1[2];
      return (var6 > 1.0E-7 || var6 < -1.0E-7) && (var2 > var4 + 1.0E-7 || var4 > var2 + 1.0E-7 || var2 < var4 - 1.0E-7 || var4 < var2 - 1.0E-7 || this.cachedScaleX > this.cachedScaleY + 1.0E-7 || this.cachedScaleY > this.cachedScaleX + 1.0E-7 || this.cachedScaleX < this.cachedScaleY - 1.0E-7 || this.cachedScaleY < this.cachedScaleX - 1.0E-7);
   }

   private boolean isXformScrollCacheCapable(double[] var1) {
      if (this.unsupported(var1)) {
         return false;
      } else {
         double var2 = var1[2];
         return this.rotateHint || var2 == 0.0;
      }
   }

   private boolean needToRenderCache(BaseTransform var1, double[] var2, float var3, float var4) {
      if (this.cachedImageData == null) {
         return true;
      } else {
         if (this.lastXDelta != 0.0 || this.lastYDelta != 0.0) {
            label93: {
               if (!(Math.abs(this.lastXDelta) >= (double)this.cacheBounds.width) && !(Math.abs(this.lastYDelta) >= (double)this.cacheBounds.height) && Math.rint(this.lastXDelta) == this.lastXDelta && Math.rint(this.lastYDelta) == this.lastYDelta) {
                  if (this.scrollCacheState != CacheFilter.ScrollCacheState.CHECKING_PRECONDITIONS) {
                     break label93;
                  }

                  if (this.impl_scrollCacheCapable() && this.isXformScrollCacheCapable(var2)) {
                     this.scrollCacheState = CacheFilter.ScrollCacheState.ENABLED;
                     break label93;
                  }

                  this.scrollCacheState = CacheFilter.ScrollCacheState.DISABLED;
                  return true;
               }

               this.node.clearDirtyTree();
               this.lastXDelta = this.lastYDelta = 0.0;
               return true;
            }
         }

         if (this.cachedXform.getMxx() == var1.getMxx() && this.cachedXform.getMyy() == var1.getMyy() && this.cachedXform.getMxy() == var1.getMxy() && this.cachedXform.getMyx() == var1.getMyx()) {
            return false;
         } else if (!this.wasUnsupported && !this.unsupported(var2)) {
            double var5 = var2[0];
            double var7 = var2[1];
            double var9 = var2[2];
            if (this.scaleHint) {
               if (!(this.cachedScaleX < (double)var3) && !(this.cachedScaleY < (double)var4)) {
                  if (this.rotateHint) {
                     return false;
                  } else {
                     return !(this.cachedRotate - 1.0E-7 < var9) || !(var9 < this.cachedRotate + 1.0E-7);
                  }
               } else {
                  return true;
               }
            } else if (this.rotateHint) {
               return !(this.cachedScaleX - 1.0E-7 < var5) || !(var5 < this.cachedScaleX + 1.0E-7) || !(this.cachedScaleY - 1.0E-7 < var7) || !(var7 < this.cachedScaleY + 1.0E-7);
            } else {
               return true;
            }
         } else {
            return true;
         }
      }
   }

   void updateScreenXform(double[] var1) {
      double var2;
      if (this.scaleHint) {
         double var4;
         if (this.rotateHint) {
            var2 = var1[0] / this.cachedScaleX;
            var4 = var1[1] / this.cachedScaleY;
            double var6 = var1[2] - this.cachedRotate;
            this.screenXform.setToScale(var2, var4);
            this.screenXform.rotate(var6);
         } else {
            var2 = var1[0] / this.cachedScaleX;
            var4 = var1[1] / this.cachedScaleY;
            this.screenXform.setToScale(var2, var4);
         }
      } else if (this.rotateHint) {
         var2 = var1[2] - this.cachedRotate;
         this.screenXform.setToRotation(var2, 0.0, 0.0);
      } else {
         this.screenXform.setTransform(BaseTransform.IDENTITY_TRANSFORM);
      }

   }

   public void invalidate() {
      if (this.scrollCacheState == CacheFilter.ScrollCacheState.ENABLED) {
         this.scrollCacheState = CacheFilter.ScrollCacheState.CHECKING_PRECONDITIONS;
      }

      this.imageDataUnref();
      this.lastXDelta = this.lastYDelta = 0.0;
   }

   void imageDataUnref() {
      if (this.tempTexture != null) {
         this.tempTexture.dispose();
         this.tempTexture = null;
      }

      if (this.cachedImageData != null) {
         Filterable var1 = this.cachedImageData.getUntransformedImage();
         if (var1 != null) {
            var1.lock();
         }

         this.cachedImageData.unref();
         this.cachedImageData = null;
      }

   }

   void invalidateByTranslation(double var1, double var3) {
      if (this.cachedImageData != null) {
         if (this.scrollCacheState == CacheFilter.ScrollCacheState.DISABLED) {
            this.imageDataUnref();
         } else if (var1 != 0.0 && var3 != 0.0) {
            this.imageDataUnref();
         } else {
            this.lastYDelta = var3;
            this.lastXDelta = var1;
         }

      }
   }

   public void dispose() {
      this.invalidate();
      this.node = null;
   }

   double[] unmatrix(BaseTransform var1) {
      double[] var2 = new double[3];
      double[][] var3 = new double[][]{{var1.getMxx(), var1.getMxy()}, {var1.getMyx(), var1.getMyy()}};
      double var4 = Math.signum(var3[0][0]);
      double var6 = Math.signum(var3[1][1]);
      double var8 = var4 * this.v2length(var3[0]);
      this.v2scale(var3[0], var4);
      double var10 = this.v2dot(var3[0], var3[1]);
      this.v2combine(var3[1], var3[0], var3[1], 1.0, -var10);
      double var12 = var6 * this.v2length(var3[1]);
      this.v2scale(var3[1], var6);
      double var14 = var3[1][0];
      double var16 = var3[0][0];
      double var18 = 0.0;
      if (var14 >= 0.0) {
         var18 = Math.acos(var16);
      } else if (var16 > 0.0) {
         var18 = 6.283185307179586 + Math.asin(var14);
      } else {
         var18 = Math.PI + Math.acos(-var16);
      }

      var2[0] = var8;
      var2[1] = var12;
      var2[2] = var18;
      return var2;
   }

   void v2combine(double[] var1, double[] var2, double[] var3, double var4, double var6) {
      var3[0] = var4 * var1[0] + var6 * var2[0];
      var3[1] = var4 * var1[1] + var6 * var2[1];
   }

   double v2dot(double[] var1, double[] var2) {
      return var1[0] * var2[0] + var1[1] * var2[1];
   }

   void v2scale(double[] var1, double var2) {
      double var4 = this.v2length(var1);
      if (var4 != 0.0) {
         var1[0] *= var2 / var4;
         var1[1] *= var2 / var4;
      }

   }

   double v2length(double[] var1) {
      return Math.sqrt(var1[0] * var1[0] + var1[1] * var1[1]);
   }

   void render(Graphics var1) {
      BaseTransform var2 = var1.getTransformNoClone();
      PrFilterContext var3 = PrFilterContext.getInstance(var1.getAssociatedScreen());
      double[] var4 = this.unmatrix(var2);
      boolean var5 = this.unsupported(var4);
      this.lastXDelta *= var4[0];
      this.lastYDelta *= var4[1];
      if (this.cachedImageData != null) {
         Filterable var6 = this.cachedImageData.getUntransformedImage();
         if (var6 != null) {
            var6.lock();
            if (!this.cachedImageData.validate(var3)) {
               var6.unlock();
               this.invalidate();
            }
         }
      }

      float var13 = var1.getPixelScaleFactorX();
      float var7 = var1.getPixelScaleFactorY();
      Filterable var8;
      if (this.needToRenderCache(var2, var4, var13, var7)) {
         if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter("CacheFilter rebuilding");
         }

         if (this.cachedImageData != null) {
            var8 = this.cachedImageData.getUntransformedImage();
            if (var8 != null) {
               var8.unlock();
            }

            this.invalidate();
         }

         if (this.scaleHint) {
            this.cachedScaleX = Math.max((double)var13, var4[0]);
            this.cachedScaleY = Math.max((double)var7, var4[1]);
            this.cachedRotate = 0.0;
            this.cachedXform.setTransform(this.cachedScaleX, 0.0, 0.0, this.cachedScaleX, 0.0, 0.0);
            this.updateScreenXform(var4);
         } else {
            this.cachedScaleX = var4[0];
            this.cachedScaleY = var4[1];
            this.cachedRotate = var4[2];
            this.cachedXform.setTransform(var2.getMxx(), var2.getMyx(), var2.getMxy(), var2.getMyy(), 0.0, 0.0);
            this.screenXform.setTransform(BaseTransform.IDENTITY_TRANSFORM);
         }

         this.cacheBounds = this.impl_getCacheBounds(this.cacheBounds, this.cachedXform);
         this.cachedImageData = this.impl_createImageData(var3, this.cacheBounds);
         this.impl_renderNodeToCache(this.cachedImageData, this.cacheBounds, this.cachedXform, (Rectangle)null);
         Rectangle var14 = this.cachedImageData.getUntransformedBounds();
         this.cachedX = (double)var14.x;
         this.cachedY = (double)var14.y;
      } else {
         if (this.scrollCacheState == CacheFilter.ScrollCacheState.ENABLED && (this.lastXDelta != 0.0 || this.lastYDelta != 0.0)) {
            this.impl_moveCacheBy(this.cachedImageData, this.lastXDelta, this.lastYDelta);
            this.impl_renderNodeToCache(this.cachedImageData, this.cacheBounds, this.cachedXform, this.computeDirtyRegionForTranslate());
            this.lastXDelta = this.lastYDelta = 0.0;
         }

         if (var5) {
            this.screenXform.setTransform(BaseTransform.IDENTITY_TRANSFORM);
         } else {
            this.updateScreenXform(var4);
         }
      }

      this.wasUnsupported = var5;
      var8 = this.cachedImageData.getUntransformedImage();
      if (var8 == null) {
         if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter("CacheFilter not used");
         }

         this.impl_renderNodeToScreen(var1);
      } else {
         double var9 = var2.getMxt();
         double var11 = var2.getMyt();
         this.impl_renderCacheToScreen(var1, var8, var9, var11);
         var8.unlock();
      }

   }

   ImageData impl_createImageData(FilterContext var1, Rectangle var2) {
      Filterable var3;
      try {
         var3 = Effect.getCompatibleImage(var1, var2.width, var2.height);
         Texture var4 = ((PrDrawable)var3).getTextureObject();
         var4.contentsUseful();
      } catch (Throwable var5) {
         var3 = null;
      }

      return new ImageData(var1, var3, var2);
   }

   void impl_renderNodeToCache(ImageData var1, Rectangle var2, BaseTransform var3, Rectangle var4) {
      PrDrawable var5 = (PrDrawable)var1.getUntransformedImage();
      if (var5 != null) {
         Graphics var6 = var5.createGraphics();
         TEMP_CACHEFILTER_TRANSFORM.setToIdentity();
         TEMP_CACHEFILTER_TRANSFORM.translate((double)(-var2.x), (double)(-var2.y));
         if (var3 != null) {
            TEMP_CACHEFILTER_TRANSFORM.concatenate(var3);
         }

         if (var4 != null) {
            TEMP_CONTAINER.deriveWithNewRegion((RectBounds)TEMP_BOUNDS.deriveWithNewBounds(var4));
            this.node.doPreCulling(TEMP_CONTAINER, TEMP_CACHEFILTER_TRANSFORM, new GeneralTransform3D());
            var6.setHasPreCullingBits(true);
            var6.setClipRectIndex(0);
            var6.setClipRect(var4);
         }

         var6.transform(TEMP_CACHEFILTER_TRANSFORM);
         if (this.node.getClipNode() != null) {
            this.node.renderClip(var6);
         } else if (this.node.getEffectFilter() != null) {
            this.node.renderEffect(var6);
         } else {
            this.node.renderContent(var6);
         }
      }

   }

   void impl_renderNodeToScreen(Object var1) {
      Graphics var2 = (Graphics)var1;
      if (this.node.getEffectFilter() != null) {
         this.node.renderEffect(var2);
      } else {
         this.node.renderContent(var2);
      }

   }

   void impl_renderCacheToScreen(Object var1, Filterable var2, double var3, double var5) {
      Graphics var7 = (Graphics)var1;
      var7.setTransform(this.screenXform.getMxx(), this.screenXform.getMyx(), this.screenXform.getMxy(), this.screenXform.getMyy(), var3, var5);
      var7.translate((float)this.cachedX, (float)this.cachedY);
      Texture var8 = ((PrDrawable)var2).getTextureObject();
      Rectangle var9 = this.cachedImageData.getUntransformedBounds();
      var7.drawTexture(var8, 0.0F, 0.0F, (float)var9.width, (float)var9.height);
   }

   boolean impl_scrollCacheCapable() {
      if (!(this.node instanceof NGGroup)) {
         return false;
      } else {
         List var1 = ((NGGroup)this.node).getChildren();
         if (var1.size() != 1) {
            return false;
         } else {
            NGNode var2 = (NGNode)var1.get(0);
            if (!var2.getTransform().is2D()) {
               return false;
            } else {
               NGNode var3 = this.node.getClipNode();
               if (var3 != null && var3.isRectClip(BaseTransform.IDENTITY_TRANSFORM, false)) {
                  if (this.node instanceof NGRegion) {
                     NGRegion var4 = (NGRegion)this.node;
                     if (!var4.getBorder().isEmpty()) {
                        return false;
                     }

                     Background var5 = var4.getBackground();
                     if (!var5.isEmpty()) {
                        if (var5.getImages().isEmpty() && var5.getFills().size() == 1) {
                           BackgroundFill var6 = (BackgroundFill)var5.getFills().get(0);
                           Paint var7 = var6.getFill();
                           BaseBounds var8 = var3.getCompleteBounds(TEMP_BOUNDS, BaseTransform.IDENTITY_TRANSFORM);
                           return var7.isOpaque() && var7 instanceof Color && var6.getInsets().equals(Insets.EMPTY) && var8.getMinX() == 0.0F && var8.getMinY() == 0.0F && var8.getMaxX() == var4.getWidth() && var8.getMaxY() == var4.getHeight();
                        }

                        return false;
                     }
                  }

                  return true;
               } else {
                  return false;
               }
            }
         }
      }
   }

   void impl_moveCacheBy(ImageData var1, double var2, double var4) {
      PrDrawable var6 = (PrDrawable)var1.getUntransformedImage();
      Rectangle var7 = var1.getUntransformedBounds();
      int var8 = (int)Math.max(0.0, -var2);
      int var9 = (int)Math.max(0.0, -var4);
      int var10 = (int)Math.max(0.0, var2);
      int var11 = (int)Math.max(0.0, var4);
      int var12 = var7.width - (int)Math.abs(var2);
      int var13 = var7.height - (int)Math.abs(var4);
      Graphics var14 = var6.createGraphics();
      if (this.tempTexture != null) {
         this.tempTexture.lock();
         if (this.tempTexture.isSurfaceLost()) {
            this.tempTexture = null;
         }
      }

      if (this.tempTexture == null) {
         this.tempTexture = var14.getResourceFactory().createRTTexture(var6.getPhysicalWidth(), var6.getPhysicalHeight(), Texture.WrapMode.CLAMP_NOT_NEEDED);
      }

      Graphics var15 = this.tempTexture.createGraphics();
      var15.clear();
      var15.drawTexture(var6.getTextureObject(), 0.0F, 0.0F, (float)var12, (float)var13, (float)var8, (float)var9, (float)(var8 + var12), (float)(var9 + var13));
      var15.sync();
      var14.clear();
      var14.drawTexture(this.tempTexture, (float)var10, (float)var11, (float)(var10 + var12), (float)(var11 + var13), 0.0F, 0.0F, (float)var12, (float)var13);
      this.tempTexture.unlock();
   }

   Rectangle impl_getCacheBounds(Rectangle var1, BaseTransform var2) {
      BaseBounds var3 = this.node.getClippedBounds(TEMP_BOUNDS, var2);
      var1.setBounds(var3);
      return var1;
   }

   BaseBounds computeDirtyBounds(BaseBounds var1, BaseTransform var2, GeneralTransform3D var3) {
      if (!this.node.dirtyBounds.isEmpty()) {
         var1 = var1.deriveWithNewBounds(this.node.dirtyBounds);
      } else {
         var1 = var1.deriveWithNewBounds(this.node.transformedBounds);
      }

      if (!var1.isEmpty()) {
         var1.roundOut();
         var1 = this.node.computePadding(var1);
         var1 = var2.transform(var1, var1);
         var1 = var3.transform(var1, var1);
      }

      return var1;
   }

   private static enum ScrollCacheState {
      CHECKING_PRECONDITIONS,
      ENABLED,
      DISABLED;
   }
}
