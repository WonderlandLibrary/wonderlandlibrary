package com.sun.prism.d3d;

import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseTexture;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

class D3DTexture extends BaseTexture implements D3DContextSource {
   D3DTexture(D3DContext var1, PixelFormat var2, Texture.WrapMode var3, long var4, int var6, int var7, int var8, int var9, boolean var10) {
      this(var1, var2, var3, var4, var6, var7, 0, 0, var8, var9, var10, 0, false);
   }

   D3DTexture(D3DContext var1, PixelFormat var2, Texture.WrapMode var3, long var4, int var6, int var7, int var8, int var9, int var10, int var11, boolean var12, int var13, boolean var14) {
      super(new D3DTextureResource(new D3DTextureData(var1, var4, var12, var6, var7, var2, var13)), var2, var3, var6, var7, var8, var9, var10, var11, var6, var7, var14);
   }

   D3DTexture(D3DTexture var1, Texture.WrapMode var2) {
      super(var1, var2, false);
   }

   protected Texture createSharedTexture(Texture.WrapMode var1) {
      return new D3DTexture(this, var1);
   }

   public long getNativeSourceHandle() {
      return ((D3DTextureData)((D3DTextureResource)this.resource).getResource()).getResource();
   }

   public long getNativeTextureObject() {
      return D3DResourceFactory.nGetNativeTextureObject(this.getNativeSourceHandle());
   }

   public D3DContext getContext() {
      return ((D3DTextureData)((D3DTextureResource)this.resource).getResource()).getContext();
   }

   public void update(MediaFrame var1, boolean var2) {
      if (var1.getPixelFormat() == PixelFormat.MULTI_YCbCr_420) {
         throw new IllegalArgumentException("Unsupported format " + var1.getPixelFormat());
      } else {
         var1.holdFrame();
         ByteBuffer var3 = var1.getBufferForPlane(0);
         D3DContext var5 = this.getContext();
         if (!var2) {
            var5.flushVertexBuffer();
         }

         PixelFormat var6 = var1.getPixelFormat();
         int var4;
         if (var6.getDataType() == PixelFormat.DataType.INT) {
            var4 = D3DResourceFactory.nUpdateTextureI(var5.getContextHandle(), this.getNativeSourceHandle(), var3.asIntBuffer(), (int[])null, 0, 0, 0, 0, var1.getEncodedWidth(), var1.getEncodedHeight(), var1.strideForPlane(0));
         } else {
            var4 = D3DResourceFactory.nUpdateTextureB(var5.getContextHandle(), this.getNativeSourceHandle(), var3, (byte[])null, var6.ordinal(), 0, 0, 0, 0, var1.getEncodedWidth(), var1.getEncodedHeight(), var1.strideForPlane(0));
         }

         D3DContext.validate(var4);
         var1.releaseFrame();
      }
   }

   public void update(Buffer var1, PixelFormat var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10) {
      this.checkUpdateParams(var1, var2, var3, var4, var5, var6, var7, var8, var9);
      if (!var10) {
         this.getContext().flushVertexBuffer();
      }

      int var11 = this.getContentX();
      int var12 = this.getContentY();
      int var13 = this.getContentWidth();
      int var14 = this.getContentHeight();
      int var15 = this.getPhysicalWidth();
      int var16 = this.getPhysicalHeight();
      this.update(var1, var2, var11 + var3, var12 + var4, var5, var6, var7, var8, var9);
      boolean var17;
      boolean var18;
      switch (this.getWrapMode()) {
         case CLAMP_TO_EDGE:
         case REPEAT:
         default:
            break;
         case CLAMP_TO_EDGE_SIMULATED:
            var17 = var13 < var15 && var3 + var7 == var13;
            var18 = var14 < var16 && var4 + var8 == var14;
            if (var17) {
               this.update(var1, var2, var11 + var13, var12 + var4, var5 + var7 - 1, var6, 1, var8, var9);
            }

            if (var18) {
               this.update(var1, var2, var11 + var3, var12 + var14, var5, var6 + var8 - 1, var7, 1, var9);
               if (var17) {
                  this.update(var1, var2, var11 + var13, var12 + var14, var5 + var7 - 1, var6 + var8 - 1, 1, 1, var9);
               }
            }
            break;
         case REPEAT_SIMULATED:
            var17 = var13 < var15 && var3 == 0;
            var18 = var14 < var16 && var4 == 0;
            if (var17) {
               this.update(var1, var2, var11 + var13, var12 + var4, var5, var6, 1, var8, var9);
            }

            if (var18) {
               this.update(var1, var2, var11 + var3, var12 + var14, var5, var6, var7, 1, var9);
               if (var17) {
                  this.update(var1, var2, var11 + var13, var12 + var14, var5, var6, 1, 1, var9);
               }
            }
      }

   }

   public void update(Buffer var1, PixelFormat var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      D3DContext var10 = this.getContext();
      int var11;
      if (var2.getDataType() == PixelFormat.DataType.INT) {
         IntBuffer var12 = (IntBuffer)var1;
         int[] var13 = var12.hasArray() ? var12.array() : null;
         var11 = D3DResourceFactory.nUpdateTextureI(var10.getContextHandle(), this.getNativeSourceHandle(), var12, var13, var3, var4, var5, var6, var7, var8, var9);
      } else if (var2.getDataType() == PixelFormat.DataType.FLOAT) {
         FloatBuffer var14 = (FloatBuffer)var1;
         float[] var16 = var14.hasArray() ? var14.array() : null;
         var11 = D3DResourceFactory.nUpdateTextureF(var10.getContextHandle(), this.getNativeSourceHandle(), var14, var16, var3, var4, var5, var6, var7, var8, var9);
      } else {
         ByteBuffer var15 = (ByteBuffer)var1;
         var15.rewind();
         byte[] var17 = var15.hasArray() ? var15.array() : null;
         var11 = D3DResourceFactory.nUpdateTextureB(var10.getContextHandle(), this.getNativeSourceHandle(), var15, var17, var2.ordinal(), var3, var4, var5, var6, var7, var8, var9);
      }

      D3DContext.validate(var11);
   }
}
