package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ATIVertexArrayObject {
  public static final int GL_STATIC_ATI = 34656;
  
  public static final int GL_DYNAMIC_ATI = 34657;
  
  public static final int GL_PRESERVE_ATI = 34658;
  
  public static final int GL_DISCARD_ATI = 34659;
  
  public static final int GL_OBJECT_BUFFER_SIZE_ATI = 34660;
  
  public static final int GL_OBJECT_BUFFER_USAGE_ATI = 34661;
  
  public static final int GL_ARRAY_OBJECT_BUFFER_ATI = 34662;
  
  public static final int GL_ARRAY_OBJECT_OFFSET_ATI = 34663;
  
  public static int glNewObjectBufferATI(int pPointer_size, int usage) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNewObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    int __result = nglNewObjectBufferATI(pPointer_size, 0L, usage, function_pointer);
    return __result;
  }
  
  public static int glNewObjectBufferATI(ByteBuffer pPointer, int usage) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNewObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    int __result = nglNewObjectBufferATI(pPointer.remaining(), MemoryUtil.getAddress(pPointer), usage, function_pointer);
    return __result;
  }
  
  public static int glNewObjectBufferATI(DoubleBuffer pPointer, int usage) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNewObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    int __result = nglNewObjectBufferATI(pPointer.remaining() << 3, MemoryUtil.getAddress(pPointer), usage, function_pointer);
    return __result;
  }
  
  public static int glNewObjectBufferATI(FloatBuffer pPointer, int usage) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNewObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    int __result = nglNewObjectBufferATI(pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), usage, function_pointer);
    return __result;
  }
  
  public static int glNewObjectBufferATI(IntBuffer pPointer, int usage) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNewObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    int __result = nglNewObjectBufferATI(pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), usage, function_pointer);
    return __result;
  }
  
  public static int glNewObjectBufferATI(ShortBuffer pPointer, int usage) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNewObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    int __result = nglNewObjectBufferATI(pPointer.remaining() << 1, MemoryUtil.getAddress(pPointer), usage, function_pointer);
    return __result;
  }
  
  static native int nglNewObjectBufferATI(int paramInt1, long paramLong1, int paramInt2, long paramLong2);
  
  public static boolean glIsObjectBufferATI(int buffer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glIsObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    boolean __result = nglIsObjectBufferATI(buffer, function_pointer);
    return __result;
  }
  
  static native boolean nglIsObjectBufferATI(int paramInt, long paramLong);
  
  public static void glUpdateObjectBufferATI(int buffer, int offset, ByteBuffer pPointer, int preserve) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glUpdateObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    nglUpdateObjectBufferATI(buffer, offset, pPointer.remaining(), MemoryUtil.getAddress(pPointer), preserve, function_pointer);
  }
  
  public static void glUpdateObjectBufferATI(int buffer, int offset, DoubleBuffer pPointer, int preserve) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glUpdateObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    nglUpdateObjectBufferATI(buffer, offset, pPointer.remaining() << 3, MemoryUtil.getAddress(pPointer), preserve, function_pointer);
  }
  
  public static void glUpdateObjectBufferATI(int buffer, int offset, FloatBuffer pPointer, int preserve) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glUpdateObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    nglUpdateObjectBufferATI(buffer, offset, pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), preserve, function_pointer);
  }
  
  public static void glUpdateObjectBufferATI(int buffer, int offset, IntBuffer pPointer, int preserve) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glUpdateObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    nglUpdateObjectBufferATI(buffer, offset, pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), preserve, function_pointer);
  }
  
  public static void glUpdateObjectBufferATI(int buffer, int offset, ShortBuffer pPointer, int preserve) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glUpdateObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    nglUpdateObjectBufferATI(buffer, offset, pPointer.remaining() << 1, MemoryUtil.getAddress(pPointer), preserve, function_pointer);
  }
  
  static native void nglUpdateObjectBufferATI(int paramInt1, int paramInt2, int paramInt3, long paramLong1, int paramInt4, long paramLong2);
  
  public static void glGetObjectBufferATI(int buffer, int pname, FloatBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetObjectBufferfvATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(params);
    nglGetObjectBufferfvATI(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetObjectBufferfvATI(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glGetObjectBufferATI(int buffer, int pname, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetObjectBufferivATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(params);
    nglGetObjectBufferivATI(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetObjectBufferivATI(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static int glGetObjectBufferiATI(int buffer, int pname) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetObjectBufferivATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    IntBuffer params = APIUtil.getBufferInt(caps);
    nglGetObjectBufferivATI(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
    return params.get(0);
  }
  
  public static void glFreeObjectBufferATI(int buffer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glFreeObjectBufferATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglFreeObjectBufferATI(buffer, function_pointer);
  }
  
  static native void nglFreeObjectBufferATI(int paramInt, long paramLong);
  
  public static void glArrayObjectATI(int array, int size, int type, int stride, int buffer, int offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glArrayObjectATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglArrayObjectATI(array, size, type, stride, buffer, offset, function_pointer);
  }
  
  static native void nglArrayObjectATI(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);
  
  public static void glGetArrayObjectATI(int array, int pname, FloatBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetArrayObjectfvATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 4);
    nglGetArrayObjectfvATI(array, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetArrayObjectfvATI(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glGetArrayObjectATI(int array, int pname, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetArrayObjectivATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 4);
    nglGetArrayObjectivATI(array, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetArrayObjectivATI(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glVariantArrayObjectATI(int id, int type, int stride, int buffer, int offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVariantArrayObjectATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVariantArrayObjectATI(id, type, stride, buffer, offset, function_pointer);
  }
  
  static native void nglVariantArrayObjectATI(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
  
  public static void glGetVariantArrayObjectATI(int id, int pname, FloatBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVariantArrayObjectfvATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 4);
    nglGetVariantArrayObjectfvATI(id, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetVariantArrayObjectfvATI(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glGetVariantArrayObjectATI(int id, int pname, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVariantArrayObjectivATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 4);
    nglGetVariantArrayObjectivATI(id, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetVariantArrayObjectivATI(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ATIVertexArrayObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */