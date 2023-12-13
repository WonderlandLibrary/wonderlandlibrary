package org.lwjgl.opengl;

import java.nio.IntBuffer;

public final class ARBFramebufferObject {
   public static final int GL_FRAMEBUFFER = 36160;
   public static final int GL_READ_FRAMEBUFFER = 36008;
   public static final int GL_DRAW_FRAMEBUFFER = 36009;
   public static final int GL_RENDERBUFFER = 36161;
   public static final int GL_STENCIL_INDEX1 = 36166;
   public static final int GL_STENCIL_INDEX4 = 36167;
   public static final int GL_STENCIL_INDEX8 = 36168;
   public static final int GL_STENCIL_INDEX16 = 36169;
   public static final int GL_RENDERBUFFER_WIDTH = 36162;
   public static final int GL_RENDERBUFFER_HEIGHT = 36163;
   public static final int GL_RENDERBUFFER_INTERNAL_FORMAT = 36164;
   public static final int GL_RENDERBUFFER_RED_SIZE = 36176;
   public static final int GL_RENDERBUFFER_GREEN_SIZE = 36177;
   public static final int GL_RENDERBUFFER_BLUE_SIZE = 36178;
   public static final int GL_RENDERBUFFER_ALPHA_SIZE = 36179;
   public static final int GL_RENDERBUFFER_DEPTH_SIZE = 36180;
   public static final int GL_RENDERBUFFER_STENCIL_SIZE = 36181;
   public static final int GL_RENDERBUFFER_SAMPLES = 36011;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 36048;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 36049;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 36050;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 36051;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 36052;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 33296;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 33297;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE = 33298;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 33299;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 33300;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 33301;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 33302;
   public static final int GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 33303;
   public static final int GL_SRGB = 35904;
   public static final int GL_UNSIGNED_NORMALIZED = 35863;
   public static final int GL_FRAMEBUFFER_DEFAULT = 33304;
   public static final int GL_INDEX = 33314;
   public static final int GL_COLOR_ATTACHMENT0 = 36064;
   public static final int GL_COLOR_ATTACHMENT1 = 36065;
   public static final int GL_COLOR_ATTACHMENT2 = 36066;
   public static final int GL_COLOR_ATTACHMENT3 = 36067;
   public static final int GL_COLOR_ATTACHMENT4 = 36068;
   public static final int GL_COLOR_ATTACHMENT5 = 36069;
   public static final int GL_COLOR_ATTACHMENT6 = 36070;
   public static final int GL_COLOR_ATTACHMENT7 = 36071;
   public static final int GL_COLOR_ATTACHMENT8 = 36072;
   public static final int GL_COLOR_ATTACHMENT9 = 36073;
   public static final int GL_COLOR_ATTACHMENT10 = 36074;
   public static final int GL_COLOR_ATTACHMENT11 = 36075;
   public static final int GL_COLOR_ATTACHMENT12 = 36076;
   public static final int GL_COLOR_ATTACHMENT13 = 36077;
   public static final int GL_COLOR_ATTACHMENT14 = 36078;
   public static final int GL_COLOR_ATTACHMENT15 = 36079;
   public static final int GL_DEPTH_ATTACHMENT = 36096;
   public static final int GL_STENCIL_ATTACHMENT = 36128;
   public static final int GL_DEPTH_STENCIL_ATTACHMENT = 33306;
   public static final int GL_MAX_SAMPLES = 36183;
   public static final int GL_FRAMEBUFFER_COMPLETE = 36053;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 36059;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 36060;
   public static final int GL_FRAMEBUFFER_UNSUPPORTED = 36061;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 36182;
   public static final int GL_FRAMEBUFFER_UNDEFINED = 33305;
   public static final int GL_FRAMEBUFFER_BINDING = 36006;
   public static final int GL_DRAW_FRAMEBUFFER_BINDING = 36006;
   public static final int GL_READ_FRAMEBUFFER_BINDING = 36010;
   public static final int GL_RENDERBUFFER_BINDING = 36007;
   public static final int GL_MAX_COLOR_ATTACHMENTS = 36063;
   public static final int GL_MAX_RENDERBUFFER_SIZE = 34024;
   public static final int GL_INVALID_FRAMEBUFFER_OPERATION = 1286;
   public static final int GL_DEPTH_STENCIL = 34041;
   public static final int GL_UNSIGNED_INT_24_8 = 34042;
   public static final int GL_DEPTH24_STENCIL8 = 35056;
   public static final int GL_TEXTURE_STENCIL_SIZE = 35057;

   private ARBFramebufferObject() {
   }

   public static boolean glIsRenderbuffer(int var0) {
      return GL30.glIsRenderbuffer(var0);
   }

   public static void glBindRenderbuffer(int var0, int var1) {
      GL30.glBindRenderbuffer(var0, var1);
   }

   public static void glDeleteRenderbuffers(IntBuffer var0) {
      GL30.glDeleteRenderbuffers(var0);
   }

   public static void glDeleteRenderbuffers(int var0) {
      GL30.glDeleteRenderbuffers(var0);
   }

   public static void glGenRenderbuffers(IntBuffer var0) {
      GL30.glGenRenderbuffers(var0);
   }

   public static int glGenRenderbuffers() {
      return GL30.glGenRenderbuffers();
   }

   public static void glRenderbufferStorage(int var0, int var1, int var2, int var3) {
      GL30.glRenderbufferStorage(var0, var1, var2, var3);
   }

   public static void glRenderbufferStorageMultisample(int var0, int var1, int var2, int var3, int var4) {
      GL30.glRenderbufferStorageMultisample(var0, var1, var2, var3, var4);
   }

   public static void glGetRenderbufferParameter(int var0, int var1, IntBuffer var2) {
      GL30.glGetRenderbufferParameter(var0, var1, var2);
   }

   /** @deprecated */
   @Deprecated
   public static int glGetRenderbufferParameter(int var0, int var1) {
      return glGetRenderbufferParameteri(var0, var1);
   }

   public static int glGetRenderbufferParameteri(int var0, int var1) {
      return GL30.glGetRenderbufferParameteri(var0, var1);
   }

   public static boolean glIsFramebuffer(int var0) {
      return GL30.glIsFramebuffer(var0);
   }

   public static void glBindFramebuffer(int var0, int var1) {
      GL30.glBindFramebuffer(var0, var1);
   }

   public static void glDeleteFramebuffers(IntBuffer var0) {
      GL30.glDeleteFramebuffers(var0);
   }

   public static void glDeleteFramebuffers(int var0) {
      GL30.glDeleteFramebuffers(var0);
   }

   public static void glGenFramebuffers(IntBuffer var0) {
      GL30.glGenFramebuffers(var0);
   }

   public static int glGenFramebuffers() {
      return GL30.glGenFramebuffers();
   }

   public static int glCheckFramebufferStatus(int var0) {
      return GL30.glCheckFramebufferStatus(var0);
   }

   public static void glFramebufferTexture1D(int var0, int var1, int var2, int var3, int var4) {
      GL30.glFramebufferTexture1D(var0, var1, var2, var3, var4);
   }

   public static void glFramebufferTexture2D(int var0, int var1, int var2, int var3, int var4) {
      GL30.glFramebufferTexture2D(var0, var1, var2, var3, var4);
   }

   public static void glFramebufferTexture3D(int var0, int var1, int var2, int var3, int var4, int var5) {
      GL30.glFramebufferTexture3D(var0, var1, var2, var3, var4, var5);
   }

   public static void glFramebufferTextureLayer(int var0, int var1, int var2, int var3, int var4) {
      GL30.glFramebufferTextureLayer(var0, var1, var2, var3, var4);
   }

   public static void glFramebufferRenderbuffer(int var0, int var1, int var2, int var3) {
      GL30.glFramebufferRenderbuffer(var0, var1, var2, var3);
   }

   public static void glGetFramebufferAttachmentParameter(int var0, int var1, int var2, IntBuffer var3) {
      GL30.glGetFramebufferAttachmentParameter(var0, var1, var2, var3);
   }

   /** @deprecated */
   @Deprecated
   public static int glGetFramebufferAttachmentParameter(int var0, int var1, int var2) {
      return GL30.glGetFramebufferAttachmentParameteri(var0, var1, var2);
   }

   public static int glGetFramebufferAttachmentParameteri(int var0, int var1, int var2) {
      return GL30.glGetFramebufferAttachmentParameteri(var0, var1, var2);
   }

   public static void glBlitFramebuffer(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      GL30.glBlitFramebuffer(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public static void glGenerateMipmap(int var0) {
      GL30.glGenerateMipmap(var0);
   }
}
