package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTSeparateShaderObjects {
  public static final int GL_ACTIVE_PROGRAM_EXT = 35725;
  
  public static void glUseShaderProgramEXT(int type, int program) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glUseShaderProgramEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglUseShaderProgramEXT(type, program, function_pointer);
  }
  
  static native void nglUseShaderProgramEXT(int paramInt1, int paramInt2, long paramLong);
  
  public static void glActiveProgramEXT(int program) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glActiveProgramEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglActiveProgramEXT(program, function_pointer);
  }
  
  static native void nglActiveProgramEXT(int paramInt, long paramLong);
  
  public static int glCreateShaderProgramEXT(int type, ByteBuffer string) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCreateShaderProgramEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(string);
    BufferChecks.checkNullTerminated(string);
    int __result = nglCreateShaderProgramEXT(type, MemoryUtil.getAddress(string), function_pointer);
    return __result;
  }
  
  static native int nglCreateShaderProgramEXT(int paramInt, long paramLong1, long paramLong2);
  
  public static int glCreateShaderProgramEXT(int type, CharSequence string) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCreateShaderProgramEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    int __result = nglCreateShaderProgramEXT(type, APIUtil.getBufferNT(caps, string), function_pointer);
    return __result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\EXTSeparateShaderObjects.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */