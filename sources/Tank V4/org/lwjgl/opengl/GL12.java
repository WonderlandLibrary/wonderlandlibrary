package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class GL12 {
   public static final int GL_TEXTURE_BINDING_3D = 32874;
   public static final int GL_PACK_SKIP_IMAGES = 32875;
   public static final int GL_PACK_IMAGE_HEIGHT = 32876;
   public static final int GL_UNPACK_SKIP_IMAGES = 32877;
   public static final int GL_UNPACK_IMAGE_HEIGHT = 32878;
   public static final int GL_TEXTURE_3D = 32879;
   public static final int GL_PROXY_TEXTURE_3D = 32880;
   public static final int GL_TEXTURE_DEPTH = 32881;
   public static final int GL_TEXTURE_WRAP_R = 32882;
   public static final int GL_MAX_3D_TEXTURE_SIZE = 32883;
   public static final int GL_BGR = 32992;
   public static final int GL_BGRA = 32993;
   public static final int GL_UNSIGNED_BYTE_3_3_2 = 32818;
   public static final int GL_UNSIGNED_BYTE_2_3_3_REV = 33634;
   public static final int GL_UNSIGNED_SHORT_5_6_5 = 33635;
   public static final int GL_UNSIGNED_SHORT_5_6_5_REV = 33636;
   public static final int GL_UNSIGNED_SHORT_4_4_4_4 = 32819;
   public static final int GL_UNSIGNED_SHORT_4_4_4_4_REV = 33637;
   public static final int GL_UNSIGNED_SHORT_5_5_5_1 = 32820;
   public static final int GL_UNSIGNED_SHORT_1_5_5_5_REV = 33638;
   public static final int GL_UNSIGNED_INT_8_8_8_8 = 32821;
   public static final int GL_UNSIGNED_INT_8_8_8_8_REV = 33639;
   public static final int GL_UNSIGNED_INT_10_10_10_2 = 32822;
   public static final int GL_UNSIGNED_INT_2_10_10_10_REV = 33640;
   public static final int GL_RESCALE_NORMAL = 32826;
   public static final int GL_LIGHT_MODEL_COLOR_CONTROL = 33272;
   public static final int GL_SINGLE_COLOR = 33273;
   public static final int GL_SEPARATE_SPECULAR_COLOR = 33274;
   public static final int GL_CLAMP_TO_EDGE = 33071;
   public static final int GL_TEXTURE_MIN_LOD = 33082;
   public static final int GL_TEXTURE_MAX_LOD = 33083;
   public static final int GL_TEXTURE_BASE_LEVEL = 33084;
   public static final int GL_TEXTURE_MAX_LEVEL = 33085;
   public static final int GL_MAX_ELEMENTS_VERTICES = 33000;
   public static final int GL_MAX_ELEMENTS_INDICES = 33001;
   public static final int GL_ALIASED_POINT_SIZE_RANGE = 33901;
   public static final int GL_ALIASED_LINE_WIDTH_RANGE = 33902;
   public static final int GL_SMOOTH_POINT_SIZE_RANGE = 2834;
   public static final int GL_SMOOTH_POINT_SIZE_GRANULARITY = 2835;
   public static final int GL_SMOOTH_LINE_WIDTH_RANGE = 2850;
   public static final int GL_SMOOTH_LINE_WIDTH_GRANULARITY = 2851;

   private GL12() {
   }

   public static void glDrawRangeElements(int var0, int var1, int var2, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawRangeElements;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglDrawRangeElements(var0, var1, var2, var3.remaining(), 5121, MemoryUtil.getAddress(var3), var5);
   }

   public static void glDrawRangeElements(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawRangeElements;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglDrawRangeElements(var0, var1, var2, var3.remaining(), 5125, MemoryUtil.getAddress(var3), var5);
   }

   public static void glDrawRangeElements(int var0, int var1, int var2, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawRangeElements;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var3);
      nglDrawRangeElements(var0, var1, var2, var3.remaining(), 5123, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglDrawRangeElements(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glDrawRangeElements(int var0, int var1, int var2, int var3, int var4, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glDrawRangeElements;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureElementVBOenabled(var7);
      nglDrawRangeElementsBO(var0, var1, var2, var3, var4, var5, var8);
   }

   static native void nglDrawRangeElementsBO(int var0, int var1, int var2, int var3, int var4, long var5, long var7);

   public static void glTexImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ByteBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTexImage3D;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage3DStorage(var9, var7, var8, var3, var4, var5));
      }

      nglTexImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glTexImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, DoubleBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTexImage3D;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage3DStorage(var9, var7, var8, var3, var4, var5));
      }

      nglTexImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glTexImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, FloatBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTexImage3D;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage3DStorage(var9, var7, var8, var3, var4, var5));
      }

      nglTexImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glTexImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, IntBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTexImage3D;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage3DStorage(var9, var7, var8, var3, var4, var5));
      }

      nglTexImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   public static void glTexImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, ShortBuffer var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTexImage3D;
      BufferChecks.checkFunctionAddress(var11);
      GLChecks.ensureUnpackPBOdisabled(var10);
      if (var9 != null) {
         BufferChecks.checkBuffer(var9, GLChecks.calculateTexImage3DStorage(var9, var7, var8, var3, var4, var5));
      }

      nglTexImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, MemoryUtil.getAddressSafe(var9), var11);
   }

   static native void nglTexImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glTexImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTexImage3D;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOenabled(var11);
      nglTexImage3DBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var12);
   }

   static native void nglTexImage3DBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, long var11);

   public static void glTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ByteBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTexSubImage3D;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      BufferChecks.checkBuffer(var10, GLChecks.calculateImageStorage(var10, var8, var9, var5, var6, var7));
      nglTexSubImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddress(var10), var12);
   }

   public static void glTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, DoubleBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTexSubImage3D;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      BufferChecks.checkBuffer(var10, GLChecks.calculateImageStorage(var10, var8, var9, var5, var6, var7));
      nglTexSubImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddress(var10), var12);
   }

   public static void glTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, FloatBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTexSubImage3D;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      BufferChecks.checkBuffer(var10, GLChecks.calculateImageStorage(var10, var8, var9, var5, var6, var7));
      nglTexSubImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddress(var10), var12);
   }

   public static void glTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, IntBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTexSubImage3D;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      BufferChecks.checkBuffer(var10, GLChecks.calculateImageStorage(var10, var8, var9, var5, var6, var7));
      nglTexSubImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddress(var10), var12);
   }

   public static void glTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, ShortBuffer var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glTexSubImage3D;
      BufferChecks.checkFunctionAddress(var12);
      GLChecks.ensureUnpackPBOdisabled(var11);
      BufferChecks.checkBuffer(var10, GLChecks.calculateImageStorage(var10, var8, var9, var5, var6, var7));
      nglTexSubImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, MemoryUtil.getAddress(var10), var12);
   }

   static native void nglTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, long var12);

   public static void glTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10) {
      ContextCapabilities var12 = GLContext.getCapabilities();
      long var13 = var12.glTexSubImage3D;
      BufferChecks.checkFunctionAddress(var13);
      GLChecks.ensureUnpackPBOenabled(var12);
      nglTexSubImage3DBO(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var13);
   }

   static native void nglTexSubImage3DBO(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, long var12);

   public static void glCopyTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glCopyTexSubImage3D;
      BufferChecks.checkFunctionAddress(var10);
      nglCopyTexSubImage3D(var0, var1, var2, var3, var4, var5, var6, var7, var8, var10);
   }

   static native void nglCopyTexSubImage3D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9);
}
