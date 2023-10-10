package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public class NVProgram {
   public static final int GL_PROGRAM_TARGET_NV = 34374;
   public static final int GL_PROGRAM_LENGTH_NV = 34343;
   public static final int GL_PROGRAM_RESIDENT_NV = 34375;
   public static final int GL_PROGRAM_STRING_NV = 34344;
   public static final int GL_PROGRAM_ERROR_POSITION_NV = 34379;
   public static final int GL_PROGRAM_ERROR_STRING_NV = 34932;

   public static void glLoadProgramNV(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glLoadProgramNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglLoadProgramNV(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglLoadProgramNV(int var0, int var1, int var2, long var3, long var5);

   public static void glLoadProgramNV(int var0, int var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glLoadProgramNV;
      BufferChecks.checkFunctionAddress(var4);
      nglLoadProgramNV(var0, var1, var2.length(), APIUtil.getBuffer(var3, var2), var4);
   }

   public static void glBindProgramNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindProgramNV;
      BufferChecks.checkFunctionAddress(var3);
      nglBindProgramNV(var0, var1, var3);
   }

   static native void nglBindProgramNV(int var0, int var1, long var2);

   public static void glDeleteProgramsNV(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteProgramsNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteProgramsNV(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteProgramsNV(int var0, long var1, long var3);

   public static void glDeleteProgramsNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteProgramsNV;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteProgramsNV(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenProgramsNV(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenProgramsNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenProgramsNV(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenProgramsNV(int var0, long var1, long var3);

   public static int glGenProgramsNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenProgramsNV;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenProgramsNV(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static void glGetProgramNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetProgramivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramivNV(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static int glGetProgramNV(int var0, int var1) {
      return glGetProgramiNV(var0, var1);
   }

   public static int glGetProgramiNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetProgramivNV;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetProgramivNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetProgramStringNV(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramStringNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglGetProgramStringNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramStringNV(int var0, int var1, long var2, long var4);

   public static String glGetProgramStringNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetProgramStringNV;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = glGetProgramiNV(var0, 34343);
      ByteBuffer var6 = APIUtil.getBufferByte(var2, var5);
      nglGetProgramStringNV(var0, var1, MemoryUtil.getAddress(var6), var3);
      var6.limit(var5);
      return APIUtil.getString(var2, var6);
   }

   public static boolean glIsProgramNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsProgramNV;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsProgramNV(var0, var2);
      return var4;
   }

   static native boolean nglIsProgramNV(int var0, long var1);

   public static boolean glAreProgramsResidentNV(IntBuffer var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glAreProgramsResidentNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var0);
      BufferChecks.checkBuffer(var1, var0.remaining());
      boolean var5 = nglAreProgramsResidentNV(var0.remaining(), MemoryUtil.getAddress(var0), MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native boolean nglAreProgramsResidentNV(int var0, long var1, long var3, long var5);

   public static void glRequestResidentProgramsNV(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glRequestResidentProgramsNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglRequestResidentProgramsNV(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglRequestResidentProgramsNV(int var0, long var1, long var3);

   public static void glRequestResidentProgramsNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glRequestResidentProgramsNV;
      BufferChecks.checkFunctionAddress(var2);
      nglRequestResidentProgramsNV(1, APIUtil.getInt(var1, var0), var2);
   }
}
