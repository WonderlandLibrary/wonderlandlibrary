package org.lwjgl.opencl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opencl.api.CLBufferRegion;
import org.lwjgl.opencl.api.CLImageFormat;

public final class CLMem extends CLObjectChild<CLContext> {
  private static final CLMemUtil util = (CLMemUtil)CLPlatform.<CLMem>getInfoUtilInstance(CLMem.class, "CL_MEM_UTIL");
  
  CLMem(long pointer, CLContext context) {
    super(pointer, context);
    if (isValid())
      context.getCLMemRegistry().registerObject(this); 
  }
  
  public static CLMem createImage2D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_row_pitch, Buffer host_ptr, IntBuffer errcode_ret) {
    return util.createImage2D(context, flags, image_format, image_width, image_height, image_row_pitch, host_ptr, errcode_ret);
  }
  
  public static CLMem createImage3D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, Buffer host_ptr, IntBuffer errcode_ret) {
    return util.createImage3D(context, flags, image_format, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, host_ptr, errcode_ret);
  }
  
  public CLMem createSubBuffer(long flags, int buffer_create_type, CLBufferRegion buffer_create_info, IntBuffer errcode_ret) {
    return util.createSubBuffer(this, flags, buffer_create_type, buffer_create_info, errcode_ret);
  }
  
  public int getInfoInt(int param_name) {
    return util.getInfoInt(this, param_name);
  }
  
  public long getInfoSize(int param_name) {
    return util.getInfoSize(this, param_name);
  }
  
  public long getInfoLong(int param_name) {
    return util.getInfoLong(this, param_name);
  }
  
  public ByteBuffer getInfoHostBuffer() {
    return util.getInfoHostBuffer(this);
  }
  
  public long getImageInfoSize(int param_name) {
    return util.getImageInfoSize(this, param_name);
  }
  
  public CLImageFormat getImageFormat() {
    return util.getImageInfoFormat(this);
  }
  
  public int getImageChannelOrder() {
    return util.getImageInfoFormat(this, 0);
  }
  
  public int getImageChannelType() {
    return util.getImageInfoFormat(this, 1);
  }
  
  public int getGLObjectType() {
    return util.getGLObjectType(this);
  }
  
  public int getGLObjectName() {
    return util.getGLObjectName(this);
  }
  
  public int getGLTextureInfoInt(int param_name) {
    return util.getGLTextureInfoInt(this, param_name);
  }
  
  static CLMem create(long pointer, CLContext context) {
    CLMem clMem = context.getCLMemRegistry().getObject(pointer);
    if (clMem == null) {
      clMem = new CLMem(pointer, context);
    } else {
      clMem.retain();
    } 
    return clMem;
  }
  
  int release() {
    try {
      return super.release();
    } finally {
      if (!isValid())
        getParent().getCLMemRegistry().unregisterObject(this); 
    } 
  }
  
  static interface CLMemUtil extends InfoUtil<CLMem> {
    CLMem createImage2D(CLContext param1CLContext, long param1Long1, CLImageFormat param1CLImageFormat, long param1Long2, long param1Long3, long param1Long4, Buffer param1Buffer, IntBuffer param1IntBuffer);
    
    CLMem createImage3D(CLContext param1CLContext, long param1Long1, CLImageFormat param1CLImageFormat, long param1Long2, long param1Long3, long param1Long4, long param1Long5, long param1Long6, Buffer param1Buffer, IntBuffer param1IntBuffer);
    
    CLMem createSubBuffer(CLMem param1CLMem, long param1Long, int param1Int, CLBufferRegion param1CLBufferRegion, IntBuffer param1IntBuffer);
    
    ByteBuffer getInfoHostBuffer(CLMem param1CLMem);
    
    long getImageInfoSize(CLMem param1CLMem, int param1Int);
    
    CLImageFormat getImageInfoFormat(CLMem param1CLMem);
    
    int getImageInfoFormat(CLMem param1CLMem, int param1Int);
    
    int getGLObjectType(CLMem param1CLMem);
    
    int getGLObjectName(CLMem param1CLMem);
    
    int getGLTextureInfoInt(CLMem param1CLMem, int param1Int);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLMem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */