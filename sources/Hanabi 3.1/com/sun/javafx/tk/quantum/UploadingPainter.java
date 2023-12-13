package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Pixels;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.QueuedPixelSource;
import java.nio.IntBuffer;

final class UploadingPainter extends ViewPainter implements Runnable {
   private RTTexture rttexture;
   private RTTexture resolveRTT = null;
   private QueuedPixelSource pixelSource = new QueuedPixelSource(true);
   private float penScaleX;
   private float penScaleY;

   UploadingPainter(GlassScene var1) {
      super(var1);
   }

   void disposeRTTexture() {
      if (this.rttexture != null) {
         this.rttexture.dispose();
         this.rttexture = null;
      }

      if (this.resolveRTT != null) {
         this.resolveRTT.dispose();
         this.resolveRTT = null;
      }

   }

   public float getPixelScaleFactorX() {
      return this.sceneState.getRenderScaleX();
   }

   public float getPixelScaleFactorY() {
      return this.sceneState.getRenderScaleY();
   }

   public void run() {
      renderLock.lock();
      boolean var1 = false;

      try {
         try {
            if (!this.validateStageGraphics()) {
               if (QuantumToolkit.verbose) {
                  System.err.println("UploadingPainter: validateStageGraphics failed");
               }

               this.paintImpl((Graphics)null);
               return;
            }

            if (this.factory == null) {
               this.factory = GraphicsPipeline.getDefaultResourceFactory();
            }

            if (this.factory == null || !this.factory.isDeviceReady()) {
               return;
            }

            float var2 = this.getPixelScaleFactorX();
            float var3 = this.getPixelScaleFactorY();
            int var4 = this.sceneState.getRenderWidth();
            int var5 = this.sceneState.getRenderHeight();
            boolean var6 = this.penScaleX != var2 || this.penScaleY != var3 || this.penWidth != this.viewWidth || this.penHeight != this.viewHeight || this.rttexture == null || this.rttexture.getContentWidth() != var4 || this.rttexture.getContentHeight() != var5;
            if (!var6) {
               this.rttexture.lock();
               if (this.rttexture.isSurfaceLost()) {
                  this.rttexture.unlock();
                  this.sceneState.getScene().entireSceneNeedsRepaint();
                  var6 = true;
               }
            }

            if (var6) {
               this.disposeRTTexture();
               this.rttexture = this.factory.createRTTexture(var4, var5, Texture.WrapMode.CLAMP_NOT_NEEDED, this.sceneState.isMSAA());
               if (this.rttexture == null) {
                  return;
               }

               this.penScaleX = var2;
               this.penScaleY = var3;
               this.penWidth = this.viewWidth;
               this.penHeight = this.viewHeight;
               this.freshBackBuffer = true;
            }

            Graphics var7 = this.rttexture.createGraphics();
            if (var7 == null) {
               this.disposeRTTexture();
               this.sceneState.getScene().entireSceneNeedsRepaint();
               return;
            }

            var7.scale(var2, var3);
            this.paintImpl(var7);
            this.freshBackBuffer = false;
            int var8 = this.sceneState.getOutputWidth();
            int var9 = this.sceneState.getOutputHeight();
            float var10 = this.sceneState.getOutputScaleX();
            float var11 = this.sceneState.getOutputScaleY();
            RTTexture var12;
            if (!this.rttexture.isMSAA() && var8 == var4 && var9 == var5) {
               var12 = this.rttexture;
            } else {
               var12 = this.resolveRenderTarget(var7, var8, var9);
            }

            Pixels var13 = this.pixelSource.getUnusedPixels(var8, var9, var10, var11);
            IntBuffer var14 = (IntBuffer)var13.getPixels();
            int[] var15 = var12.getPixels();
            if (var15 != null) {
               var14.put(var15, 0, var8 * var9);
            } else if (!var12.readPixels(var14)) {
               this.sceneState.getScene().entireSceneNeedsRepaint();
               this.disposeRTTexture();
               var13 = null;
            }

            if (this.rttexture != null) {
               this.rttexture.unlock();
            }

            if (var13 != null) {
               this.pixelSource.enqueuePixels(var13);
               this.sceneState.uploadPixels(this.pixelSource);
               return;
            }
         } catch (Throwable var19) {
            var1 = true;
            var19.printStackTrace(System.err);
         }

      } finally {
         if (this.rttexture != null && this.rttexture.isLocked()) {
            this.rttexture.unlock();
         }

         if (this.resolveRTT != null && this.resolveRTT.isLocked()) {
            this.resolveRTT.unlock();
         }

         Disposer.cleanUp();
         this.sceneState.getScene().setPainting(false);
         if (this.factory != null) {
            this.factory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(var1);
         }

         renderLock.unlock();
      }
   }

   private RTTexture resolveRenderTarget(Graphics var1, int var2, int var3) {
      if (this.resolveRTT != null) {
         this.resolveRTT.lock();
         if (this.resolveRTT.isSurfaceLost() || this.resolveRTT.getContentWidth() != var2 || this.resolveRTT.getContentHeight() != var3) {
            this.resolveRTT.unlock();
            this.resolveRTT.dispose();
            this.resolveRTT = null;
         }
      }

      if (this.resolveRTT == null) {
         this.resolveRTT = var1.getResourceFactory().createRTTexture(var2, var3, Texture.WrapMode.CLAMP_NOT_NEEDED, false);
      }

      int var4 = this.rttexture.getContentWidth();
      int var5 = this.rttexture.getContentHeight();
      var1.blit(this.rttexture, this.resolveRTT, 0, 0, var4, var5, 0, 0, var2, var3);
      return this.resolveRTT;
   }
}
