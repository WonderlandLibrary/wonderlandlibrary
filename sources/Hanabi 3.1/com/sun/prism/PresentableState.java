package com.sun.prism;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;

public abstract class PresentableState {
   protected Window window;
   protected View view;
   protected int nativeFrameBuffer;
   protected int windowX;
   protected int windowY;
   protected float windowAlpha;
   protected long nativeWindowHandle;
   protected long nativeView;
   protected int viewWidth;
   protected int viewHeight;
   protected float renderScaleX;
   protected float renderScaleY;
   protected int renderWidth;
   protected int renderHeight;
   protected float outputScaleX;
   protected float outputScaleY;
   protected int outputWidth;
   protected int outputHeight;
   protected int screenHeight;
   protected int screenWidth;
   protected boolean isWindowVisible;
   protected boolean isWindowMinimized;
   protected static final boolean hasWindowManager = Application.GetApplication().hasWindowManager();
   protected boolean isClosed;
   protected final int pixelFormat = Pixels.getNativeFormat();

   public int getWindowX() {
      return this.windowX;
   }

   public int getWindowY() {
      return this.windowY;
   }

   public int getWidth() {
      return this.viewWidth;
   }

   public int getHeight() {
      return this.viewHeight;
   }

   public int getRenderWidth() {
      return this.renderWidth;
   }

   public int getRenderHeight() {
      return this.renderHeight;
   }

   public int getOutputWidth() {
      return this.outputWidth;
   }

   public int getOutputHeight() {
      return this.outputHeight;
   }

   public float getRenderScaleX() {
      return this.renderScaleX;
   }

   public float getRenderScaleY() {
      return this.renderScaleY;
   }

   public float getOutputScaleX() {
      return this.outputScaleX;
   }

   public float getOutputScaleY() {
      return this.outputScaleY;
   }

   public float getAlpha() {
      return this.windowAlpha;
   }

   public long getNativeWindow() {
      return this.nativeWindowHandle;
   }

   public long getNativeView() {
      return this.nativeView;
   }

   public int getScreenHeight() {
      return this.screenHeight;
   }

   public int getScreenWidth() {
      return this.screenWidth;
   }

   public boolean isViewClosed() {
      return this.isClosed;
   }

   public boolean isWindowMinimized() {
      return this.isWindowMinimized;
   }

   public boolean isWindowVisible() {
      return this.isWindowVisible;
   }

   public boolean hasWindowManager() {
      return hasWindowManager;
   }

   public Window getWindow() {
      return this.window;
   }

   public boolean isMSAA() {
      return false;
   }

   public View getView() {
      return this.view;
   }

   public int getPixelFormat() {
      return this.pixelFormat;
   }

   public int getNativeFrameBuffer() {
      return this.nativeFrameBuffer;
   }

   public void lock() {
      if (this.view != null) {
         this.view.lock();
         this.nativeFrameBuffer = this.view.getNativeFrameBuffer();
      }

   }

   public void unlock() {
      if (this.view != null) {
         this.view.unlock();
      }

   }

   public void uploadPixels(PixelSource var1) {
      Pixels var2 = var1.getLatestPixels();
      if (var2 != null) {
         try {
            this.view.uploadPixels(var2);
         } finally {
            var1.doneWithPixels(var2);
         }
      }

   }

   private int scale(int var1, float var2, float var3) {
      return var2 == var3 ? var1 : (int)Math.ceil((double)((float)var1 * var3 / var2));
   }

   protected void update(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.renderScaleX = var3;
      this.renderScaleY = var4;
      this.outputScaleX = var5;
      this.outputScaleY = var6;
      if (var3 == var1 && var4 == var2) {
         this.renderWidth = this.viewWidth;
         this.renderHeight = this.viewHeight;
      } else {
         this.renderWidth = this.scale(this.viewWidth, var1, var3);
         this.renderHeight = this.scale(this.viewHeight, var2, var4);
      }

      if (var5 == var1 && var6 == var2) {
         this.outputWidth = this.viewWidth;
         this.outputHeight = this.viewHeight;
      } else if (var5 == var3 && var6 == var4) {
         this.outputWidth = this.renderWidth;
         this.outputHeight = this.renderHeight;
      } else {
         this.outputWidth = this.scale(this.viewWidth, var1, var5);
         this.outputHeight = this.scale(this.viewHeight, var2, var6);
      }

   }

   public void update() {
      if (this.view != null) {
         this.viewWidth = this.view.getWidth();
         this.viewHeight = this.view.getHeight();
         this.window = this.view.getWindow();
      } else {
         this.viewWidth = this.viewHeight = -1;
         this.window = null;
      }

      if (this.window != null) {
         this.windowX = this.window.getX();
         this.windowY = this.window.getY();
         this.windowAlpha = this.window.getAlpha();
         this.nativeView = this.view.getNativeView();
         this.nativeWindowHandle = this.window.getNativeWindow();
         this.isClosed = this.view.isClosed();
         this.isWindowVisible = this.window.isVisible();
         this.isWindowMinimized = this.window.isMinimized();
         this.update(this.window.getPlatformScaleX(), this.window.getPlatformScaleY(), this.window.getRenderScaleX(), this.window.getRenderScaleY(), this.window.getOutputScaleX(), this.window.getOutputScaleY());
         Screen var1 = this.window.getScreen();
         if (var1 != null) {
            this.screenHeight = var1.getHeight();
            this.screenWidth = var1.getWidth();
         }
      } else {
         this.nativeView = -1L;
         this.nativeWindowHandle = -1L;
         this.isClosed = true;
      }

   }
}
