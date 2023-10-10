package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class ARBGetProgramBinary {
   public static final int GL_PROGRAM_BINARY_RETRIEVABLE_HINT = 33367;
   public static final int GL_PROGRAM_BINARY_LENGTH = 34625;
   public static final int GL_NUM_PROGRAM_BINARY_FORMATS = 34814;
   public static final int GL_PROGRAM_BINARY_FORMATS = 34815;

   private ARBGetProgramBinary() {
   }

   public static void glGetProgramBinary(int var0, IntBuffer var1, IntBuffer var2, ByteBuffer var3) {
      GL41.glGetProgramBinary(var0, var1, var2, var3);
   }

   public static void glProgramBinary(int var0, int var1, ByteBuffer var2) {
      GL41.glProgramBinary(var0, var1, var2);
   }

   public static void glProgramParameteri(int var0, int var1, int var2) {
      GL41.glProgramParameteri(var0, var1, var2);
   }
}
