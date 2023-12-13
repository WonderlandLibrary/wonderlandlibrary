package org.lwjgl.opengl;

import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVVertexBufferUnifiedMemory {
   public static final int GL_VERTEX_ATTRIB_ARRAY_UNIFIED_NV = 36638;
   public static final int GL_ELEMENT_ARRAY_UNIFIED_NV = 36639;
   public static final int GL_VERTEX_ATTRIB_ARRAY_ADDRESS_NV = 36640;
   public static final int GL_TEXTURE_COORD_ARRAY_ADDRESS_NV = 36645;
   public static final int GL_VERTEX_ARRAY_ADDRESS_NV = 36641;
   public static final int GL_NORMAL_ARRAY_ADDRESS_NV = 36642;
   public static final int GL_COLOR_ARRAY_ADDRESS_NV = 36643;
   public static final int GL_INDEX_ARRAY_ADDRESS_NV = 36644;
   public static final int GL_EDGE_FLAG_ARRAY_ADDRESS_NV = 36646;
   public static final int GL_SECONDARY_COLOR_ARRAY_ADDRESS_NV = 36647;
   public static final int GL_FOG_COORD_ARRAY_ADDRESS_NV = 36648;
   public static final int GL_ELEMENT_ARRAY_ADDRESS_NV = 36649;
   public static final int GL_VERTEX_ATTRIB_ARRAY_LENGTH_NV = 36650;
   public static final int GL_TEXTURE_COORD_ARRAY_LENGTH_NV = 36655;
   public static final int GL_VERTEX_ARRAY_LENGTH_NV = 36651;
   public static final int GL_NORMAL_ARRAY_LENGTH_NV = 36652;
   public static final int GL_COLOR_ARRAY_LENGTH_NV = 36653;
   public static final int GL_INDEX_ARRAY_LENGTH_NV = 36654;
   public static final int GL_EDGE_FLAG_ARRAY_LENGTH_NV = 36656;
   public static final int GL_SECONDARY_COLOR_ARRAY_LENGTH_NV = 36657;
   public static final int GL_FOG_COORD_ARRAY_LENGTH_NV = 36658;
   public static final int GL_ELEMENT_ARRAY_LENGTH_NV = 36659;

   private NVVertexBufferUnifiedMemory() {
   }

   public static void glBufferAddressRangeNV(int var0, int var1, long var2, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glBufferAddressRangeNV;
      BufferChecks.checkFunctionAddress(var7);
      nglBufferAddressRangeNV(var0, var1, var2, var4, var7);
   }

   static native void nglBufferAddressRangeNV(int var0, int var1, long var2, long var4, long var6);

   public static void glVertexFormatNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexFormatNV;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexFormatNV(var0, var1, var2, var4);
   }

   static native void nglVertexFormatNV(int var0, int var1, int var2, long var3);

   public static void glNormalFormatNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glNormalFormatNV;
      BufferChecks.checkFunctionAddress(var3);
      nglNormalFormatNV(var0, var1, var3);
   }

   static native void nglNormalFormatNV(int var0, int var1, long var2);

   public static void glColorFormatNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glColorFormatNV;
      BufferChecks.checkFunctionAddress(var4);
      nglColorFormatNV(var0, var1, var2, var4);
   }

   static native void nglColorFormatNV(int var0, int var1, int var2, long var3);

   public static void glIndexFormatNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glIndexFormatNV;
      BufferChecks.checkFunctionAddress(var3);
      nglIndexFormatNV(var0, var1, var3);
   }

   static native void nglIndexFormatNV(int var0, int var1, long var2);

   public static void glTexCoordFormatNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glTexCoordFormatNV;
      BufferChecks.checkFunctionAddress(var4);
      nglTexCoordFormatNV(var0, var1, var2, var4);
   }

   static native void nglTexCoordFormatNV(int var0, int var1, int var2, long var3);

   public static void glEdgeFlagFormatNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glEdgeFlagFormatNV;
      BufferChecks.checkFunctionAddress(var2);
      nglEdgeFlagFormatNV(var0, var2);
   }

   static native void nglEdgeFlagFormatNV(int var0, long var1);

   public static void glSecondaryColorFormatNV(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSecondaryColorFormatNV;
      BufferChecks.checkFunctionAddress(var4);
      nglSecondaryColorFormatNV(var0, var1, var2, var4);
   }

   static native void nglSecondaryColorFormatNV(int var0, int var1, int var2, long var3);

   public static void glFogCoordFormatNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glFogCoordFormatNV;
      BufferChecks.checkFunctionAddress(var3);
      nglFogCoordFormatNV(var0, var1, var3);
   }

   static native void nglFogCoordFormatNV(int var0, int var1, long var2);

   public static void glVertexAttribFormatNV(int var0, int var1, int var2, boolean var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribFormatNV;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttribFormatNV(var0, var1, var2, var3, var4, var6);
   }

   static native void nglVertexAttribFormatNV(int var0, int var1, int var2, boolean var3, int var4, long var5);

   public static void glVertexAttribIFormatNV(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribIFormatNV;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribIFormatNV(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribIFormatNV(int var0, int var1, int var2, int var3, long var4);

   public static void glGetIntegeruNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetIntegerui64i_vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      nglGetIntegerui64i_vNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetIntegerui64i_vNV(int var0, int var1, long var2, long var4);

   public static long glGetIntegerui64NV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetIntegerui64i_vNV;
      BufferChecks.checkFunctionAddress(var3);
      LongBuffer var5 = APIUtil.getBufferLong(var2);
      nglGetIntegerui64i_vNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }
}
