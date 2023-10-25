package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBMultitexture {
  public static final int GL_TEXTURE0_ARB = 33984;
  
  public static final int GL_TEXTURE1_ARB = 33985;
  
  public static final int GL_TEXTURE2_ARB = 33986;
  
  public static final int GL_TEXTURE3_ARB = 33987;
  
  public static final int GL_TEXTURE4_ARB = 33988;
  
  public static final int GL_TEXTURE5_ARB = 33989;
  
  public static final int GL_TEXTURE6_ARB = 33990;
  
  public static final int GL_TEXTURE7_ARB = 33991;
  
  public static final int GL_TEXTURE8_ARB = 33992;
  
  public static final int GL_TEXTURE9_ARB = 33993;
  
  public static final int GL_TEXTURE10_ARB = 33994;
  
  public static final int GL_TEXTURE11_ARB = 33995;
  
  public static final int GL_TEXTURE12_ARB = 33996;
  
  public static final int GL_TEXTURE13_ARB = 33997;
  
  public static final int GL_TEXTURE14_ARB = 33998;
  
  public static final int GL_TEXTURE15_ARB = 33999;
  
  public static final int GL_TEXTURE16_ARB = 34000;
  
  public static final int GL_TEXTURE17_ARB = 34001;
  
  public static final int GL_TEXTURE18_ARB = 34002;
  
  public static final int GL_TEXTURE19_ARB = 34003;
  
  public static final int GL_TEXTURE20_ARB = 34004;
  
  public static final int GL_TEXTURE21_ARB = 34005;
  
  public static final int GL_TEXTURE22_ARB = 34006;
  
  public static final int GL_TEXTURE23_ARB = 34007;
  
  public static final int GL_TEXTURE24_ARB = 34008;
  
  public static final int GL_TEXTURE25_ARB = 34009;
  
  public static final int GL_TEXTURE26_ARB = 34010;
  
  public static final int GL_TEXTURE27_ARB = 34011;
  
  public static final int GL_TEXTURE28_ARB = 34012;
  
  public static final int GL_TEXTURE29_ARB = 34013;
  
  public static final int GL_TEXTURE30_ARB = 34014;
  
  public static final int GL_TEXTURE31_ARB = 34015;
  
  public static final int GL_ACTIVE_TEXTURE_ARB = 34016;
  
  public static final int GL_CLIENT_ACTIVE_TEXTURE_ARB = 34017;
  
  public static final int GL_MAX_TEXTURE_UNITS_ARB = 34018;
  
  public static void glClientActiveTextureARB(int texture) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glClientActiveTextureARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglClientActiveTextureARB(texture, function_pointer);
  }
  
  static native void nglClientActiveTextureARB(int paramInt, long paramLong);
  
  public static void glActiveTextureARB(int texture) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glActiveTextureARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglActiveTextureARB(texture, function_pointer);
  }
  
  static native void nglActiveTextureARB(int paramInt, long paramLong);
  
  public static void glMultiTexCoord1fARB(int target, float s) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord1fARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord1fARB(target, s, function_pointer);
  }
  
  static native void nglMultiTexCoord1fARB(int paramInt, float paramFloat, long paramLong);
  
  public static void glMultiTexCoord1dARB(int target, double s) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord1dARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord1dARB(target, s, function_pointer);
  }
  
  static native void nglMultiTexCoord1dARB(int paramInt, double paramDouble, long paramLong);
  
  public static void glMultiTexCoord1iARB(int target, int s) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord1iARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord1iARB(target, s, function_pointer);
  }
  
  static native void nglMultiTexCoord1iARB(int paramInt1, int paramInt2, long paramLong);
  
  public static void glMultiTexCoord1sARB(int target, short s) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord1sARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord1sARB(target, s, function_pointer);
  }
  
  static native void nglMultiTexCoord1sARB(int paramInt, short paramShort, long paramLong);
  
  public static void glMultiTexCoord2fARB(int target, float s, float t) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord2fARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord2fARB(target, s, t, function_pointer);
  }
  
  static native void nglMultiTexCoord2fARB(int paramInt, float paramFloat1, float paramFloat2, long paramLong);
  
  public static void glMultiTexCoord2dARB(int target, double s, double t) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord2dARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord2dARB(target, s, t, function_pointer);
  }
  
  static native void nglMultiTexCoord2dARB(int paramInt, double paramDouble1, double paramDouble2, long paramLong);
  
  public static void glMultiTexCoord2iARB(int target, int s, int t) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord2iARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord2iARB(target, s, t, function_pointer);
  }
  
  static native void nglMultiTexCoord2iARB(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static void glMultiTexCoord2sARB(int target, short s, short t) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord2sARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord2sARB(target, s, t, function_pointer);
  }
  
  static native void nglMultiTexCoord2sARB(int paramInt, short paramShort1, short paramShort2, long paramLong);
  
  public static void glMultiTexCoord3fARB(int target, float s, float t, float r) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord3fARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord3fARB(target, s, t, r, function_pointer);
  }
  
  static native void nglMultiTexCoord3fARB(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
  
  public static void glMultiTexCoord3dARB(int target, double s, double t, double r) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord3dARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord3dARB(target, s, t, r, function_pointer);
  }
  
  static native void nglMultiTexCoord3dARB(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
  
  public static void glMultiTexCoord3iARB(int target, int s, int t, int r) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord3iARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord3iARB(target, s, t, r, function_pointer);
  }
  
  static native void nglMultiTexCoord3iARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
  
  public static void glMultiTexCoord3sARB(int target, short s, short t, short r) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord3sARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord3sARB(target, s, t, r, function_pointer);
  }
  
  static native void nglMultiTexCoord3sARB(int paramInt, short paramShort1, short paramShort2, short paramShort3, long paramLong);
  
  public static void glMultiTexCoord4fARB(int target, float s, float t, float r, float q) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord4fARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord4fARB(target, s, t, r, q, function_pointer);
  }
  
  static native void nglMultiTexCoord4fARB(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
  
  public static void glMultiTexCoord4dARB(int target, double s, double t, double r, double q) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord4dARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord4dARB(target, s, t, r, q, function_pointer);
  }
  
  static native void nglMultiTexCoord4dARB(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
  
  public static void glMultiTexCoord4iARB(int target, int s, int t, int r, int q) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord4iARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord4iARB(target, s, t, r, q, function_pointer);
  }
  
  static native void nglMultiTexCoord4iARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
  
  public static void glMultiTexCoord4sARB(int target, short s, short t, short r, short q) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord4sARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord4sARB(target, s, t, r, q, function_pointer);
  }
  
  static native void nglMultiTexCoord4sARB(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBMultitexture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */