package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVExplicitMultisample {
  public static final int GL_SAMPLE_POSITION_NV = 36432;
  
  public static final int GL_SAMPLE_MASK_NV = 36433;
  
  public static final int GL_SAMPLE_MASK_VALUE_NV = 36434;
  
  public static final int GL_TEXTURE_BINDING_RENDERBUFFER_NV = 36435;
  
  public static final int GL_TEXTURE_RENDERBUFFER_DATA_STORE_BINDING_NV = 36436;
  
  public static final int GL_MAX_SAMPLE_MASK_WORDS_NV = 36441;
  
  public static final int GL_TEXTURE_RENDERBUFFER_NV = 36437;
  
  public static final int GL_SAMPLER_RENDERBUFFER_NV = 36438;
  
  public static final int GL_INT_SAMPLER_RENDERBUFFER_NV = 36439;
  
  public static final int GL_UNSIGNED_INT_SAMPLER_RENDERBUFFER_NV = 36440;
  
  public static void glGetBooleanIndexedEXT(int pname, int index, ByteBuffer data) {
    EXTDrawBuffers2.glGetBooleanIndexedEXT(pname, index, data);
  }
  
  public static boolean glGetBooleanIndexedEXT(int pname, int index) {
    return EXTDrawBuffers2.glGetBooleanIndexedEXT(pname, index);
  }
  
  public static void glGetIntegerIndexedEXT(int pname, int index, IntBuffer data) {
    EXTDrawBuffers2.glGetIntegerIndexedEXT(pname, index, data);
  }
  
  public static int glGetIntegerIndexedEXT(int pname, int index) {
    return EXTDrawBuffers2.glGetIntegerIndexedEXT(pname, index);
  }
  
  public static void glGetMultisampleNV(int pname, int index, FloatBuffer val) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetMultisamplefvNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(val, 2);
    nglGetMultisamplefvNV(pname, index, MemoryUtil.getAddress(val), function_pointer);
  }
  
  static native void nglGetMultisamplefvNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glSampleMaskIndexedNV(int index, int mask) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glSampleMaskIndexedNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglSampleMaskIndexedNV(index, mask, function_pointer);
  }
  
  static native void nglSampleMaskIndexedNV(int paramInt1, int paramInt2, long paramLong);
  
  public static void glTexRenderbufferNV(int target, int renderbuffer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexRenderbufferNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTexRenderbufferNV(target, renderbuffer, function_pointer);
  }
  
  static native void nglTexRenderbufferNV(int paramInt1, int paramInt2, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVExplicitMultisample.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */