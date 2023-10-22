package javafx.scene.media;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.media.PrismMediaFrameHandler;
import com.sun.javafx.sg.prism.MediaFrameTracker;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;

class NGMediaView extends NGNode {
   private boolean smooth = true;
   private final RectBounds dimension = new RectBounds();
   private final RectBounds viewport = new RectBounds();
   private PrismMediaFrameHandler handler;
   private MediaPlayer player;
   private MediaFrameTracker frameTracker;

   public void renderNextFrame() {
      this.visualsChanged();
   }

   public boolean isSmooth() {
      return this.smooth;
   }

   public void setSmooth(boolean var1) {
      if (var1 != this.smooth) {
         this.smooth = var1;
         this.visualsChanged();
      }

   }

   public void setX(float var1) {
      if (var1 != this.dimension.getMinX()) {
         float var2 = this.dimension.getWidth();
         this.dimension.setMinX(var1);
         this.dimension.setMaxX(var1 + var2);
         this.geometryChanged();
      }

   }

   public void setY(float var1) {
      if (var1 != this.dimension.getMinY()) {
         float var2 = this.dimension.getHeight();
         this.dimension.setMinY(var1);
         this.dimension.setMaxY(var1 + var2);
         this.geometryChanged();
      }

   }

   public void setMediaProvider(Object var1) {
      if (var1 == null) {
         this.player = null;
         this.handler = null;
         this.geometryChanged();
      } else if (var1 instanceof MediaPlayer) {
         this.player = (MediaPlayer)var1;
         this.handler = PrismMediaFrameHandler.getHandler(this.player);
         this.geometryChanged();
      }

   }

   public void setViewport(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      float var8 = 0.0F;
      float var9 = 0.0F;
      float var10 = var1;
      float var11 = var2;
      if (null != this.player) {
         Media var12 = this.player.getMedia();
         var8 = (float)var12.getWidth();
         var9 = (float)var12.getHeight();
      }

      if (var5 > 0.0F && var6 > 0.0F) {
         this.viewport.setBounds(var3, var4, var3 + var5, var4 + var6);
         var8 = var5;
         var9 = var6;
      } else {
         this.viewport.setBounds(0.0F, 0.0F, var8, var9);
      }

      if (var1 <= 0.0F && var2 <= 0.0F) {
         var10 = var8;
         var11 = var9;
      } else if (var7) {
         if ((double)var1 <= 0.0) {
            var10 = var9 > 0.0F ? var8 * (var2 / var9) : 0.0F;
            var11 = var2;
         } else if ((double)var2 <= 0.0) {
            var10 = var1;
            var11 = var8 > 0.0F ? var9 * (var1 / var8) : 0.0F;
         } else {
            if (var8 == 0.0F) {
               var8 = var1;
            }

            if (var9 == 0.0F) {
               var9 = var2;
            }

            float var13 = Math.min(var1 / var8, var2 / var9);
            var10 = var8 * var13;
            var11 = var9 * var13;
         }
      } else if ((double)var2 <= 0.0) {
         var11 = var9;
      } else if ((double)var1 <= 0.0) {
         var10 = var8;
      }

      if (var11 < 1.0F) {
         var11 = 1.0F;
      }

      if (var10 < 1.0F) {
         var10 = 1.0F;
      }

      this.dimension.setMaxX(this.dimension.getMinX() + var10);
      this.dimension.setMaxY(this.dimension.getMinY() + var11);
      this.geometryChanged();
   }

   protected void renderContent(Graphics var1) {
      if (null != this.handler && null != this.player) {
         VideoDataBuffer var2 = this.player.impl_getLatestFrame();
         if (null != var2) {
            Texture var3 = this.handler.getTexture(var1, var2);
            if (var3 != null) {
               float var4 = this.viewport.getWidth();
               float var5 = this.viewport.getHeight();
               boolean var6 = !this.dimension.isEmpty();
               boolean var7 = var6 && (var4 != this.dimension.getWidth() || var5 != this.dimension.getHeight());
               var1.translate(this.dimension.getMinX(), this.dimension.getMinY());
               float var8;
               float var9;
               if (var7 && var4 != 0.0F && var5 != 0.0F) {
                  var8 = this.dimension.getWidth() / var4;
                  var9 = this.dimension.getHeight() / var5;
                  var1.scale(var8, var9);
               }

               var8 = this.viewport.getMinX();
               var9 = this.viewport.getMinY();
               float var10 = var8 + var4;
               float var11 = var9 + var5;
               var1.drawTexture(var3, 0.0F, 0.0F, var4, var5, var8, var9, var10, var11);
               var3.unlock();
               if (null != this.frameTracker) {
                  this.frameTracker.incrementRenderedFrameCount(1);
               }
            }

            var2.releaseFrame();
         }
      }
   }

   protected boolean hasOverlappingContents() {
      return false;
   }

   public void setFrameTracker(MediaFrameTracker var1) {
      this.frameTracker = var1;
   }
}
