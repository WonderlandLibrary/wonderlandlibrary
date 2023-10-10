package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class ARBShaderSubroutine {
   public static final int GL_ACTIVE_SUBROUTINES = 36325;
   public static final int GL_ACTIVE_SUBROUTINE_UNIFORMS = 36326;
   public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS = 36423;
   public static final int GL_ACTIVE_SUBROUTINE_MAX_LENGTH = 36424;
   public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH = 36425;
   public static final int GL_MAX_SUBROUTINES = 36327;
   public static final int GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS = 36328;
   public static final int GL_NUM_COMPATIBLE_SUBROUTINES = 36426;
   public static final int GL_COMPATIBLE_SUBROUTINES = 36427;
   public static final int GL_UNIFORM_SIZE = 35384;
   public static final int GL_UNIFORM_NAME_LENGTH = 35385;

   private ARBShaderSubroutine() {
   }

   public static int glGetSubroutineUniformLocation(int var0, int var1, ByteBuffer var2) {
      return GL40.glGetSubroutineUniformLocation(var0, var1, var2);
   }

   public static int glGetSubroutineUniformLocation(int var0, int var1, CharSequence var2) {
      return GL40.glGetSubroutineUniformLocation(var0, var1, var2);
   }

   public static int glGetSubroutineIndex(int var0, int var1, ByteBuffer var2) {
      return GL40.glGetSubroutineIndex(var0, var1, var2);
   }

   public static int glGetSubroutineIndex(int var0, int var1, CharSequence var2) {
      return GL40.glGetSubroutineIndex(var0, var1, var2);
   }

   public static void glGetActiveSubroutineUniform(int var0, int var1, int var2, int var3, IntBuffer var4) {
      GL40.glGetActiveSubroutineUniform(var0, var1, var2, var3, var4);
   }

   public static int glGetActiveSubroutineUniformi(int var0, int var1, int var2, int var3) {
      return GL40.glGetActiveSubroutineUniformi(var0, var1, var2, var3);
   }

   public static void glGetActiveSubroutineUniformName(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4) {
      GL40.glGetActiveSubroutineUniformName(var0, var1, var2, var3, var4);
   }

   public static String glGetActiveSubroutineUniformName(int var0, int var1, int var2, int var3) {
      return GL40.glGetActiveSubroutineUniformName(var0, var1, var2, var3);
   }

   public static void glGetActiveSubroutineName(int var0, int var1, int var2, IntBuffer var3, ByteBuffer var4) {
      GL40.glGetActiveSubroutineName(var0, var1, var2, var3, var4);
   }

   public static String glGetActiveSubroutineName(int var0, int var1, int var2, int var3) {
      return GL40.glGetActiveSubroutineName(var0, var1, var2, var3);
   }

   public static void glUniformSubroutinesu(int var0, IntBuffer var1) {
      GL40.glUniformSubroutinesu(var0, var1);
   }

   public static void glGetUniformSubroutineu(int var0, int var1, IntBuffer var2) {
      GL40.glGetUniformSubroutineu(var0, var1, var2);
   }

   public static int glGetUniformSubroutineui(int var0, int var1) {
      return GL40.glGetUniformSubroutineui(var0, var1);
   }

   public static void glGetProgramStage(int var0, int var1, int var2, IntBuffer var3) {
      GL40.glGetProgramStage(var0, var1, var2, var3);
   }

   public static int glGetProgramStagei(int var0, int var1, int var2) {
      return GL40.glGetProgramStagei(var0, var1, var2);
   }
}
