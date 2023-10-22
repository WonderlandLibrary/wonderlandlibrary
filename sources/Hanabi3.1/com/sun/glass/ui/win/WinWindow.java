package com.sun.glass.ui.win;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import java.util.Iterator;

class WinWindow extends Window {
   public static final int RESIZE_DISABLE = 0;
   public static final int RESIZE_AROUND_ANCHOR = 1;
   public static final int RESIZE_TO_FX_ORIGIN = 2;
   public static final long ANCHOR_NO_CAPTURE = Long.MIN_VALUE;
   float fxReqWidth;
   float fxReqHeight;
   int pfReqWidth;
   int pfReqHeight;
   private boolean deferredClosing = false;
   private boolean closingRequested = false;

   private static native void _initIDs();

   protected WinWindow(Window var1, Screen var2, int var3) {
      super(var1, var2, var3);
   }

   protected WinWindow(long var1) {
      super(var1);
   }

   public void setBounds(float var1, float var2, boolean var3, boolean var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      if (var3 || var4 || var5 > 0.0F || var6 > 0.0F || var7 > 0.0F || var8 > 0.0F) {
         long var11 = this._getInsets(this.getRawHandle());
         int var13 = (int)(var11 >> 48) & '\uffff';
         int var14 = (int)(var11 >> 32) & '\uffff';
         int var15 = (int)(var11 >> 16) & '\uffff';
         int var16 = (int)var11 & '\uffff';
         int var17;
         int var18;
         if (!var3 && !var4) {
            var17 = this.x;
            var18 = this.y;
         } else {
            if (var3) {
               var17 = this.screen.toPlatformX(var1);
            } else {
               var17 = this.x;
               var1 = this.screen.fromPlatformX(var17);
            }

            if (var4) {
               var18 = this.screen.toPlatformY(var2);
            } else {
               var18 = this.y;
               var2 = this.screen.fromPlatformY(var18);
            }
         }

         float var19;
         int var21;
         if (var5 > 0.0F) {
            var19 = var5 - (float)(var13 + var15) / this.platformScaleX;
            var21 = (int)Math.ceil((double)(var5 * this.platformScaleX));
         } else {
            var19 = var7 > 0.0F ? var7 : this.fxReqWidth;
            var21 = var13 + var15 + (int)Math.ceil((double)(var19 * this.platformScaleX));
         }

         this.fxReqWidth = var19;
         float var20;
         int var22;
         if (var6 > 0.0F) {
            var20 = var6 - (float)(var14 + var16) / this.platformScaleY;
            var22 = (int)Math.ceil((double)(var6 * this.platformScaleY));
         } else {
            var20 = var8 > 0.0F ? var8 : this.fxReqHeight;
            var22 = var14 + var16 + (int)Math.ceil((double)(var20 * this.platformScaleY));
         }

         this.fxReqHeight = var20;
         long var23 = this._getAnchor(this.getRawHandle());
         int var25 = var23 == Long.MIN_VALUE ? 2 : 1;
         int var26 = (int)(var23 >> 32);
         int var27 = (int)var23;
         int[] var28 = this.notifyMoving(var17, var18, var21, var22, var1, var2, var26, var27, var25, var13, var14, var15, var16);
         if (var28 != null) {
            var17 = var28[0];
            var18 = var28[1];
            var21 = var28[2];
            var22 = var28[3];
         }

         if (!var3) {
            var3 = var17 != this.x;
         }

         if (!var4) {
            var4 = var18 != this.y;
         }

         this.pfReqWidth = (int)Math.ceil((double)(this.fxReqWidth * this.platformScaleX));
         this.pfReqHeight = (int)Math.ceil((double)(this.fxReqHeight * this.platformScaleY));
         this._setBounds(this.getRawHandle(), var17, var18, var3, var4, var21, var22, 0, 0, var9, var10);
      }

   }

   protected int[] notifyMoving(int var1, int var2, int var3, int var4, float var5, float var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13) {
      if (this.screen == null || !this.screen.containsPlatformRect(var1, var2, var3, var4)) {
         float var14 = this.screen == null ? 0.0F : this.screen.portionIntersectsPlatformRect(var1, var2, var3, var4);
         if (var14 < 0.5F) {
            float var15 = (float)var7 / this.platformScaleX;
            float var16 = (float)var8 / this.platformScaleY;
            Screen var17 = this.screen;
            int var18 = var1;
            int var19 = var2;
            int var20 = var3;
            int var21 = var4;
            Iterator var22 = Screen.getScreens().iterator();

            while(true) {
               Screen var23;
               int var24;
               int var25;
               int var26;
               int var27;
               float var30;
               do {
                  do {
                     if (!var22.hasNext()) {
                        if (var17 != this.screen) {
                           this.notifyMoveToAnotherScreen(var17);
                           this.notifyScaleChanged(var17.getPlatformScaleX(), var17.getPlatformScaleY(), var17.getRecommendedOutputScaleX(), var17.getRecommendedOutputScaleY());
                           if (this.view != null) {
                              this.view.updateLocation();
                           }

                           if (var9 == 0) {
                              return null;
                           }

                           return new int[]{var18, var19, var20, var21};
                        }

                        return null;
                     }

                     var23 = (Screen)var22.next();
                  } while(var23 == this.screen);

                  if (var9 == 0) {
                     var24 = var1;
                     var25 = var2;
                     var26 = var3;
                     var27 = var4;
                  } else {
                     int var28 = (int)Math.ceil((double)(this.fxReqWidth * var23.getPlatformScaleX()));
                     int var29 = (int)Math.ceil((double)(this.fxReqHeight * var23.getPlatformScaleY()));
                     var26 = var28 + var10 + var12;
                     var27 = var29 + var11 + var13;
                     if (var9 == 1) {
                        var24 = var1 + var7 - Math.round(var15 * var23.getPlatformScaleX());
                        var25 = var2 + var8 - Math.round(var16 * var23.getPlatformScaleY());
                     } else {
                        var24 = var23.toPlatformX(var5);
                        var25 = var23.toPlatformY(var6);
                     }
                  }

                  var30 = var23.portionIntersectsPlatformRect(var24, var25, var26, var27);
               } while(this.screen != null && (!(var30 > 0.6F) || !(var30 > var14)));

               var14 = var30;
               var17 = var23;
               var18 = var24;
               var19 = var25;
               var20 = var26;
               var21 = var27;
            }
         }
      }

      return null;
   }

   protected void notifyResize(int var1, int var2, int var3) {
      float var4 = this.platformScaleX;
      float var5 = this.platformScaleY;
      long var6 = this._getInsets(this.getRawHandle());
      int var8 = (int)(var6 >> 48) & '\uffff';
      int var9 = (int)(var6 >> 32) & '\uffff';
      int var10 = (int)(var6 >> 16) & '\uffff';
      int var11 = (int)var6 & '\uffff';
      int var12 = var2 - var8 - var10;
      int var13 = var3 - var9 - var11;
      if (var12 != this.pfReqWidth || var4 != this.platformScaleX) {
         this.fxReqWidth = (float)var12 / this.platformScaleX;
         this.pfReqWidth = var12;
      }

      if (var13 != this.pfReqHeight || var5 != this.platformScaleY) {
         this.fxReqHeight = (float)var13 / this.platformScaleY;
         this.pfReqHeight = var13;
      }

      super.notifyResize(var1, var2, var3);
   }

   private native long _getInsets(long var1);

   private native long _getAnchor(long var1);

   protected native long _createWindow(long var1, long var3, int var5);

   protected native long _createChildWindow(long var1);

   protected native boolean _close(long var1);

   protected native boolean _setView(long var1, View var3);

   protected native boolean _setMenubar(long var1, long var3);

   protected native boolean _minimize(long var1, boolean var3);

   protected native boolean _maximize(long var1, boolean var3, boolean var4);

   protected native void _setBounds(long var1, int var3, int var4, boolean var5, boolean var6, int var7, int var8, int var9, int var10, float var11, float var12);

   protected native boolean _setVisible(long var1, boolean var3);

   protected native boolean _setResizable(long var1, boolean var3);

   protected native boolean _requestFocus(long var1, int var3);

   protected native void _setFocusable(long var1, boolean var3);

   protected native boolean _setTitle(long var1, String var3);

   protected native void _setLevel(long var1, int var3);

   protected native void _setAlpha(long var1, float var3);

   protected native boolean _setBackground(long var1, float var3, float var4, float var5);

   protected native void _setEnabled(long var1, boolean var3);

   protected native boolean _setMinimumSize(long var1, int var3, int var4);

   protected native boolean _setMaximumSize(long var1, int var3, int var4);

   protected native void _setIcon(long var1, Pixels var3);

   protected native void _toFront(long var1);

   protected native void _toBack(long var1);

   protected native void _enterModal(long var1);

   protected native void _enterModalWithWindow(long var1, long var3);

   protected native void _exitModal(long var1);

   protected native boolean _grabFocus(long var1);

   protected native void _ungrabFocus(long var1);

   protected native int _getEmbeddedX(long var1);

   protected native int _getEmbeddedY(long var1);

   protected native void _setCursor(long var1, Cursor var3);

   protected void _requestInput(long var1, String var3, int var4, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29, double var31) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   protected void _releaseInput(long var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   void setDeferredClosing(boolean var1) {
      this.deferredClosing = var1;
      if (!this.deferredClosing && this.closingRequested) {
         this.close();
      }

   }

   public void close() {
      if (!this.deferredClosing) {
         super.close();
      } else {
         this.closingRequested = true;
         this.setVisible(false);
      }

   }

   static {
      _initIDs();
   }
}
