package com.sun.prism;

import java.nio.Buffer;

public interface Texture extends GraphicsResource {
   PixelFormat getPixelFormat();

   int getPhysicalWidth();

   int getPhysicalHeight();

   int getContentX();

   int getContentY();

   int getContentWidth();

   int getContentHeight();

   int getMaxContentWidth();

   int getMaxContentHeight();

   void setContentWidth(int var1);

   void setContentHeight(int var1);

   int getLastImageSerial();

   void setLastImageSerial(int var1);

   void update(Image var1);

   void update(Image var1, int var2, int var3);

   void update(Image var1, int var2, int var3, int var4, int var5);

   void update(Image var1, int var2, int var3, int var4, int var5, boolean var6);

   void update(Buffer var1, PixelFormat var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10);

   void update(MediaFrame var1, boolean var2);

   WrapMode getWrapMode();

   boolean getUseMipmap();

   Texture getSharedTexture(WrapMode var1);

   boolean getLinearFiltering();

   void setLinearFiltering(boolean var1);

   void lock();

   void unlock();

   boolean isLocked();

   int getLockCount();

   void assertLocked();

   void makePermanent();

   void contentsUseful();

   void contentsNotUseful();

   boolean isSurfaceLost();

   public static enum WrapMode {
      CLAMP_NOT_NEEDED,
      CLAMP_TO_ZERO,
      CLAMP_TO_EDGE,
      REPEAT,
      CLAMP_TO_ZERO_SIMULATED(CLAMP_TO_ZERO),
      CLAMP_TO_EDGE_SIMULATED(CLAMP_TO_EDGE),
      REPEAT_SIMULATED(REPEAT);

      private WrapMode simulates;
      private WrapMode simulatedBy;

      private WrapMode(WrapMode var3) {
         this.simulates = var3;
         var3.simulatedBy = this;
      }

      private WrapMode() {
      }

      public WrapMode simulatedVersion() {
         return this.simulatedBy;
      }

      public boolean isCompatibleWith(WrapMode var1) {
         return var1 == this || var1 == this.simulates || var1 == CLAMP_NOT_NEEDED;
      }
   }

   public static enum Usage {
      DEFAULT,
      DYNAMIC,
      STATIC;
   }
}
