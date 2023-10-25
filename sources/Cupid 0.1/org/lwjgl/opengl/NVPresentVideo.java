package org.lwjgl.opengl;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVPresentVideo {
  public static final int GL_FRAME_NV = 36390;
  
  public static final int FIELDS_NV = 36391;
  
  public static final int GL_CURRENT_TIME_NV = 36392;
  
  public static final int GL_NUM_FILL_STREAMS_NV = 36393;
  
  public static final int GL_PRESENT_TIME_NV = 36394;
  
  public static final int GL_PRESENT_DURATION_NV = 36395;
  
  public static final int GL_NUM_VIDEO_SLOTS_NV = 8432;
  
  public static void glPresentFrameKeyedNV(int video_slot, long minPresentTime, int beginPresentTimeId, int presentDurationId, int type, int target0, int fill0, int key0, int target1, int fill1, int key1) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPresentFrameKeyedNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglPresentFrameKeyedNV(video_slot, minPresentTime, beginPresentTimeId, presentDurationId, type, target0, fill0, key0, target1, fill1, key1, function_pointer);
  }
  
  static native void nglPresentFrameKeyedNV(int paramInt1, long paramLong1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong2);
  
  public static void glPresentFrameDualFillNV(int video_slot, long minPresentTime, int beginPresentTimeId, int presentDurationId, int type, int target0, int fill0, int target1, int fill1, int target2, int fill2, int target3, int fill3) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPresentFrameDualFillNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglPresentFrameDualFillNV(video_slot, minPresentTime, beginPresentTimeId, presentDurationId, type, target0, fill0, target1, fill1, target2, fill2, target3, fill3, function_pointer);
  }
  
  static native void nglPresentFrameDualFillNV(int paramInt1, long paramLong1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, long paramLong2);
  
  public static void glGetVideoNV(int video_slot, int pname, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVideoivNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 1);
    nglGetVideoivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetVideoivNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static int glGetVideoiNV(int video_slot, int pname) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVideoivNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    IntBuffer params = APIUtil.getBufferInt(caps);
    nglGetVideoivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
    return params.get(0);
  }
  
  public static void glGetVideouNV(int video_slot, int pname, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVideouivNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 1);
    nglGetVideouivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetVideouivNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static int glGetVideouiNV(int video_slot, int pname) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVideouivNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    IntBuffer params = APIUtil.getBufferInt(caps);
    nglGetVideouivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
    return params.get(0);
  }
  
  public static void glGetVideoNV(int video_slot, int pname, LongBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVideoi64vNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 1);
    nglGetVideoi64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetVideoi64vNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static long glGetVideoi64NV(int video_slot, int pname) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVideoi64vNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    LongBuffer params = APIUtil.getBufferLong(caps);
    nglGetVideoi64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
    return params.get(0);
  }
  
  public static void glGetVideouNV(int video_slot, int pname, LongBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVideoui64vNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 1);
    nglGetVideoui64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetVideoui64vNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static long glGetVideoui64NV(int video_slot, int pname) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVideoui64vNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    LongBuffer params = APIUtil.getBufferLong(caps);
    nglGetVideoui64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
    return params.get(0);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVPresentVideo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */