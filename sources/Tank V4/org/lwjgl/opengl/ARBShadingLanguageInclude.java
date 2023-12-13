package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBShadingLanguageInclude {
   public static final int GL_SHADER_INCLUDE_ARB = 36270;
   public static final int GL_NAMED_STRING_LENGTH_ARB = 36329;
   public static final int GL_NAMED_STRING_TYPE_ARB = 36330;

   private ARBShadingLanguageInclude() {
   }

   public static void glNamedStringARB(int var0, ByteBuffer var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedStringARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkDirect(var2);
      nglNamedStringARB(var0, var1.remaining(), MemoryUtil.getAddress(var1), var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglNamedStringARB(int var0, int var1, long var2, int var4, long var5, long var7);

   public static void glNamedStringARB(int var0, CharSequence var1, CharSequence var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glNamedStringARB;
      BufferChecks.checkFunctionAddress(var4);
      nglNamedStringARB(var0, var1.length(), APIUtil.getBuffer(var3, var1), var2.length(), APIUtil.getBuffer(var3, var2, var1.length()), var4);
   }

   public static void glDeleteNamedStringARB(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteNamedStringARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteNamedStringARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteNamedStringARB(int var0, long var1, long var3);

   public static void glDeleteNamedStringARB(CharSequence var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteNamedStringARB;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteNamedStringARB(var0.length(), APIUtil.getBuffer(var1, var0), var2);
   }

   public static void glCompileShaderIncludeARB(int var0, int var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glCompileShaderIncludeARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      BufferChecks.checkNullTerminated(var2, var1);
      nglCompileShaderIncludeARB(var0, var1, MemoryUtil.getAddress(var2), 0L, var4);
   }

   static native void nglCompileShaderIncludeARB(int var0, int var1, long var2, long var4, long var6);

   public static void glCompileShaderIncludeARB(int var0, CharSequence[] var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCompileShaderIncludeARB;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkArray(var1);
      nglCompileShaderIncludeARB2(var0, var1.length, APIUtil.getBuffer(var2, var1), APIUtil.getLengths(var2, var1), var3);
   }

   static native void nglCompileShaderIncludeARB2(int var0, int var1, long var2, long var4, long var6);

   public static boolean glIsNamedStringARB(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsNamedStringARB;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      boolean var4 = nglIsNamedStringARB(var0.remaining(), MemoryUtil.getAddress(var0), var2);
      return var4;
   }

   static native boolean nglIsNamedStringARB(int var0, long var1, long var3);

   public static boolean glIsNamedStringARB(CharSequence var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsNamedStringARB;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsNamedStringARB(var0.length(), APIUtil.getBuffer(var1, var0), var2);
      return var4;
   }

   public static void glGetNamedStringARB(ByteBuffer var0, IntBuffer var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetNamedStringARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var0);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkDirect(var2);
      nglGetNamedStringARB(var0.remaining(), MemoryUtil.getAddress(var0), var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetNamedStringARB(int var0, long var1, int var3, long var4, long var6, long var8);

   public static void glGetNamedStringARB(CharSequence var0, IntBuffer var1, ByteBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetNamedStringARB;
      BufferChecks.checkFunctionAddress(var4);
      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      BufferChecks.checkDirect(var2);
      nglGetNamedStringARB(var0.length(), APIUtil.getBuffer(var3, var0), var2.remaining(), MemoryUtil.getAddressSafe(var1), MemoryUtil.getAddress(var2), var4);
   }

   public static String glGetNamedStringARB(CharSequence var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetNamedStringARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getLengths(var2);
      ByteBuffer var6 = APIUtil.getBufferByte(var2, var1 + var0.length());
      nglGetNamedStringARB(var0.length(), APIUtil.getBuffer(var2, var0), var1, MemoryUtil.getAddress0((Buffer)var5), MemoryUtil.getAddress(var6), var3);
      var6.limit(var0.length() + var5.get(0));
      return APIUtil.getString(var2, var6);
   }

   public static void glGetNamedStringARB(ByteBuffer var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetNamedStringivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var0);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetNamedStringivARB(var0.remaining(), MemoryUtil.getAddress(var0), var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetNamedStringivARB(int var0, long var1, int var3, long var4, long var6);

   public static void glGetNamedStringiARB(CharSequence var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetNamedStringivARB;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nglGetNamedStringivARB(var0.length(), APIUtil.getBuffer(var3, var0), var1, MemoryUtil.getAddress(var2), var4);
   }

   public static int glGetNamedStringiARB(CharSequence var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetNamedStringivARB;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetNamedStringivARB(var0.length(), APIUtil.getBuffer(var2, var0), var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
