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

public final class GL15 {
   public static final int GL_ARRAY_BUFFER = 34962;
   public static final int GL_ELEMENT_ARRAY_BUFFER = 34963;
   public static final int GL_ARRAY_BUFFER_BINDING = 34964;
   public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING = 34965;
   public static final int GL_VERTEX_ARRAY_BUFFER_BINDING = 34966;
   public static final int GL_NORMAL_ARRAY_BUFFER_BINDING = 34967;
   public static final int GL_COLOR_ARRAY_BUFFER_BINDING = 34968;
   public static final int GL_INDEX_ARRAY_BUFFER_BINDING = 34969;
   public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING = 34970;
   public static final int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING = 34971;
   public static final int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING = 34972;
   public static final int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING = 34973;
   public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING = 34974;
   public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 34975;
   public static final int GL_STREAM_DRAW = 35040;
   public static final int GL_STREAM_READ = 35041;
   public static final int GL_STREAM_COPY = 35042;
   public static final int GL_STATIC_DRAW = 35044;
   public static final int GL_STATIC_READ = 35045;
   public static final int GL_STATIC_COPY = 35046;
   public static final int GL_DYNAMIC_DRAW = 35048;
   public static final int GL_DYNAMIC_READ = 35049;
   public static final int GL_DYNAMIC_COPY = 35050;
   public static final int GL_READ_ONLY = 35000;
   public static final int GL_WRITE_ONLY = 35001;
   public static final int GL_READ_WRITE = 35002;
   public static final int GL_BUFFER_SIZE = 34660;
   public static final int GL_BUFFER_USAGE = 34661;
   public static final int GL_BUFFER_ACCESS = 35003;
   public static final int GL_BUFFER_MAPPED = 35004;
   public static final int GL_BUFFER_MAP_POINTER = 35005;
   public static final int GL_FOG_COORD_SRC = 33872;
   public static final int GL_FOG_COORD = 33873;
   public static final int GL_CURRENT_FOG_COORD = 33875;
   public static final int GL_FOG_COORD_ARRAY_TYPE = 33876;
   public static final int GL_FOG_COORD_ARRAY_STRIDE = 33877;
   public static final int GL_FOG_COORD_ARRAY_POINTER = 33878;
   public static final int GL_FOG_COORD_ARRAY = 33879;
   public static final int GL_FOG_COORD_ARRAY_BUFFER_BINDING = 34973;
   public static final int GL_SRC0_RGB = 34176;
   public static final int GL_SRC1_RGB = 34177;
   public static final int GL_SRC2_RGB = 34178;
   public static final int GL_SRC0_ALPHA = 34184;
   public static final int GL_SRC1_ALPHA = 34185;
   public static final int GL_SRC2_ALPHA = 34186;
   public static final int GL_SAMPLES_PASSED = 35092;
   public static final int GL_QUERY_COUNTER_BITS = 34916;
   public static final int GL_CURRENT_QUERY = 34917;
   public static final int GL_QUERY_RESULT = 34918;
   public static final int GL_QUERY_RESULT_AVAILABLE = 34919;

   private GL15() {
   }

   public static void glBindBuffer(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindBuffer;
      BufferChecks.checkFunctionAddress(var3);
      StateTracker.bindBuffer(var2, var0, var1);
      nglBindBuffer(var0, var1, var3);
   }

   static native void nglBindBuffer(int var0, int var1, long var2);

   public static void glDeleteBuffers(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteBuffers;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteBuffers(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteBuffers(int var0, long var1, long var3);

   public static void glDeleteBuffers(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteBuffers;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteBuffers(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenBuffers(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenBuffers;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenBuffers(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenBuffers(int var0, long var1, long var3);

   public static int glGenBuffers() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenBuffers;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenBuffers(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static boolean glIsBuffer(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsBuffer;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsBuffer(var0, var2);
      return var4;
   }

   static native boolean nglIsBuffer(int var0, long var1);

   public static void glBufferData(int var0, long var1, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferData;
      BufferChecks.checkFunctionAddress(var5);
      nglBufferData(var0, var1, 0L, var3, var5);
   }

   public static void glBufferData(int var0, ByteBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferData;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferData(var0, (long)var1.remaining(), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferData(int var0, DoubleBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferData;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferData(var0, (long)(var1.remaining() << 3), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferData(int var0, FloatBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferData;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferData(var0, (long)(var1.remaining() << 2), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferData(int var0, IntBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferData;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferData(var0, (long)(var1.remaining() << 2), MemoryUtil.getAddress(var1), var2, var4);
   }

   public static void glBufferData(int var0, ShortBuffer var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glBufferData;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      nglBufferData(var0, (long)(var1.remaining() << 1), MemoryUtil.getAddress(var1), var2, var4);
   }

   static native void nglBufferData(int var0, long var1, long var3, int var5, long var6);

   public static void glBufferSubData(int var0, long var1, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferSubData;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglBufferSubData(var0, var1, (long)var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   public static void glBufferSubData(int var0, long var1, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferSubData;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglBufferSubData(var0, var1, (long)(var3.remaining() << 3), MemoryUtil.getAddress(var3), var5);
   }

   public static void glBufferSubData(int var0, long var1, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferSubData;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglBufferSubData(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glBufferSubData(int var0, long var1, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferSubData;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglBufferSubData(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glBufferSubData(int var0, long var1, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBufferSubData;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglBufferSubData(var0, var1, (long)(var3.remaining() << 1), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglBufferSubData(int var0, long var1, long var3, long var5, long var7);

   public static void glGetBufferSubData(int var0, long var1, ByteBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetBufferSubData;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetBufferSubData(var0, var1, (long)var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetBufferSubData(int var0, long var1, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetBufferSubData;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetBufferSubData(var0, var1, (long)(var3.remaining() << 3), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetBufferSubData(int var0, long var1, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetBufferSubData;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetBufferSubData(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetBufferSubData(int var0, long var1, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetBufferSubData;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetBufferSubData(var0, var1, (long)(var3.remaining() << 2), MemoryUtil.getAddress(var3), var5);
   }

   public static void glGetBufferSubData(int var0, long var1, ShortBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetBufferSubData;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetBufferSubData(var0, var1, (long)(var3.remaining() << 1), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetBufferSubData(int var0, long var1, long var3, long var5, long var7);

   public static ByteBuffer glMapBuffer(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glMapBuffer;
      BufferChecks.checkFunctionAddress(var4);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      ByteBuffer var6 = nglMapBuffer(var0, var1, (long)GLChecks.getBufferObjectSize(var3, var0), var2, var4);
      return LWJGLUtil.CHECKS && var6 == null ? null : var6.order(ByteOrder.nativeOrder());
   }

   public static ByteBuffer glMapBuffer(int var0, int var1, long var2, ByteBuffer var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glMapBuffer;
      BufferChecks.checkFunctionAddress(var6);
      if (var4 != null) {
         BufferChecks.checkDirect(var4);
      }

      ByteBuffer var8 = nglMapBuffer(var0, var1, var2, var4, var6);
      return LWJGLUtil.CHECKS && var8 == null ? null : var8.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglMapBuffer(int var0, int var1, long var2, ByteBuffer var4, long var5);

   public static boolean glUnmapBuffer(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glUnmapBuffer;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglUnmapBuffer(var0, var2);
      return var4;
   }

   static native boolean nglUnmapBuffer(int var0, long var1);

   public static void glGetBufferParameter(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetBufferParameteriv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetBufferParameteriv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetBufferParameteriv(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static int glGetBufferParameter(int var0, int var1) {
      return glGetBufferParameteri(var0, var1);
   }

   public static int glGetBufferParameteri(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetBufferParameteriv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetBufferParameteriv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static ByteBuffer glGetBufferPointer(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetBufferPointerv;
      BufferChecks.checkFunctionAddress(var3);
      ByteBuffer var5 = nglGetBufferPointerv(var0, var1, (long)GLChecks.getBufferObjectSize(var2, var0), var3);
      return LWJGLUtil.CHECKS && var5 == null ? null : var5.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglGetBufferPointerv(int var0, int var1, long var2, long var4);

   public static void glGenQueries(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenQueries;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenQueries(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenQueries(int var0, long var1, long var3);

   public static int glGenQueries() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenQueries;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenQueries(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static void glDeleteQueries(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteQueries;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteQueries(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteQueries(int var0, long var1, long var3);

   public static void glDeleteQueries(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteQueries;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteQueries(1, APIUtil.getInt(var1, var0), var2);
   }

   public static boolean glIsQuery(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsQuery;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsQuery(var0, var2);
      return var4;
   }

   static native boolean nglIsQuery(int var0, long var1);

   public static void glBeginQuery(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBeginQuery;
      BufferChecks.checkFunctionAddress(var3);
      nglBeginQuery(var0, var1, var3);
   }

   static native void nglBeginQuery(int var0, int var1, long var2);

   public static void glEndQuery(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEndQuery;
      BufferChecks.checkFunctionAddress(var2);
      nglEndQuery(var0, var2);
   }

   static native void nglEndQuery(int var0, long var1);

   public static void glGetQuery(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetQueryiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetQueryiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetQueryiv(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static int glGetQuery(int var0, int var1) {
      return glGetQueryi(var0, var1);
   }

   public static int glGetQueryi(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetQueryiv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetQueryiv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetQueryObject(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetQueryObjectiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetQueryObjectiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetQueryObjectiv(int var0, int var1, long var2, long var4);

   public static int glGetQueryObjecti(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetQueryObjectiv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetQueryObjectiv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetQueryObjectu(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetQueryObjectuiv;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetQueryObjectuiv(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetQueryObjectuiv(int var0, int var1, long var2, long var4);

   public static int glGetQueryObjectui(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetQueryObjectuiv;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetQueryObjectuiv(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
