package com.sun.prism.sw;

import com.sun.glass.ui.Pixels;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.impl.QueuedPixelSource;
import java.nio.IntBuffer;

final class SWPresentable extends SWRTTexture implements Presentable {
   private final PresentableState pState;
   private Pixels pixels;
   private QueuedPixelSource pixelSource = new QueuedPixelSource(false);

   public SWPresentable(PresentableState var1, SWResourceFactory var2) {
      super(var2, var1.getRenderWidth(), var1.getRenderHeight());
      this.pState = var1;
   }

   public boolean lockResources(PresentableState var1) {
      return this.getPhysicalWidth() != var1.getRenderWidth() || this.getPhysicalHeight() != var1.getRenderHeight();
   }

   public boolean prepare(Rectangle var1) {
      if (!this.pState.isViewClosed()) {
         int var2 = this.getPhysicalWidth();
         int var3 = this.getPhysicalHeight();
         this.pixels = this.pixelSource.getUnusedPixels(var2, var3, 1.0F, 1.0F);
         IntBuffer var4 = (IntBuffer)this.pixels.getPixels();
         IntBuffer var5 = this.getSurface().getDataIntBuffer();

         assert var5.hasArray();

         System.arraycopy(var5.array(), 0, var4.array(), 0, var2 * var3);
         return true;
      } else {
         return false;
      }
   }

   public boolean present() {
      this.pixelSource.enqueuePixels(this.pixels);
      this.pState.uploadPixels(this.pixelSource);
      return true;
   }

   public float getPixelScaleFactorX() {
      return this.pState.getRenderScaleX();
   }

   public float getPixelScaleFactorY() {
      return this.pState.getRenderScaleY();
   }

   public int getContentWidth() {
      return this.pState.getOutputWidth();
   }

   public int getContentHeight() {
      return this.pState.getOutputHeight();
   }

   public boolean isMSAA() {
      return super.isMSAA();
   }
}
