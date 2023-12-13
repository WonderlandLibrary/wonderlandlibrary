package com.sun.javafx.webkit.prism;

import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

final class RTImage extends PrismImage implements ResourceFactoryListener {
   private RTTexture txt;
   private final int width;
   private final int height;
   private boolean listenerAdded = false;
   private ByteBuffer pixelBuffer;
   private float pixelScale;

   RTImage(int var1, int var2, float var3) {
      this.width = var1;
      this.height = var2;
      this.pixelScale = var3;
   }

   Image getImage() {
      return Image.fromByteBgraPreData(this.getPixelBuffer(), this.getWidth(), this.getHeight());
   }

   Graphics getGraphics() {
      Graphics var1 = this.getTexture().createGraphics();
      var1.transform(PrismGraphicsManager.getPixelScaleTransform());
      return var1;
   }

   private RTTexture getTexture() {
      if (this.txt == null) {
         ResourceFactory var1 = GraphicsPipeline.getDefaultResourceFactory();
         this.txt = var1.createRTTexture((int)Math.ceil((double)((float)this.width * this.pixelScale)), (int)Math.ceil((double)((float)this.height * this.pixelScale)), Texture.WrapMode.CLAMP_NOT_NEEDED);
         this.txt.contentsUseful();
         this.txt.makePermanent();
         if (!this.listenerAdded) {
            var1.addFactoryListener(this);
            this.listenerAdded = true;
         }
      }

      return this.txt;
   }

   void draw(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      if (this.txt != null || var1.getCompositeMode() != CompositeMode.SRC_OVER) {
         if (var1 instanceof PrinterGraphics) {
            int var10 = var8 - var6;
            int var11 = var9 - var7;
            IntBuffer var12 = IntBuffer.allocate(var10 * var11);
            PrismInvoker.runOnRenderThread(() -> {
               this.getTexture().readPixels(var12);
            });
            Image var13 = Image.fromIntArgbPreData(var12, var10, var11);
            Texture var14 = var1.getResourceFactory().createTexture(var13, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_NOT_NEEDED);
            var1.drawTexture(var14, (float)var2, (float)var3, (float)var4, (float)var5, 0.0F, 0.0F, (float)var10, (float)var11);
            var14.dispose();
         } else if (this.txt == null) {
            Paint var15 = var1.getPaint();
            var1.setPaint(Color.TRANSPARENT);
            var1.fillQuad((float)var2, (float)var3, (float)var4, (float)var5);
            var1.setPaint(var15);
         } else {
            var1.drawTexture(this.txt, (float)var2, (float)var3, (float)var4, (float)var5, (float)var6 * this.pixelScale, (float)var7 * this.pixelScale, (float)var8 * this.pixelScale, (float)var9 * this.pixelScale);
         }

      }
   }

   void dispose() {
      PrismInvoker.invokeOnRenderThread(() -> {
         if (this.txt != null) {
            this.txt.dispose();
            this.txt = null;
         }

      });
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public ByteBuffer getPixelBuffer() {
      boolean var1 = false;
      if (this.pixelBuffer == null) {
         this.pixelBuffer = ByteBuffer.allocateDirect(this.width * this.height * 4);
         if (this.pixelBuffer != null) {
            this.pixelBuffer.order(ByteOrder.nativeOrder());
            var1 = true;
         }
      }

      if (var1 || this.isDirty()) {
         PrismInvoker.runOnRenderThread(() -> {
            this.flushRQ();
            if (this.txt != null && this.pixelBuffer != null) {
               PixelFormat var1 = this.txt.getPixelFormat();
               if (var1 != PixelFormat.INT_ARGB_PRE && var1 != PixelFormat.BYTE_BGRA_PRE) {
                  throw new AssertionError("Unexpected pixel format: " + var1);
               }

               RTTexture var2 = this.txt;
               if (this.pixelScale != 1.0F) {
                  ResourceFactory var3 = GraphicsPipeline.getDefaultResourceFactory();
                  var2 = var3.createRTTexture(this.width, this.height, Texture.WrapMode.CLAMP_NOT_NEEDED);
                  Graphics var4 = var2.createGraphics();
                  var4.drawTexture(this.txt, 0.0F, 0.0F, (float)this.width, (float)this.height, 0.0F, 0.0F, (float)this.width * this.pixelScale, (float)this.height * this.pixelScale);
               }

               this.pixelBuffer.rewind();
               int[] var5 = var2.getPixels();
               if (var5 != null) {
                  this.pixelBuffer.asIntBuffer().put(var5);
               } else {
                  var2.readPixels(this.pixelBuffer);
               }

               if (var2 != this.txt) {
                  var2.dispose();
               }
            }

         });
      }

      return this.pixelBuffer;
   }

   protected void drawPixelBuffer() {
      PrismInvoker.invokeOnRenderThread(new Runnable() {
         public void run() {
            Graphics var1 = RTImage.this.getGraphics();
            if (var1 != null && RTImage.this.pixelBuffer != null) {
               RTImage.this.pixelBuffer.rewind();
               Image var2 = Image.fromByteBgraPreData(RTImage.this.pixelBuffer, RTImage.this.width, RTImage.this.height);
               Texture var3 = var1.getResourceFactory().createTexture(var2, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_NOT_NEEDED);
               var1.clear();
               var1.drawTexture(var3, 0.0F, 0.0F, (float)RTImage.this.width, (float)RTImage.this.height);
               var3.dispose();
            }

         }
      });
   }

   public void factoryReset() {
      if (this.txt != null) {
         this.txt.dispose();
         this.txt = null;
      }

   }

   public void factoryReleased() {
   }

   public float getPixelScale() {
      return this.pixelScale;
   }
}
