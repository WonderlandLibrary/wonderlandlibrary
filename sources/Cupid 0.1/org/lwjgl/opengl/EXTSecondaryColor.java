package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class EXTSecondaryColor {
  public static final int GL_COLOR_SUM_EXT = 33880;
  
  public static final int GL_CURRENT_SECONDARY_COLOR_EXT = 33881;
  
  public static final int GL_SECONDARY_COLOR_ARRAY_SIZE_EXT = 33882;
  
  public static final int GL_SECONDARY_COLOR_ARRAY_TYPE_EXT = 33883;
  
  public static final int GL_SECONDARY_COLOR_ARRAY_STRIDE_EXT = 33884;
  
  public static final int GL_SECONDARY_COLOR_ARRAY_POINTER_EXT = 33885;
  
  public static final int GL_SECONDARY_COLOR_ARRAY_EXT = 33886;
  
  public static void glSecondaryColor3bEXT(byte red, byte green, byte blue) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glSecondaryColor3bEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglSecondaryColor3bEXT(red, green, blue, function_pointer);
  }
  
  static native void nglSecondaryColor3bEXT(byte paramByte1, byte paramByte2, byte paramByte3, long paramLong);
  
  public static void glSecondaryColor3fEXT(float red, float green, float blue) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glSecondaryColor3fEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglSecondaryColor3fEXT(red, green, blue, function_pointer);
  }
  
  static native void nglSecondaryColor3fEXT(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
  
  public static void glSecondaryColor3dEXT(double red, double green, double blue) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glSecondaryColor3dEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglSecondaryColor3dEXT(red, green, blue, function_pointer);
  }
  
  static native void nglSecondaryColor3dEXT(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
  
  public static void glSecondaryColor3ubEXT(byte red, byte green, byte blue) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glSecondaryColor3ubEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglSecondaryColor3ubEXT(red, green, blue, function_pointer);
  }
  
  static native void nglSecondaryColor3ubEXT(byte paramByte1, byte paramByte2, byte paramByte3, long paramLong);
  
  public static void glSecondaryColorPointerEXT(int size, int stride, DoubleBuffer pPointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glSecondaryColorPointerEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureArrayVBOdisabled(caps);
    BufferChecks.checkDirect(pPointer);
    if (LWJGLUtil.CHECKS)
      (StateTracker.getReferences(caps)).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = pPointer; 
    nglSecondaryColorPointerEXT(size, 5130, stride, MemoryUtil.getAddress(pPointer), function_pointer);
  }
  
  public static void glSecondaryColorPointerEXT(int size, int stride, FloatBuffer pPointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glSecondaryColorPointerEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureArrayVBOdisabled(caps);
    BufferChecks.checkDirect(pPointer);
    if (LWJGLUtil.CHECKS)
      (StateTracker.getReferences(caps)).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = pPointer; 
    nglSecondaryColorPointerEXT(size, 5126, stride, MemoryUtil.getAddress(pPointer), function_pointer);
  }
  
  public static void glSecondaryColorPointerEXT(int size, boolean unsigned, int stride, ByteBuffer pPointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glSecondaryColorPointerEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureArrayVBOdisabled(caps);
    BufferChecks.checkDirect(pPointer);
    if (LWJGLUtil.CHECKS)
      (StateTracker.getReferences(caps)).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = pPointer; 
    nglSecondaryColorPointerEXT(size, unsigned ? 5121 : 5120, stride, MemoryUtil.getAddress(pPointer), function_pointer);
  }
  
  static native void nglSecondaryColorPointerEXT(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
  
  public static void glSecondaryColorPointerEXT(int size, int type, int stride, long pPointer_buffer_offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glSecondaryColorPointerEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureArrayVBOenabled(caps);
    nglSecondaryColorPointerEXTBO(size, type, stride, pPointer_buffer_offset, function_pointer);
  }
  
  static native void nglSecondaryColorPointerEXTBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\EXTSecondaryColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */