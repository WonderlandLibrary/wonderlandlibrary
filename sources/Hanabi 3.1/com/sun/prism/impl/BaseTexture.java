package com.sun.prism.impl;

import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import java.nio.Buffer;

public abstract class BaseTexture implements Texture {
   protected final ManagedResource resource;
   private final PixelFormat format;
   private final int physicalWidth;
   private final int physicalHeight;
   private final int contentX;
   private final int contentY;
   protected int contentWidth;
   protected int contentHeight;
   private final int maxContentWidth;
   private final int maxContentHeight;
   private final Texture.WrapMode wrapMode;
   private final boolean useMipmap;
   private boolean linearFiltering;
   private int lastImageSerial;

   protected BaseTexture(BaseTexture var1, Texture.WrapMode var2, boolean var3) {
      this.linearFiltering = true;
      this.resource = var1.resource;
      this.format = var1.format;
      this.wrapMode = var2;
      this.physicalWidth = var1.physicalWidth;
      this.physicalHeight = var1.physicalHeight;
      this.contentX = var1.contentX;
      this.contentY = var1.contentY;
      this.contentWidth = var1.contentWidth;
      this.contentHeight = var1.contentHeight;
      this.maxContentWidth = var1.maxContentWidth;
      this.maxContentHeight = var1.maxContentHeight;
      this.useMipmap = var3;
   }

   protected BaseTexture(ManagedResource var1, PixelFormat var2, Texture.WrapMode var3, int var4, int var5) {
      this(var1, var2, var3, var4, var5, 0, 0, var4, var5, false);
   }

   protected BaseTexture(ManagedResource var1, PixelFormat var2, Texture.WrapMode var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10) {
      this.linearFiltering = true;
      this.resource = var1;
      this.format = var2;
      this.wrapMode = var3;
      this.physicalWidth = var4;
      this.physicalHeight = var5;
      this.contentX = var6;
      this.contentY = var7;
      this.contentWidth = var8;
      this.contentHeight = var9;
      this.maxContentWidth = var4;
      this.maxContentHeight = var5;
      this.useMipmap = var10;
   }

   protected BaseTexture(ManagedResource var1, PixelFormat var2, Texture.WrapMode var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, boolean var12) {
      this.linearFiltering = true;
      this.resource = var1;
      this.format = var2;
      this.wrapMode = var3;
      this.physicalWidth = var4;
      this.physicalHeight = var5;
      this.contentX = var6;
      this.contentY = var7;
      this.contentWidth = var8;
      this.contentHeight = var9;
      this.maxContentWidth = var10;
      this.maxContentHeight = var11;
      this.useMipmap = var12;
   }

   public final PixelFormat getPixelFormat() {
      return this.format;
   }

   public final int getPhysicalWidth() {
      return this.physicalWidth;
   }

   public final int getPhysicalHeight() {
      return this.physicalHeight;
   }

   public final int getContentX() {
      return this.contentX;
   }

   public final int getContentY() {
      return this.contentY;
   }

   public final int getContentWidth() {
      return this.contentWidth;
   }

   public final int getContentHeight() {
      return this.contentHeight;
   }

   public int getMaxContentWidth() {
      return this.maxContentWidth;
   }

   public int getMaxContentHeight() {
      return this.maxContentHeight;
   }

   public void setContentWidth(int var1) {
      if (var1 > this.maxContentWidth) {
         throw new IllegalArgumentException("ContentWidth must be less than or equal to maxContentWidth");
      } else {
         this.contentWidth = var1;
      }
   }

   public void setContentHeight(int var1) {
      if (var1 > this.maxContentHeight) {
         throw new IllegalArgumentException("ContentWidth must be less than or equal to maxContentHeight");
      } else {
         this.contentHeight = var1;
      }
   }

   public final Texture.WrapMode getWrapMode() {
      return this.wrapMode;
   }

   public boolean getUseMipmap() {
      return this.useMipmap;
   }

   public Texture getSharedTexture(Texture.WrapMode var1) {
      this.assertLocked();
      if (this.wrapMode == var1) {
         this.lock();
         return this;
      } else {
         switch (var1) {
            case REPEAT:
               if (this.wrapMode != Texture.WrapMode.CLAMP_TO_EDGE) {
                  return null;
               }
               break;
            case CLAMP_TO_EDGE:
               if (this.wrapMode != Texture.WrapMode.REPEAT) {
                  return null;
               }
               break;
            default:
               return null;
         }

         Texture var2 = this.createSharedTexture(var1);
         var2.lock();
         return var2;
      }
   }

   protected abstract Texture createSharedTexture(Texture.WrapMode var1);

   public final boolean getLinearFiltering() {
      return this.linearFiltering;
   }

   public void setLinearFiltering(boolean var1) {
      this.linearFiltering = var1;
   }

   public final int getLastImageSerial() {
      return this.lastImageSerial;
   }

   public final void setLastImageSerial(int var1) {
      this.lastImageSerial = var1;
   }

   public final void lock() {
      this.resource.lock();
   }

   public final boolean isLocked() {
      return this.resource.isLocked();
   }

   public final int getLockCount() {
      return this.resource.getLockCount();
   }

   public final void assertLocked() {
      this.resource.assertLocked();
   }

   public final void unlock() {
      this.resource.unlock();
   }

   public final void makePermanent() {
      this.resource.makePermanent();
   }

   public final void contentsUseful() {
      this.resource.contentsUseful();
   }

   public final void contentsNotUseful() {
      this.resource.contentsNotUseful();
   }

   public final boolean isSurfaceLost() {
      return !this.resource.isValid();
   }

   public final void dispose() {
      this.resource.dispose();
   }

   public void update(Image var1) {
      this.update(var1, 0, 0);
   }

   public void update(Image var1, int var2, int var3) {
      this.update(var1, var2, var3, var1.getWidth(), var1.getHeight());
   }

   public void update(Image var1, int var2, int var3, int var4, int var5) {
      this.update(var1, var2, var3, var4, var5, false);
   }

   public void update(Image var1, int var2, int var3, int var4, int var5, boolean var6) {
      Buffer var7 = var1.getPixelBuffer();
      int var8 = var7.position();
      this.update(var7, var1.getPixelFormat(), var2, var3, var1.getMinX(), var1.getMinY(), var4, var5, var1.getScanlineStride(), var6);
      var7.position(var8);
   }

   protected void checkUpdateParams(Buffer var1, PixelFormat var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      if (this.format == PixelFormat.MULTI_YCbCr_420) {
         throw new IllegalArgumentException("MULTI_YCbCr_420 requires multitexturing");
      } else if (var1 == null) {
         throw new IllegalArgumentException("Pixel buffer must be non-null");
      } else if (var2 != this.format) {
         throw new IllegalArgumentException("Image format (" + var2 + ") must match texture format (" + this.format + ")");
      } else if (var3 >= 0 && var4 >= 0) {
         if (var5 >= 0 && var6 >= 0) {
            if (var7 > 0 && var8 > 0) {
               int var10 = var2.getBytesPerPixelUnit();
               if (var9 % var10 != 0) {
                  throw new IllegalArgumentException("srcscan (" + var9 + ") must be a multiple of the pixel stride (" + var10 + ")");
               } else if (var7 > var9 / var10) {
                  throw new IllegalArgumentException("srcw (" + var7 + ") must be <= srcscan/bytesPerPixel (" + var9 / var10 + ")");
               } else if (var3 + var7 <= this.contentWidth && var4 + var8 <= this.contentHeight) {
                  int var11 = var5 * var10 + var6 * var9 + (var8 - 1) * var9 + var7 * var10;
                  int var12 = var11 / this.format.getDataType().getSizeInBytes();
                  if (var12 > var1.remaining()) {
                     throw new IllegalArgumentException("Upload requires " + var12 + " elements, but only " + var1.remaining() + " elements remain in the buffer");
                  }
               } else {
                  throw new IllegalArgumentException("Destination region (x=" + var3 + ", y=" + var4 + ", w=" + var7 + ", h=" + var8 + ") must fit within texture content bounds (contentWidth=" + this.contentWidth + ", contentHeight=" + this.contentHeight + ")");
               }
            } else {
               throw new IllegalArgumentException("srcw (" + var7 + ") and srch (" + var8 + ") must be > 0");
            }
         } else {
            throw new IllegalArgumentException("srcx (" + var5 + ") and srcy (" + var6 + ") must be >= 0");
         }
      } else {
         throw new IllegalArgumentException("dstx (" + var3 + ") and dsty (" + var4 + ") must be >= 0");
      }
   }

   public String toString() {
      return super.toString() + " [format=" + this.format + " physicalWidth=" + this.physicalWidth + " physicalHeight=" + this.physicalHeight + " contentX=" + this.contentX + " contentY=" + this.contentY + " contentWidth=" + this.contentWidth + " contentHeight=" + this.contentHeight + " wrapMode=" + this.wrapMode + " linearFiltering=" + this.linearFiltering + "]";
   }
}
