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
  
  public static void glBufferAddressRangeNV(int pname, int index, long address, long length) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBufferAddressRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBufferAddressRangeNV(pname, index, address, length, function_pointer);
  }
  
  static native void nglBufferAddressRangeNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);
  
  public static void glVertexFormatNV(int size, int type, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexFormatNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexFormatNV(size, type, stride, function_pointer);
  }
  
  static native void nglVertexFormatNV(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static void glNormalFormatNV(int type, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNormalFormatNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglNormalFormatNV(type, stride, function_pointer);
  }
  
  static native void nglNormalFormatNV(int paramInt1, int paramInt2, long paramLong);
  
  public static void glColorFormatNV(int size, int type, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glColorFormatNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglColorFormatNV(size, type, stride, function_pointer);
  }
  
  static native void nglColorFormatNV(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static void glIndexFormatNV(int type, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glIndexFormatNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglIndexFormatNV(type, stride, function_pointer);
  }
  
  static native void nglIndexFormatNV(int paramInt1, int paramInt2, long paramLong);
  
  public static void glTexCoordFormatNV(int size, int type, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexCoordFormatNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTexCoordFormatNV(size, type, stride, function_pointer);
  }
  
  static native void nglTexCoordFormatNV(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static void glEdgeFlagFormatNV(int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glEdgeFlagFormatNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglEdgeFlagFormatNV(stride, function_pointer);
  }
  
  static native void nglEdgeFlagFormatNV(int paramInt, long paramLong);
  
  public static void glSecondaryColorFormatNV(int size, int type, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glSecondaryColorFormatNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglSecondaryColorFormatNV(size, type, stride, function_pointer);
  }
  
  static native void nglSecondaryColorFormatNV(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static void glFogCoordFormatNV(int type, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glFogCoordFormatNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglFogCoordFormatNV(type, stride, function_pointer);
  }
  
  static native void nglFogCoordFormatNV(int paramInt1, int paramInt2, long paramLong);
  
  public static void glVertexAttribFormatNV(int index, int size, int type, boolean normalized, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttribFormatNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexAttribFormatNV(index, size, type, normalized, stride, function_pointer);
  }
  
  static native void nglVertexAttribFormatNV(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, long paramLong);
  
  public static void glVertexAttribIFormatNV(int index, int size, int type, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttribIFormatNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexAttribIFormatNV(index, size, type, stride, function_pointer);
  }
  
  static native void nglVertexAttribIFormatNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
  
  public static void glGetIntegeruNV(int value, int index, LongBuffer result) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetIntegerui64i_vNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(result, 1);
    nglGetIntegerui64i_vNV(value, index, MemoryUtil.getAddress(result), function_pointer);
  }
  
  static native void nglGetIntegerui64i_vNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static long glGetIntegerui64NV(int value, int index) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetIntegerui64i_vNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    LongBuffer result = APIUtil.getBufferLong(caps);
    nglGetIntegerui64i_vNV(value, index, MemoryUtil.getAddress(result), function_pointer);
    return result.get(0);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVVertexBufferUnifiedMemory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */