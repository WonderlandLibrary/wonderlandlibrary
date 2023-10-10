package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTTextureBufferObject {
   public static final int GL_TEXTURE_BUFFER_EXT = 35882;
   public static final int GL_MAX_TEXTURE_BUFFER_SIZE_EXT = 35883;
   public static final int GL_TEXTURE_BINDING_BUFFER_EXT = 35884;
   public static final int GL_TEXTURE_BUFFER_DATA_STORE_BINDING_EXT = 35885;
   public static final int GL_TEXTURE_BUFFER_FORMAT_EXT = 35886;

   private EXTTextureBufferObject() {
   }

   public static void glTexBufferEXT(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexBufferEXT;
      BufferChecks.checkFunctionAddress(var4);
      nglTexBufferEXT(var0, var1, var2, var4);
   }

   static native void nglTexBufferEXT(int var0, int var1, int var2, long var3);
}
