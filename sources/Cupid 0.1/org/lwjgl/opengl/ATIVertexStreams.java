package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ATIVertexStreams {
  public static final int GL_MAX_VERTEX_STREAMS_ATI = 34667;
  
  public static final int GL_VERTEX_SOURCE_ATI = 34668;
  
  public static final int GL_VERTEX_STREAM0_ATI = 34669;
  
  public static final int GL_VERTEX_STREAM1_ATI = 34670;
  
  public static final int GL_VERTEX_STREAM2_ATI = 34671;
  
  public static final int GL_VERTEX_STREAM3_ATI = 34672;
  
  public static final int GL_VERTEX_STREAM4_ATI = 34673;
  
  public static final int GL_VERTEX_STREAM5_ATI = 34674;
  
  public static final int GL_VERTEX_STREAM6_ATI = 34675;
  
  public static final int GL_VERTEX_STREAM7_ATI = 34676;
  
  public static void glVertexStream2fATI(int stream, float x, float y) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream2fATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream2fATI(stream, x, y, function_pointer);
  }
  
  static native void nglVertexStream2fATI(int paramInt, float paramFloat1, float paramFloat2, long paramLong);
  
  public static void glVertexStream2dATI(int stream, double x, double y) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream2dATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream2dATI(stream, x, y, function_pointer);
  }
  
  static native void nglVertexStream2dATI(int paramInt, double paramDouble1, double paramDouble2, long paramLong);
  
  public static void glVertexStream2iATI(int stream, int x, int y) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream2iATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream2iATI(stream, x, y, function_pointer);
  }
  
  static native void nglVertexStream2iATI(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static void glVertexStream2sATI(int stream, short x, short y) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream2sATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream2sATI(stream, x, y, function_pointer);
  }
  
  static native void nglVertexStream2sATI(int paramInt, short paramShort1, short paramShort2, long paramLong);
  
  public static void glVertexStream3fATI(int stream, float x, float y, float z) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream3fATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream3fATI(stream, x, y, z, function_pointer);
  }
  
  static native void nglVertexStream3fATI(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
  
  public static void glVertexStream3dATI(int stream, double x, double y, double z) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream3dATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream3dATI(stream, x, y, z, function_pointer);
  }
  
  static native void nglVertexStream3dATI(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
  
  public static void glVertexStream3iATI(int stream, int x, int y, int z) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream3iATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream3iATI(stream, x, y, z, function_pointer);
  }
  
  static native void nglVertexStream3iATI(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
  
  public static void glVertexStream3sATI(int stream, short x, short y, short z) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream3sATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream3sATI(stream, x, y, z, function_pointer);
  }
  
  static native void nglVertexStream3sATI(int paramInt, short paramShort1, short paramShort2, short paramShort3, long paramLong);
  
  public static void glVertexStream4fATI(int stream, float x, float y, float z, float w) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream4fATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream4fATI(stream, x, y, z, w, function_pointer);
  }
  
  static native void nglVertexStream4fATI(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
  
  public static void glVertexStream4dATI(int stream, double x, double y, double z, double w) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream4dATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream4dATI(stream, x, y, z, w, function_pointer);
  }
  
  static native void nglVertexStream4dATI(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
  
  public static void glVertexStream4iATI(int stream, int x, int y, int z, int w) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream4iATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream4iATI(stream, x, y, z, w, function_pointer);
  }
  
  static native void nglVertexStream4iATI(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
  
  public static void glVertexStream4sATI(int stream, short x, short y, short z, short w) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexStream4sATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexStream4sATI(stream, x, y, z, w, function_pointer);
  }
  
  static native void nglVertexStream4sATI(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4, long paramLong);
  
  public static void glNormalStream3bATI(int stream, byte x, byte y, byte z) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNormalStream3bATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglNormalStream3bATI(stream, x, y, z, function_pointer);
  }
  
  static native void nglNormalStream3bATI(int paramInt, byte paramByte1, byte paramByte2, byte paramByte3, long paramLong);
  
  public static void glNormalStream3fATI(int stream, float x, float y, float z) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNormalStream3fATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglNormalStream3fATI(stream, x, y, z, function_pointer);
  }
  
  static native void nglNormalStream3fATI(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
  
  public static void glNormalStream3dATI(int stream, double x, double y, double z) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNormalStream3dATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglNormalStream3dATI(stream, x, y, z, function_pointer);
  }
  
  static native void nglNormalStream3dATI(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
  
  public static void glNormalStream3iATI(int stream, int x, int y, int z) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNormalStream3iATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglNormalStream3iATI(stream, x, y, z, function_pointer);
  }
  
  static native void nglNormalStream3iATI(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
  
  public static void glNormalStream3sATI(int stream, short x, short y, short z) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNormalStream3sATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglNormalStream3sATI(stream, x, y, z, function_pointer);
  }
  
  static native void nglNormalStream3sATI(int paramInt, short paramShort1, short paramShort2, short paramShort3, long paramLong);
  
  public static void glClientActiveVertexStreamATI(int stream) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glClientActiveVertexStreamATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglClientActiveVertexStreamATI(stream, function_pointer);
  }
  
  static native void nglClientActiveVertexStreamATI(int paramInt, long paramLong);
  
  public static void glVertexBlendEnvfATI(int pname, float param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexBlendEnvfATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexBlendEnvfATI(pname, param, function_pointer);
  }
  
  static native void nglVertexBlendEnvfATI(int paramInt, float paramFloat, long paramLong);
  
  public static void glVertexBlendEnviATI(int pname, int param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexBlendEnviATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexBlendEnviATI(pname, param, function_pointer);
  }
  
  static native void nglVertexBlendEnviATI(int paramInt1, int paramInt2, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ATIVertexStreams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */