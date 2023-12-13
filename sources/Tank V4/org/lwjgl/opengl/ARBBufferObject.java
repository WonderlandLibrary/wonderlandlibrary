package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public class ARBBufferObject {
   public static final int GL_STREAM_DRAW_ARB = 35040;
   public static final int GL_STREAM_READ_ARB = 35041;
   public static final int GL_STREAM_COPY_ARB = 35042;
   public static final int GL_STATIC_DRAW_ARB = 35044;
   public static final int GL_STATIC_READ_ARB = 35045;
   public static final int GL_STATIC_COPY_ARB = 35046;
   public static final int GL_DYNAMIC_DRAW_ARB = 35048;
   public static final int GL_DYNAMIC_READ_ARB = 35049;
   public static final int GL_DYNAMIC_COPY_ARB = 35050;
   public static final int GL_READ_ONLY_ARB = 35000;
   public static final int GL_WRITE_ONLY_ARB = 35001;
   public static final int GL_READ_WRITE_ARB = 35002;
   public static final int GL_BUFFER_SIZE_ARB = 34660;
   public static final int GL_BUFFER_USAGE_ARB = 34661;
   public static final int GL_BUFFER_ACCESS_ARB = 35003;
   public static final int GL_BUFFER_MAPPED_ARB = 35004;
   public static final int GL_BUFFER_MAP_POINTER_ARB = 35005;

   public static void glBindBufferARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindBufferARB;
      BufferChecks.checkFunctionAddress(var3);
      StateTracker.bindBuffer(var2, var0, var1);
      nglBindBufferARB(var0, var1, var3);
   }

   static native void nglBindBufferARB(int var0, int var1, long var2);

   public static void glDeleteBuffersARB(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteBuffersARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteBuffersARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteBuffersARB(int var0, long var1, long var3);

   public static void glDeleteBuffersARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteBuffersARB;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteBuffersARB(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenBuffersARB(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenBuffersARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenBuffersARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenBuffersARB(int var0, long var1, long var3);

   public static int glGenBuffersARB() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenBuffersARB;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenBuffersARB(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static boolean glIsBufferARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsBufferARB;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsBufferARB(var0, var2);
      return var4;
   }

   static native boolean nglIsBufferARB(int var0, long var1);

   public static void glBufferDataARB(int var0, long var1, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferDataARB;
      BufferChecks.checkFunctionAddress(var5);
      nglBufferDataARB(var0, var1, 0L, var3, var5);
   }

   public static void glBufferDataARB(int var0, ByteBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferDataARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferDataARB(var0, (long)var1.remaining(), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferDataARB(int var0, DoubleBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferDataARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferDataARB(var0, (long)(var1.remaining() << 3), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferDataARB(int var0, FloatBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferDataARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferDataARB(var0, (long)(var1.remaining() << 2), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferDataARB(int var0, IntBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferDataARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferDataARB(var0, (long)(var1.remaining() << 2), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferDataARB(int var0, ShortBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferDataARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferDataARB(var0, (long)(var1.remaining() << 1), MemoryUtil.getAddress(var1), var2, var4);
   }

   static native void nglBufferDataARB(int var0, long var1, long var3, int var5, long var6);

   public static void glBufferSubDataARB(int var0, long var1, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferSubDataARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglBufferSubDataARB(var0, var1, (long)var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   public static void glBufferSubDataARB(int var0, long var1, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferSubDataARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglBufferSubDataARB(var0, var1, (long)(var3.remaining() << 3), MemoryUtil.getAddress(var3), var5);
   }

   public static void glBufferSubDataARB(int var0, long var1, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferSubDataARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglBufferSubDataARB(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glBufferSubDataARB(int var0, long var1, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferSubDataARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglBufferSubDataARB(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glBufferSubDataARB(int var0, long var1, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferSubDataARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglBufferSubDataARB(var0, var1, (long)(var3.remaining() << 1), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglBufferSubDataARB(int var0, long var1, long var3, long var5, long var7);

   public static void glGetBufferSubDataARB(int var0, long var1, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetBufferSubDataARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetBufferSubDataARB(var0, var1, (long)var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetBufferSubDataARB(int var0, long var1, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetBufferSubDataARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetBufferSubDataARB(var0, var1, (long)(var3.remaining() << 3), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetBufferSubDataARB(int var0, long var1, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetBufferSubDataARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetBufferSubDataARB(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetBufferSubDataARB(int var0, long var1, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetBufferSubDataARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetBufferSubDataARB(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetBufferSubDataARB(int var0, long var1, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetBufferSubDataARB;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetBufferSubDataARB(var0, var1, (long)(var3.remaining() << 1), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetBufferSubDataARB(int var0, long var1, long var3, long var5, long var7);

   public static ByteBuffer glMapBufferARB(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMapBufferARB;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      ByteBuffer var6 = nglMapBufferARB(var0, var1, (long)GLChecks.getBufferObjectSizeARB(var3, var0), var2, var4);
      return LWJGLUtil.CHECKS && var6 == null ? null : var6.order(ByteOrder.nativeOrder());
   }

   public static ByteBuffer glMapBufferARB(int var0, int var1, long var2, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMapBufferARB;
      BufferChecks.checkFunctionAddress(var6);
      if (var4 != null) {
         BufferChecks.checkDirect(var4);
      }

      ByteBuffer var8 = nglMapBufferARB(var0, var1, var2, var4, var6);
      return LWJGLUtil.CHECKS && var8 == null ? null : var8.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglMapBufferARB(int var0, int var1, long var2, ByteBuffer var4, long var5);

   public static boolean glUnmapBufferARB(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glUnmapBufferARB;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglUnmapBufferARB(var0, var2);
      return var4;
   }

   static native boolean nglUnmapBufferARB(int var0, long var1);

   public static void glGetBufferParameterARB(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetBufferParameterivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetBufferParameterivARB(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetBufferParameterivARB(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static int glGetBufferParameterARB(int var0, int var1) {
      return glGetBufferParameteriARB(var0, var1);
   }

   public static int glGetBufferParameteriARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetBufferParameterivARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetBufferParameterivARB(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static ByteBuffer glGetBufferPointerARB(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetBufferPointervARB;
      BufferChecks.checkFunctionAddress(var3);
      ByteBuffer var5 = nglGetBufferPointervARB(var0, var1, (long)GLChecks.getBufferObjectSizeARB(var2, var0), var3);
      return LWJGLUtil.CHECKS && var5 == null ? null : var5.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetBufferPointervARB(int var0, int var1, long var2, long var4);
}
