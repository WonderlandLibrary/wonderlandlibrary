package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTFramebufferObject {
   public static final int GL_FRAMEBUFFER_EXT = 36160;
   public static final int GL_RENDERBUFFER_EXT = 36161;
   public static final int GL_STENCIL_INDEX1_EXT = 36166;
   public static final int GL_STENCIL_INDEX4_EXT = 36167;
   public static final int GL_STENCIL_INDEX8_EXT = 36168;
   public static final int GL_STENCIL_INDEX16_EXT = 36169;
   public static final int GL_RENDERBUFFER_WIDTH_EXT = 36162;
   public static final int GL_RENDERBUFFER_HEIGHT_EXT = 36163;
   public static final int GL_RENDERBUFFER_INTERNAL_FORMAT_EXT = 36164;
   public static final int GL_RENDERBUFFER_RED_SIZE_EXT = 36176;
   public static final int GL_RENDERBUFFER_GREEN_SIZE_EXT = 36177;
   public static final int GL_RENDERBUFFER_BLUE_SIZE_EXT = 36178;
   public static final int GL_RENDERBUFFER_ALPHA_SIZE_EXT = 36179;
   public static final int GL_RENDERBUFFER_DEPTH_SIZE_EXT = 36180;
   public static final int GL_RENDERBUFFER_STENCIL_SIZE_EXT = 36181;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_EXT = 36048;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_EXT = 36049;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_EXT = 36050;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_EXT = 36051;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET_EXT = 36052;
   public static final int GL_COLOR_ATTACHMENT0_EXT = 36064;
   public static final int GL_COLOR_ATTACHMENT1_EXT = 36065;
   public static final int GL_COLOR_ATTACHMENT2_EXT = 36066;
   public static final int GL_COLOR_ATTACHMENT3_EXT = 36067;
   public static final int GL_COLOR_ATTACHMENT4_EXT = 36068;
   public static final int GL_COLOR_ATTACHMENT5_EXT = 36069;
   public static final int GL_COLOR_ATTACHMENT6_EXT = 36070;
   public static final int GL_COLOR_ATTACHMENT7_EXT = 36071;
   public static final int GL_COLOR_ATTACHMENT8_EXT = 36072;
   public static final int GL_COLOR_ATTACHMENT9_EXT = 36073;
   public static final int GL_COLOR_ATTACHMENT10_EXT = 36074;
   public static final int GL_COLOR_ATTACHMENT11_EXT = 36075;
   public static final int GL_COLOR_ATTACHMENT12_EXT = 36076;
   public static final int GL_COLOR_ATTACHMENT13_EXT = 36077;
   public static final int GL_COLOR_ATTACHMENT14_EXT = 36078;
   public static final int GL_COLOR_ATTACHMENT15_EXT = 36079;
   public static final int GL_DEPTH_ATTACHMENT_EXT = 36096;
   public static final int GL_STENCIL_ATTACHMENT_EXT = 36128;
   public static final int GL_FRAMEBUFFER_COMPLETE_EXT = 36053;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT = 36054;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT = 36055;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT = 36057;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT = 36058;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT = 36059;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT = 36060;
   public static final int GL_FRAMEBUFFER_UNSUPPORTED_EXT = 36061;
   public static final int GL_FRAMEBUFFER_BINDING_EXT = 36006;
   public static final int GL_RENDERBUFFER_BINDING_EXT = 36007;
   public static final int GL_MAX_COLOR_ATTACHMENTS_EXT = 36063;
   public static final int GL_MAX_RENDERBUFFER_SIZE_EXT = 34024;
   public static final int GL_INVALID_FRAMEBUFFER_OPERATION_EXT = 1286;

   private EXTFramebufferObject() {
   }

   public static boolean glIsRenderbufferEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsRenderbufferEXT;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsRenderbufferEXT(var0, var2);
      return var4;
   }

   static native boolean nglIsRenderbufferEXT(int var0, long var1);

   public static void glBindRenderbufferEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindRenderbufferEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglBindRenderbufferEXT(var0, var1, var3);
   }

   static native void nglBindRenderbufferEXT(int var0, int var1, long var2);

   public static void glDeleteRenderbuffersEXT(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteRenderbuffersEXT;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteRenderbuffersEXT(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteRenderbuffersEXT(int var0, long var1, long var3);

   public static void glDeleteRenderbuffersEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteRenderbuffersEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteRenderbuffersEXT(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenRenderbuffersEXT(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenRenderbuffersEXT;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenRenderbuffersEXT(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenRenderbuffersEXT(int var0, long var1, long var3);

   public static int glGenRenderbuffersEXT() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenRenderbuffersEXT;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenRenderbuffersEXT(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static void glRenderbufferStorageEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glRenderbufferStorageEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglRenderbufferStorageEXT(var0, var1, var2, var3, var5);
   }

   static native void nglRenderbufferStorageEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glGetRenderbufferParameterEXT(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetRenderbufferParameterivEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetRenderbufferParameterivEXT(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetRenderbufferParameterivEXT(int var0, int var1, long var2, long var4);

   /** @deprecated */
   @Deprecated
   public static int glGetRenderbufferParameterEXT(int var0, int var1) {
      return glGetRenderbufferParameteriEXT(var0, var1);
   }

   public static int glGetRenderbufferParameteriEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetRenderbufferParameterivEXT;
      BufferChecks.checkFunctionAddress(var3);
      IntBuffer var5 = APIUtil.getBufferInt(var2);
      nglGetRenderbufferParameterivEXT(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static boolean glIsFramebufferEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsFramebufferEXT;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsFramebufferEXT(var0, var2);
      return var4;
   }

   static native boolean nglIsFramebufferEXT(int var0, long var1);

   public static void glBindFramebufferEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBindFramebufferEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglBindFramebufferEXT(var0, var1, var3);
   }

   static native void nglBindFramebufferEXT(int var0, int var1, long var2);

   public static void glDeleteFramebuffersEXT(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteFramebuffersEXT;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglDeleteFramebuffersEXT(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglDeleteFramebuffersEXT(int var0, long var1, long var3);

   public static void glDeleteFramebuffersEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glDeleteFramebuffersEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglDeleteFramebuffersEXT(1, APIUtil.getInt(var1, var0), var2);
   }

   public static void glGenFramebuffersEXT(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenFramebuffersEXT;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglGenFramebuffersEXT(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   static native void nglGenFramebuffersEXT(int var0, long var1, long var3);

   public static int glGenFramebuffersEXT() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glGenFramebuffersEXT;
      BufferChecks.checkFunctionAddress(var1);
      IntBuffer var3 = APIUtil.getBufferInt(var0);
      nglGenFramebuffersEXT(1, MemoryUtil.getAddress(var3), var1);
      return var3.get(0);
   }

   public static int glCheckFramebufferStatusEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glCheckFramebufferStatusEXT;
      BufferChecks.checkFunctionAddress(var2);
      int var4 = nglCheckFramebufferStatusEXT(var0, var2);
      return var4;
   }

   static native int nglCheckFramebufferStatusEXT(int var0, long var1);

   public static void glFramebufferTexture1DEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glFramebufferTexture1DEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglFramebufferTexture1DEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglFramebufferTexture1DEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glFramebufferTexture2DEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glFramebufferTexture2DEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglFramebufferTexture2DEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglFramebufferTexture2DEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glFramebufferTexture3DEXT(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glFramebufferTexture3DEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglFramebufferTexture3DEXT(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglFramebufferTexture3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glFramebufferRenderbufferEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glFramebufferRenderbufferEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglFramebufferRenderbufferEXT(var0, var1, var2, var3, var5);
   }

   static native void nglFramebufferRenderbufferEXT(int var0, int var1, int var2, int var3, long var4);

   public static void glGetFramebufferAttachmentParameterEXT(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetFramebufferAttachmentParameterivEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 4);
      nglGetFramebufferAttachmentParameterivEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetFramebufferAttachmentParameterivEXT(int var0, int var1, int var2, long var3, long var5);

   /** @deprecated */
   @Deprecated
   public static int glGetFramebufferAttachmentParameterEXT(int var0, int var1, int var2) {
      return glGetFramebufferAttachmentParameteriEXT(var0, var1, var2);
   }

   public static int glGetFramebufferAttachmentParameteriEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetFramebufferAttachmentParameterivEXT;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetFramebufferAttachmentParameterivEXT(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glGenerateMipmapEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGenerateMipmapEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglGenerateMipmapEXT(var0, var2);
   }

   static native void nglGenerateMipmapEXT(int var0, long var1);
}
