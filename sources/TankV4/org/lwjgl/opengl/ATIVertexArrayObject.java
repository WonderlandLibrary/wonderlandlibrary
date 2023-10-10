package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ATIVertexArrayObject {
   public static final int GL_STATIC_ATI = 34656;
   public static final int GL_DYNAMIC_ATI = 34657;
   public static final int GL_PRESERVE_ATI = 34658;
   public static final int GL_DISCARD_ATI = 34659;
   public static final int GL_OBJECT_BUFFER_SIZE_ATI = 34660;
   public static final int GL_OBJECT_BUFFER_USAGE_ATI = 34661;
   public static final int GL_ARRAY_OBJECT_BUFFER_ATI = 34662;
   public static final int GL_ARRAY_OBJECT_OFFSET_ATI = 34663;

   private ATIVertexArrayObject() {
   }

   public static int glNewObjectBufferATI(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNewObjectBufferATI;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglNewObjectBufferATI(var0, 0L, var1, var3);
      return var5;
   }

   public static int glNewObjectBufferATI(ByteBuffer var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNewObjectBufferATI;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var0);
      int var5 = nglNewObjectBufferATI(var0.remaining(), MemoryUtil.getAddress(var0), var1, var3);
      return var5;
   }

   public static int glNewObjectBufferATI(DoubleBuffer var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNewObjectBufferATI;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var0);
      int var5 = nglNewObjectBufferATI(var0.remaining() << 3, MemoryUtil.getAddress(var0), var1, var3);
      return var5;
   }

   public static int glNewObjectBufferATI(FloatBuffer var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNewObjectBufferATI;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var0);
      int var5 = nglNewObjectBufferATI(var0.remaining() << 2, MemoryUtil.getAddress(var0), var1, var3);
      return var5;
   }

   public static int glNewObjectBufferATI(IntBuffer var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNewObjectBufferATI;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var0);
      int var5 = nglNewObjectBufferATI(var0.remaining() << 2, MemoryUtil.getAddress(var0), var1, var3);
      return var5;
   }

   public static int glNewObjectBufferATI(ShortBuffer var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNewObjectBufferATI;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var0);
      int var5 = nglNewObjectBufferATI(var0.remaining() << 1, MemoryUtil.getAddress(var0), var1, var3);
      return var5;
   }

   static native int nglNewObjectBufferATI(int var0, long var1, int var3, long var4);

   public static boolean glIsObjectBufferATI(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsObjectBufferATI;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsObjectBufferATI(var0, var2);
      return var4;
   }

   static native boolean nglIsObjectBufferATI(int var0, long var1);

   public static void glUpdateObjectBufferATI(int var0, int var1, ByteBuffer var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glUpdateObjectBufferATI;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var2);
      nglUpdateObjectBufferATI(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var3, var5);
   }

   public static void glUpdateObjectBufferATI(int var0, int var1, DoubleBuffer var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glUpdateObjectBufferATI;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var2);
      nglUpdateObjectBufferATI(var0, var1, var2.remaining() << 3, MemoryUtil.getAddress(var2), var3, var5);
   }

   public static void glUpdateObjectBufferATI(int var0, int var1, FloatBuffer var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glUpdateObjectBufferATI;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var2);
      nglUpdateObjectBufferATI(var0, var1, var2.remaining() << 2, MemoryUtil.getAddress(var2), var3, var5);
   }

   public static void glUpdateObjectBufferATI(int var0, int var1, IntBuffer var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glUpdateObjectBufferATI;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var2);
      nglUpdateObjectBufferATI(var0, var1, var2.remaining() << 2, MemoryUtil.getAddress(var2), var3, var5);
   }

   public static void glUpdateObjectBufferATI(int var0, int var1, ShortBuffer var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glUpdateObjectBufferATI;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var2);
      nglUpdateObjectBufferATI(var0, var1, var2.remaining() << 1, MemoryUtil.getAddress(var2), var3, var5);
   }

   static native void nglUpdateObjectBufferATI(int var0, int var1, int var2, long var3, int var5, long var6);

   public static void glGetObjectBufferATI(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetObjectBufferfvATI;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetObjectBufferfvATI(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetObjectBufferfvATI(int var0, int var1, long var2, long var4);

   public static void glGetObjectBufferATI(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetObjectBufferivATI;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetObjectBufferivATI(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetObjectBufferivATI(int var0, int var1, long var2, long var4);

   public static int glGetObjectBufferiATI(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetObjectBufferivATI;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetObjectBufferivATI(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glFreeObjectBufferATI(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glFreeObjectBufferATI;
      BufferChecks.checkFunctionAddress(var2);
      nglFreeObjectBufferATI(var0, var2);
   }

   static native void nglFreeObjectBufferATI(int var0, long var1);

   public static void glArrayObjectATI(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glArrayObjectATI;
      BufferChecks.checkFunctionAddress(var7);
      nglArrayObjectATI(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglArrayObjectATI(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glGetArrayObjectATI(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetArrayObjectfvATI;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetArrayObjectfvATI(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetArrayObjectfvATI(int var0, int var1, long var2, long var4);

   public static void glGetArrayObjectATI(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetArrayObjectivATI;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetArrayObjectivATI(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetArrayObjectivATI(int var0, int var1, long var2, long var4);

   public static void glVariantArrayObjectATI(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVariantArrayObjectATI;
      BufferChecks.checkFunctionAddress(var6);
      nglVariantArrayObjectATI(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVariantArrayObjectATI(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glGetVariantArrayObjectATI(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVariantArrayObjectfvATI;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetVariantArrayObjectfvATI(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVariantArrayObjectfvATI(int var0, int var1, long var2, long var4);

   public static void glGetVariantArrayObjectATI(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVariantArrayObjectivATI;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetVariantArrayObjectivATI(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVariantArrayObjectivATI(int var0, int var1, long var2, long var4);
}
