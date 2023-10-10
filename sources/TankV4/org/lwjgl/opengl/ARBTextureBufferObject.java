package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBTextureBufferObject {
   public static final int GL_TEXTURE_BUFFER_ARB = 35882;
   public static final int GL_MAX_TEXTURE_BUFFER_SIZE_ARB = 35883;
   public static final int GL_TEXTURE_BINDING_BUFFER_ARB = 35884;
   public static final int GL_TEXTURE_BUFFER_DATA_STORE_BINDING_ARB = 35885;
   public static final int GL_TEXTURE_BUFFER_FORMAT_ARB = 35886;

   private ARBTextureBufferObject() {
   }

   public static void glTexBufferARB(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexBufferARB;
      BufferChecks.checkFunctionAddress(var4);
      nglTexBufferARB(var0, var1, var2, var4);
   }

   static native void nglTexBufferARB(int var0, int var1, int var2, long var3);
}
