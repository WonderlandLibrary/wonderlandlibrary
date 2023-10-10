package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class APPLEVertexArrayRange {
   public static final int GL_VERTEX_ARRAY_RANGE_APPLE = 34077;
   public static final int GL_VERTEX_ARRAY_RANGE_LENGTH_APPLE = 34078;
   public static final int GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_APPLE = 34080;
   public static final int GL_VERTEX_ARRAY_RANGE_POINTER_APPLE = 34081;
   public static final int GL_VERTEX_ARRAY_STORAGE_HINT_APPLE = 34079;
   public static final int GL_STORAGE_CACHED_APPLE = 34238;
   public static final int GL_STORAGE_SHARED_APPLE = 34239;
   public static final int GL_DRAW_PIXELS_APPLE = 35338;
   public static final int GL_FENCE_APPLE = 35339;

   private APPLEVertexArrayRange() {
   }

   public static void glVertexArrayRangeAPPLE(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glVertexArrayRangeAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglVertexArrayRangeAPPLE(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglVertexArrayRangeAPPLE(int var0, long var1, long var3);

   public static void glFlushVertexArrayRangeAPPLE(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glFlushVertexArrayRangeAPPLE;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglFlushVertexArrayRangeAPPLE(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglFlushVertexArrayRangeAPPLE(int var0, long var1, long var3);

   public static void glVertexArrayParameteriAPPLE(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexArrayParameteriAPPLE;
      BufferChecks.checkFunctionAddress(var3);
      nglVertexArrayParameteriAPPLE(var0, var1, var3);
   }

   static native void nglVertexArrayParameteriAPPLE(int var0, int var1, long var2);
}
