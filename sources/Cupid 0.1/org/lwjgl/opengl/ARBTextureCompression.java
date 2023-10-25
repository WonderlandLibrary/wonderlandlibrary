package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBTextureCompression {
  public static final int GL_COMPRESSED_ALPHA_ARB = 34025;
  
  public static final int GL_COMPRESSED_LUMINANCE_ARB = 34026;
  
  public static final int GL_COMPRESSED_LUMINANCE_ALPHA_ARB = 34027;
  
  public static final int GL_COMPRESSED_INTENSITY_ARB = 34028;
  
  public static final int GL_COMPRESSED_RGB_ARB = 34029;
  
  public static final int GL_COMPRESSED_RGBA_ARB = 34030;
  
  public static final int GL_TEXTURE_COMPRESSION_HINT_ARB = 34031;
  
  public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE_ARB = 34464;
  
  public static final int GL_TEXTURE_COMPRESSED_ARB = 34465;
  
  public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS_ARB = 34466;
  
  public static final int GL_COMPRESSED_TEXTURE_FORMATS_ARB = 34467;
  
  public static void glCompressedTexImage1DARB(int target, int level, int internalformat, int width, int border, ByteBuffer pData) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexImage1DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOdisabled(caps);
    BufferChecks.checkDirect(pData);
    nglCompressedTexImage1DARB(target, level, internalformat, width, border, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
  }
  
  static native void nglCompressedTexImage1DARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
  
  public static void glCompressedTexImage1DARB(int target, int level, int internalformat, int width, int border, int pData_imageSize, long pData_buffer_offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexImage1DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOenabled(caps);
    nglCompressedTexImage1DARBBO(target, level, internalformat, width, border, pData_imageSize, pData_buffer_offset, function_pointer);
  }
  
  static native void nglCompressedTexImage1DARBBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
  
  public static void glCompressedTexImage2DARB(int target, int level, int internalformat, int width, int height, int border, ByteBuffer pData) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexImage2DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOdisabled(caps);
    BufferChecks.checkDirect(pData);
    nglCompressedTexImage2DARB(target, level, internalformat, width, height, border, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
  }
  
  static native void nglCompressedTexImage2DARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);
  
  public static void glCompressedTexImage2DARB(int target, int level, int internalformat, int width, int height, int border, int pData_imageSize, long pData_buffer_offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexImage2DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOenabled(caps);
    nglCompressedTexImage2DARBBO(target, level, internalformat, width, height, border, pData_imageSize, pData_buffer_offset, function_pointer);
  }
  
  static native void nglCompressedTexImage2DARBBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);
  
  public static void glCompressedTexImage3DARB(int target, int level, int internalformat, int width, int height, int depth, int border, ByteBuffer pData) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexImage3DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOdisabled(caps);
    BufferChecks.checkDirect(pData);
    nglCompressedTexImage3DARB(target, level, internalformat, width, height, depth, border, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
  }
  
  static native void nglCompressedTexImage3DARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
  
  public static void glCompressedTexImage3DARB(int target, int level, int internalformat, int width, int height, int depth, int border, int pData_imageSize, long pData_buffer_offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexImage3DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOenabled(caps);
    nglCompressedTexImage3DARBBO(target, level, internalformat, width, height, depth, border, pData_imageSize, pData_buffer_offset, function_pointer);
  }
  
  static native void nglCompressedTexImage3DARBBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
  
  public static void glCompressedTexSubImage1DARB(int target, int level, int xoffset, int width, int format, ByteBuffer pData) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexSubImage1DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOdisabled(caps);
    BufferChecks.checkDirect(pData);
    nglCompressedTexSubImage1DARB(target, level, xoffset, width, format, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
  }
  
  static native void nglCompressedTexSubImage1DARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
  
  public static void glCompressedTexSubImage1DARB(int target, int level, int xoffset, int width, int format, int pData_imageSize, long pData_buffer_offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexSubImage1DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOenabled(caps);
    nglCompressedTexSubImage1DARBBO(target, level, xoffset, width, format, pData_imageSize, pData_buffer_offset, function_pointer);
  }
  
  static native void nglCompressedTexSubImage1DARBBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
  
  public static void glCompressedTexSubImage2DARB(int target, int level, int xoffset, int yoffset, int width, int height, int format, ByteBuffer pData) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexSubImage2DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOdisabled(caps);
    BufferChecks.checkDirect(pData);
    nglCompressedTexSubImage2DARB(target, level, xoffset, yoffset, width, height, format, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
  }
  
  static native void nglCompressedTexSubImage2DARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
  
  public static void glCompressedTexSubImage2DARB(int target, int level, int xoffset, int yoffset, int width, int height, int format, int pData_imageSize, long pData_buffer_offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexSubImage2DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOenabled(caps);
    nglCompressedTexSubImage2DARBBO(target, level, xoffset, yoffset, width, height, format, pData_imageSize, pData_buffer_offset, function_pointer);
  }
  
  static native void nglCompressedTexSubImage2DARBBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
  
  public static void glCompressedTexSubImage3DARB(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, ByteBuffer pData) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexSubImage3DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOdisabled(caps);
    BufferChecks.checkDirect(pData);
    nglCompressedTexSubImage3DARB(target, level, xoffset, yoffset, zoffset, width, height, depth, format, pData.remaining(), MemoryUtil.getAddress(pData), function_pointer);
  }
  
  static native void nglCompressedTexSubImage3DARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong1, long paramLong2);
  
  public static void glCompressedTexSubImage3DARB(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int pData_imageSize, long pData_buffer_offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCompressedTexSubImage3DARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureUnpackPBOenabled(caps);
    nglCompressedTexSubImage3DARBBO(target, level, xoffset, yoffset, zoffset, width, height, depth, format, pData_imageSize, pData_buffer_offset, function_pointer);
  }
  
  static native void nglCompressedTexSubImage3DARBBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong1, long paramLong2);
  
  public static void glGetCompressedTexImageARB(int target, int lod, ByteBuffer pImg) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetCompressedTexImageARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensurePackPBOdisabled(caps);
    BufferChecks.checkDirect(pImg);
    nglGetCompressedTexImageARB(target, lod, MemoryUtil.getAddress(pImg), function_pointer);
  }
  
  static native void nglGetCompressedTexImageARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glGetCompressedTexImageARB(int target, int lod, long pImg_buffer_offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetCompressedTexImageARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensurePackPBOenabled(caps);
    nglGetCompressedTexImageARBBO(target, lod, pImg_buffer_offset, function_pointer);
  }
  
  static native void nglGetCompressedTexImageARBBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBTextureCompression.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */