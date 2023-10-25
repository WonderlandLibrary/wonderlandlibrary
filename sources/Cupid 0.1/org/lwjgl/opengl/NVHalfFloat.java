package org.lwjgl.opengl;

import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVHalfFloat {
  public static final int GL_HALF_FLOAT_NV = 5131;
  
  public static void glVertex2hNV(short x, short y) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertex2hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertex2hNV(x, y, function_pointer);
  }
  
  static native void nglVertex2hNV(short paramShort1, short paramShort2, long paramLong);
  
  public static void glVertex3hNV(short x, short y, short z) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertex3hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertex3hNV(x, y, z, function_pointer);
  }
  
  static native void nglVertex3hNV(short paramShort1, short paramShort2, short paramShort3, long paramLong);
  
  public static void glVertex4hNV(short x, short y, short z, short w) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertex4hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertex4hNV(x, y, z, w, function_pointer);
  }
  
  static native void nglVertex4hNV(short paramShort1, short paramShort2, short paramShort3, short paramShort4, long paramLong);
  
  public static void glNormal3hNV(short nx, short ny, short nz) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glNormal3hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglNormal3hNV(nx, ny, nz, function_pointer);
  }
  
  static native void nglNormal3hNV(short paramShort1, short paramShort2, short paramShort3, long paramLong);
  
  public static void glColor3hNV(short red, short green, short blue) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glColor3hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglColor3hNV(red, green, blue, function_pointer);
  }
  
  static native void nglColor3hNV(short paramShort1, short paramShort2, short paramShort3, long paramLong);
  
  public static void glColor4hNV(short red, short green, short blue, short alpha) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glColor4hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglColor4hNV(red, green, blue, alpha, function_pointer);
  }
  
  static native void nglColor4hNV(short paramShort1, short paramShort2, short paramShort3, short paramShort4, long paramLong);
  
  public static void glTexCoord1hNV(short s) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexCoord1hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTexCoord1hNV(s, function_pointer);
  }
  
  static native void nglTexCoord1hNV(short paramShort, long paramLong);
  
  public static void glTexCoord2hNV(short s, short t) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexCoord2hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTexCoord2hNV(s, t, function_pointer);
  }
  
  static native void nglTexCoord2hNV(short paramShort1, short paramShort2, long paramLong);
  
  public static void glTexCoord3hNV(short s, short t, short r) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexCoord3hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTexCoord3hNV(s, t, r, function_pointer);
  }
  
  static native void nglTexCoord3hNV(short paramShort1, short paramShort2, short paramShort3, long paramLong);
  
  public static void glTexCoord4hNV(short s, short t, short r, short q) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexCoord4hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTexCoord4hNV(s, t, r, q, function_pointer);
  }
  
  static native void nglTexCoord4hNV(short paramShort1, short paramShort2, short paramShort3, short paramShort4, long paramLong);
  
  public static void glMultiTexCoord1hNV(int target, short s) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord1hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord1hNV(target, s, function_pointer);
  }
  
  static native void nglMultiTexCoord1hNV(int paramInt, short paramShort, long paramLong);
  
  public static void glMultiTexCoord2hNV(int target, short s, short t) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord2hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord2hNV(target, s, t, function_pointer);
  }
  
  static native void nglMultiTexCoord2hNV(int paramInt, short paramShort1, short paramShort2, long paramLong);
  
  public static void glMultiTexCoord3hNV(int target, short s, short t, short r) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord3hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord3hNV(target, s, t, r, function_pointer);
  }
  
  static native void nglMultiTexCoord3hNV(int paramInt, short paramShort1, short paramShort2, short paramShort3, long paramLong);
  
  public static void glMultiTexCoord4hNV(int target, short s, short t, short r, short q) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiTexCoord4hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMultiTexCoord4hNV(target, s, t, r, q, function_pointer);
  }
  
  static native void nglMultiTexCoord4hNV(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4, long paramLong);
  
  public static void glFogCoordhNV(short fog) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glFogCoordhNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglFogCoordhNV(fog, function_pointer);
  }
  
  static native void nglFogCoordhNV(short paramShort, long paramLong);
  
  public static void glSecondaryColor3hNV(short red, short green, short blue) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glSecondaryColor3hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglSecondaryColor3hNV(red, green, blue, function_pointer);
  }
  
  static native void nglSecondaryColor3hNV(short paramShort1, short paramShort2, short paramShort3, long paramLong);
  
  public static void glVertexWeighthNV(short weight) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexWeighthNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexWeighthNV(weight, function_pointer);
  }
  
  static native void nglVertexWeighthNV(short paramShort, long paramLong);
  
  public static void glVertexAttrib1hNV(int index, short x) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttrib1hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexAttrib1hNV(index, x, function_pointer);
  }
  
  static native void nglVertexAttrib1hNV(int paramInt, short paramShort, long paramLong);
  
  public static void glVertexAttrib2hNV(int index, short x, short y) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttrib2hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexAttrib2hNV(index, x, y, function_pointer);
  }
  
  static native void nglVertexAttrib2hNV(int paramInt, short paramShort1, short paramShort2, long paramLong);
  
  public static void glVertexAttrib3hNV(int index, short x, short y, short z) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttrib3hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexAttrib3hNV(index, x, y, z, function_pointer);
  }
  
  static native void nglVertexAttrib3hNV(int paramInt, short paramShort1, short paramShort2, short paramShort3, long paramLong);
  
  public static void glVertexAttrib4hNV(int index, short x, short y, short z, short w) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttrib4hNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexAttrib4hNV(index, x, y, z, w, function_pointer);
  }
  
  static native void nglVertexAttrib4hNV(int paramInt, short paramShort1, short paramShort2, short paramShort3, short paramShort4, long paramLong);
  
  public static void glVertexAttribs1NV(int index, ShortBuffer attribs) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttribs1hvNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(attribs);
    nglVertexAttribs1hvNV(index, attribs.remaining(), MemoryUtil.getAddress(attribs), function_pointer);
  }
  
  static native void nglVertexAttribs1hvNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glVertexAttribs2NV(int index, ShortBuffer attribs) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttribs2hvNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(attribs);
    nglVertexAttribs2hvNV(index, attribs.remaining() >> 1, MemoryUtil.getAddress(attribs), function_pointer);
  }
  
  static native void nglVertexAttribs2hvNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glVertexAttribs3NV(int index, ShortBuffer attribs) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttribs3hvNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(attribs);
    nglVertexAttribs3hvNV(index, attribs.remaining() / 3, MemoryUtil.getAddress(attribs), function_pointer);
  }
  
  static native void nglVertexAttribs3hvNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glVertexAttribs4NV(int index, ShortBuffer attribs) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttribs4hvNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(attribs);
    nglVertexAttribs4hvNV(index, attribs.remaining() >> 2, MemoryUtil.getAddress(attribs), function_pointer);
  }
  
  static native void nglVertexAttribs4hvNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVHalfFloat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */