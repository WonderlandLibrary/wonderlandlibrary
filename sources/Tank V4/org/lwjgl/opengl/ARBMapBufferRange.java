package org.lwjgl.opengl;

import java.nio.ByteBuffer;

public final class ARBMapBufferRange {
   public static final int GL_MAP_READ_BIT = 1;
   public static final int GL_MAP_WRITE_BIT = 2;
   public static final int GL_MAP_INVALIDATE_RANGE_BIT = 4;
   public static final int GL_MAP_INVALIDATE_BUFFER_BIT = 8;
   public static final int GL_MAP_FLUSH_EXPLICIT_BIT = 16;
   public static final int GL_MAP_UNSYNCHRONIZED_BIT = 32;

   private ARBMapBufferRange() {
   }

   public static ByteBuffer glMapBufferRange(int var0, long var1, long var3, int var5, ByteBuffer var6) {
      return GL30.glMapBufferRange(var0, var1, var3, var5, var6);
   }

   public static void glFlushMappedBufferRange(int var0, long var1, long var3) {
      GL30.glFlushMappedBufferRange(var0, var1, var3);
   }
}
