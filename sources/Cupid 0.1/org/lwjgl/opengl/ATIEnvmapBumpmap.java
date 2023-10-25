package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ATIEnvmapBumpmap {
  public static final int GL_BUMP_ROT_MATRIX_ATI = 34677;
  
  public static final int GL_BUMP_ROT_MATRIX_SIZE_ATI = 34678;
  
  public static final int GL_BUMP_NUM_TEX_UNITS_ATI = 34679;
  
  public static final int GL_BUMP_TEX_UNITS_ATI = 34680;
  
  public static final int GL_DUDV_ATI = 34681;
  
  public static final int GL_DU8DV8_ATI = 34682;
  
  public static final int GL_BUMP_ENVMAP_ATI = 34683;
  
  public static final int GL_BUMP_TARGET_ATI = 34684;
  
  public static void glTexBumpParameterATI(int pname, FloatBuffer param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexBumpParameterfvATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(param, 4);
    nglTexBumpParameterfvATI(pname, MemoryUtil.getAddress(param), function_pointer);
  }
  
  static native void nglTexBumpParameterfvATI(int paramInt, long paramLong1, long paramLong2);
  
  public static void glTexBumpParameterATI(int pname, IntBuffer param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexBumpParameterivATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(param, 4);
    nglTexBumpParameterivATI(pname, MemoryUtil.getAddress(param), function_pointer);
  }
  
  static native void nglTexBumpParameterivATI(int paramInt, long paramLong1, long paramLong2);
  
  public static void glGetTexBumpParameterATI(int pname, FloatBuffer param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetTexBumpParameterfvATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(param, 4);
    nglGetTexBumpParameterfvATI(pname, MemoryUtil.getAddress(param), function_pointer);
  }
  
  static native void nglGetTexBumpParameterfvATI(int paramInt, long paramLong1, long paramLong2);
  
  public static void glGetTexBumpParameterATI(int pname, IntBuffer param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetTexBumpParameterivATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(param, 4);
    nglGetTexBumpParameterivATI(pname, MemoryUtil.getAddress(param), function_pointer);
  }
  
  static native void nglGetTexBumpParameterivATI(int paramInt, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ATIEnvmapBumpmap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */