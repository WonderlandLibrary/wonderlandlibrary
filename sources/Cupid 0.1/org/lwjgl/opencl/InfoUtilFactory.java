package org.lwjgl.opencl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferChecks;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.PointerWrapper;
import org.lwjgl.opencl.api.CLBufferRegion;
import org.lwjgl.opencl.api.CLImageFormat;
import org.lwjgl.opencl.api.Filter;
import org.lwjgl.opengl.Drawable;

final class InfoUtilFactory {
  static final InfoUtil<CLCommandQueue> CL_COMMAND_QUEUE_UTIL = new InfoUtilAbstract<CLCommandQueue>() {
      protected int getInfo(CLCommandQueue object, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
        return CL10.clGetCommandQueueInfo(object, param_name, param_value, null);
      }
    };
  
  static final CLContext.CLContextUtil CL_CONTEXT_UTIL = new CLContextUtil();
  
  private static final class CLContextUtil extends InfoUtilAbstract<CLContext> implements CLContext.CLContextUtil {
    private CLContextUtil() {}
    
    protected int getInfo(CLContext context, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
      return CL10.clGetContextInfo(context, param_name, param_value, param_value_size_ret);
    }
    
    public List<CLDevice> getInfoDevices(CLContext context) {
      int num_devices;
      context.checkValid();
      if ((CLCapabilities.getPlatformCapabilities(context.getParent())).OpenCL11) {
        num_devices = getInfoInt(context, 4227);
      } else {
        PointerBuffer size_ret = APIUtil.getBufferPointer();
        CL10.clGetContextInfo(context, 4225, null, size_ret);
        num_devices = (int)(size_ret.get(0) / PointerBuffer.getPointerSize());
      } 
      PointerBuffer deviceIDs = APIUtil.getBufferPointer(num_devices);
      CL10.clGetContextInfo(context, 4225, deviceIDs.getBuffer(), null);
      List<CLDevice> devices = new ArrayList<CLDevice>(num_devices);
      for (int i = 0; i < num_devices; i++)
        devices.add(context.getParent().getCLDevice(deviceIDs.get(i))); 
      return (devices.size() == 0) ? null : devices;
    }
    
    public CLContext create(CLPlatform platform, List<CLDevice> devices, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
      int propertyCount = 2 + ((share_drawable == null) ? 0 : 4) + 1;
      PointerBuffer properties = APIUtil.getBufferPointer(propertyCount + devices.size());
      properties.put(4228L).put((PointerWrapper)platform);
      if (share_drawable != null)
        share_drawable.setCLSharingProperties(properties); 
      properties.put(0L);
      properties.position(propertyCount);
      for (CLDevice device : devices)
        properties.put((PointerWrapper)device); 
      long function_pointer = CLCapabilities.clCreateContext;
      BufferChecks.checkFunctionAddress(function_pointer);
      if (errcode_ret != null) {
        BufferChecks.checkBuffer(errcode_ret, 1);
      } else if (LWJGLUtil.DEBUG) {
        errcode_ret = APIUtil.getBufferInt();
      } 
      long user_data = (pfn_notify == null || pfn_notify.isCustom()) ? 0L : CallbackUtil.createGlobalRef(pfn_notify);
      CLContext __result = null;
      try {
        __result = new CLContext(CL10.nclCreateContext(MemoryUtil.getAddress0(properties.getBuffer()), devices.size(), MemoryUtil.getAddress(properties, propertyCount), (pfn_notify == null) ? 0L : pfn_notify.getPointer(), user_data, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), platform);
        if (LWJGLUtil.DEBUG)
          Util.checkCLError(errcode_ret.get(0)); 
        return __result;
      } finally {
        if (__result != null)
          __result.setContextCallback(user_data); 
      } 
    }
    
    public CLContext createFromType(CLPlatform platform, long device_type, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
      int propertyCount = 2 + ((share_drawable == null) ? 0 : 4) + 1;
      PointerBuffer properties = APIUtil.getBufferPointer(propertyCount);
      properties.put(4228L).put((PointerWrapper)platform);
      if (share_drawable != null)
        share_drawable.setCLSharingProperties(properties); 
      properties.put(0L);
      properties.flip();
      return CL10.clCreateContextFromType(properties, device_type, pfn_notify, errcode_ret);
    }
    
    public List<CLImageFormat> getSupportedImageFormats(CLContext context, long flags, int image_type, Filter<CLImageFormat> filter) {
      IntBuffer numBuffer = APIUtil.getBufferInt();
      CL10.clGetSupportedImageFormats(context, flags, image_type, null, numBuffer);
      int num_image_formats = numBuffer.get(0);
      if (num_image_formats == 0)
        return null; 
      ByteBuffer formatBuffer = BufferUtils.createByteBuffer(num_image_formats * 8);
      CL10.clGetSupportedImageFormats(context, flags, image_type, formatBuffer, null);
      List<CLImageFormat> formats = new ArrayList<CLImageFormat>(num_image_formats);
      for (int i = 0; i < num_image_formats; i++) {
        int offset = num_image_formats * 8;
        CLImageFormat format = new CLImageFormat(formatBuffer.getInt(offset), formatBuffer.getInt(offset + 4));
        if (filter == null || filter.accept(format))
          formats.add(format); 
      } 
      return (formats.size() == 0) ? null : formats;
    }
  }
  
  static final InfoUtil<CLDevice> CL_DEVICE_UTIL = new CLDeviceUtil();
  
  private static final class CLDeviceUtil extends InfoUtilAbstract<CLDevice> {
    private CLDeviceUtil() {}
    
    protected int getInfo(CLDevice device, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
      return CL10.clGetDeviceInfo(device, param_name, param_value, param_value_size_ret);
    }
    
    protected int getInfoSizeArraySize(CLDevice device, int param_name) {
      switch (param_name) {
        case 4101:
          return getInfoInt(device, 4099);
      } 
      throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
    }
  }
  
  static final CLEvent.CLEventUtil CL_EVENT_UTIL = new CLEventUtil();
  
  private static final class CLEventUtil extends InfoUtilAbstract<CLEvent> implements CLEvent.CLEventUtil {
    private CLEventUtil() {}
    
    protected int getInfo(CLEvent event, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
      return CL10.clGetEventInfo(event, param_name, param_value, param_value_size_ret);
    }
    
    public long getProfilingInfoLong(CLEvent event, int param_name) {
      event.checkValid();
      ByteBuffer buffer = APIUtil.getBufferByte(8);
      CL10.clGetEventProfilingInfo(event, param_name, buffer, null);
      return buffer.getLong(0);
    }
  }
  
  static final CLKernel.CLKernelUtil CL_KERNEL_UTIL = new CLKernelUtil();
  
  private static final class CLKernelUtil extends InfoUtilAbstract<CLKernel> implements CLKernel.CLKernelUtil {
    private CLKernelUtil() {}
    
    public void setArg(CLKernel kernel, int index, byte value) {
      CL10.clSetKernelArg(kernel, index, 1L, APIUtil.getBufferByte(1).put(0, value));
    }
    
    public void setArg(CLKernel kernel, int index, short value) {
      CL10.clSetKernelArg(kernel, index, 2L, APIUtil.getBufferShort().put(0, value));
    }
    
    public void setArg(CLKernel kernel, int index, int value) {
      CL10.clSetKernelArg(kernel, index, 4L, APIUtil.getBufferInt().put(0, value));
    }
    
    public void setArg(CLKernel kernel, int index, long value) {
      CL10.clSetKernelArg(kernel, index, 8L, APIUtil.getBufferLong().put(0, value));
    }
    
    public void setArg(CLKernel kernel, int index, float value) {
      CL10.clSetKernelArg(kernel, index, 4L, APIUtil.getBufferFloat().put(0, value));
    }
    
    public void setArg(CLKernel kernel, int index, double value) {
      CL10.clSetKernelArg(kernel, index, 8L, APIUtil.getBufferDouble().put(0, value));
    }
    
    public void setArg(CLKernel kernel, int index, CLObject value) {
      CL10.clSetKernelArg(kernel, index, value);
    }
    
    public void setArgSize(CLKernel kernel, int index, long size) {
      CL10.clSetKernelArg(kernel, index, size);
    }
    
    protected int getInfo(CLKernel kernel, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
      return CL10.clGetKernelInfo(kernel, param_name, param_value, param_value_size_ret);
    }
    
    public long getWorkGroupInfoSize(CLKernel kernel, CLDevice device, int param_name) {
      device.checkValid();
      PointerBuffer buffer = APIUtil.getBufferPointer();
      CL10.clGetKernelWorkGroupInfo(kernel, device, param_name, buffer.getBuffer(), null);
      return buffer.get(0);
    }
    
    public long[] getWorkGroupInfoSizeArray(CLKernel kernel, CLDevice device, int param_name) {
      int size;
      device.checkValid();
      switch (param_name) {
        case 4529:
          size = 3;
          break;
        default:
          throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
      } 
      PointerBuffer buffer = APIUtil.getBufferPointer(size);
      CL10.clGetKernelWorkGroupInfo(kernel, device, param_name, buffer.getBuffer(), null);
      long[] array = new long[size];
      for (int i = 0; i < size; i++)
        array[i] = buffer.get(i); 
      return array;
    }
    
    public long getWorkGroupInfoLong(CLKernel kernel, CLDevice device, int param_name) {
      device.checkValid();
      ByteBuffer buffer = APIUtil.getBufferByte(8);
      CL10.clGetKernelWorkGroupInfo(kernel, device, param_name, buffer, null);
      return buffer.getLong(0);
    }
  }
  
  static final CLMem.CLMemUtil CL_MEM_UTIL = new CLMemUtil();
  
  private static final class CLMemUtil extends InfoUtilAbstract<CLMem> implements CLMem.CLMemUtil {
    private CLMemUtil() {}
    
    protected int getInfo(CLMem mem, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
      return CL10.clGetMemObjectInfo(mem, param_name, param_value, param_value_size_ret);
    }
    
    public CLMem createImage2D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_row_pitch, Buffer host_ptr, IntBuffer errcode_ret) {
      ByteBuffer formatBuffer = APIUtil.getBufferByte(8);
      formatBuffer.putInt(0, image_format.getChannelOrder());
      formatBuffer.putInt(4, image_format.getChannelType());
      long function_pointer = CLCapabilities.clCreateImage2D;
      BufferChecks.checkFunctionAddress(function_pointer);
      if (errcode_ret != null) {
        BufferChecks.checkBuffer(errcode_ret, 1);
      } else if (LWJGLUtil.DEBUG) {
        errcode_ret = APIUtil.getBufferInt();
      } 
      CLMem __result = new CLMem(CL10.nclCreateImage2D(context.getPointer(), flags, MemoryUtil.getAddress(formatBuffer, 0), image_width, image_height, image_row_pitch, MemoryUtil.getAddress0Safe(host_ptr) + ((host_ptr != null) ? BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage2DSize(formatBuffer, image_width, image_height, image_row_pitch)) : 0L), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
      if (LWJGLUtil.DEBUG)
        Util.checkCLError(errcode_ret.get(0)); 
      return __result;
    }
    
    public CLMem createImage3D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, Buffer host_ptr, IntBuffer errcode_ret) {
      ByteBuffer formatBuffer = APIUtil.getBufferByte(8);
      formatBuffer.putInt(0, image_format.getChannelOrder());
      formatBuffer.putInt(4, image_format.getChannelType());
      long function_pointer = CLCapabilities.clCreateImage3D;
      BufferChecks.checkFunctionAddress(function_pointer);
      if (errcode_ret != null) {
        BufferChecks.checkBuffer(errcode_ret, 1);
      } else if (LWJGLUtil.DEBUG) {
        errcode_ret = APIUtil.getBufferInt();
      } 
      CLMem __result = new CLMem(CL10.nclCreateImage3D(context.getPointer(), flags, MemoryUtil.getAddress(formatBuffer, 0), image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, MemoryUtil.getAddress0Safe(host_ptr) + ((host_ptr != null) ? BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage3DSize(formatBuffer, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch)) : 0L), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
      if (LWJGLUtil.DEBUG)
        Util.checkCLError(errcode_ret.get(0)); 
      return __result;
    }
    
    public CLMem createSubBuffer(CLMem mem, long flags, int buffer_create_type, CLBufferRegion buffer_create_info, IntBuffer errcode_ret) {
      PointerBuffer infoBuffer = APIUtil.getBufferPointer(2);
      infoBuffer.put(buffer_create_info.getOrigin());
      infoBuffer.put(buffer_create_info.getSize());
      return CL11.clCreateSubBuffer(mem, flags, buffer_create_type, infoBuffer.getBuffer(), errcode_ret);
    }
    
    public ByteBuffer getInfoHostBuffer(CLMem mem) {
      mem.checkValid();
      if (LWJGLUtil.DEBUG) {
        long mem_flags = getInfoLong(mem, 4353);
        if ((mem_flags & 0x8L) != 8L)
          throw new IllegalArgumentException("The specified CLMem object does not use host memory."); 
      } 
      long size = getInfoSize(mem, 4354);
      if (size == 0L)
        return null; 
      long address = getInfoSize(mem, 4355);
      return CL.getHostBuffer(address, (int)size);
    }
    
    public long getImageInfoSize(CLMem mem, int param_name) {
      mem.checkValid();
      PointerBuffer buffer = APIUtil.getBufferPointer();
      CL10.clGetImageInfo(mem, param_name, buffer.getBuffer(), null);
      return buffer.get(0);
    }
    
    public CLImageFormat getImageInfoFormat(CLMem mem) {
      mem.checkValid();
      ByteBuffer format = APIUtil.getBufferByte(8);
      CL10.clGetImageInfo(mem, 4368, format, null);
      return new CLImageFormat(format.getInt(0), format.getInt(4));
    }
    
    public int getImageInfoFormat(CLMem mem, int index) {
      mem.checkValid();
      ByteBuffer format = APIUtil.getBufferByte(8);
      CL10.clGetImageInfo(mem, 4368, format, null);
      return format.getInt(index << 2);
    }
    
    public int getGLObjectType(CLMem mem) {
      mem.checkValid();
      IntBuffer buffer = APIUtil.getBufferInt();
      CL10GL.clGetGLObjectInfo(mem, buffer, null);
      return buffer.get(0);
    }
    
    public int getGLObjectName(CLMem mem) {
      mem.checkValid();
      IntBuffer buffer = APIUtil.getBufferInt();
      CL10GL.clGetGLObjectInfo(mem, null, buffer);
      return buffer.get(0);
    }
    
    public int getGLTextureInfoInt(CLMem mem, int param_name) {
      mem.checkValid();
      ByteBuffer buffer = APIUtil.getBufferByte(4);
      CL10GL.clGetGLTextureInfo(mem, param_name, buffer, null);
      return buffer.getInt(0);
    }
  }
  
  static final CLPlatform.CLPlatformUtil CL_PLATFORM_UTIL = new CLPlatformUtil();
  
  private static final class CLPlatformUtil extends InfoUtilAbstract<CLPlatform> implements CLPlatform.CLPlatformUtil {
    private CLPlatformUtil() {}
    
    protected int getInfo(CLPlatform platform, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
      return CL10.clGetPlatformInfo(platform, param_name, param_value, param_value_size_ret);
    }
    
    public List<CLPlatform> getPlatforms(Filter<CLPlatform> filter) {
      IntBuffer numBuffer = APIUtil.getBufferInt();
      CL10.clGetPlatformIDs(null, numBuffer);
      int num_platforms = numBuffer.get(0);
      if (num_platforms == 0)
        return null; 
      PointerBuffer platformIDs = APIUtil.getBufferPointer(num_platforms);
      CL10.clGetPlatformIDs(platformIDs, null);
      List<CLPlatform> platforms = new ArrayList<CLPlatform>(num_platforms);
      for (int i = 0; i < num_platforms; i++) {
        CLPlatform platform = CLPlatform.getCLPlatform(platformIDs.get(i));
        if (filter == null || filter.accept(platform))
          platforms.add(platform); 
      } 
      return (platforms.size() == 0) ? null : platforms;
    }
    
    public List<CLDevice> getDevices(CLPlatform platform, int device_type, Filter<CLDevice> filter) {
      platform.checkValid();
      IntBuffer numBuffer = APIUtil.getBufferInt();
      CL10.clGetDeviceIDs(platform, device_type, null, numBuffer);
      int num_devices = numBuffer.get(0);
      if (num_devices == 0)
        return null; 
      PointerBuffer deviceIDs = APIUtil.getBufferPointer(num_devices);
      CL10.clGetDeviceIDs(platform, device_type, deviceIDs, null);
      List<CLDevice> devices = new ArrayList<CLDevice>(num_devices);
      for (int i = 0; i < num_devices; i++) {
        CLDevice device = platform.getCLDevice(deviceIDs.get(i));
        if (filter == null || filter.accept(device))
          devices.add(device); 
      } 
      return (devices.size() == 0) ? null : devices;
    }
  }
  
  static final CLProgram.CLProgramUtil CL_PROGRAM_UTIL = new CLProgramUtil();
  
  private static final class CLProgramUtil extends InfoUtilAbstract<CLProgram> implements CLProgram.CLProgramUtil {
    private CLProgramUtil() {}
    
    protected int getInfo(CLProgram program, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
      return CL10.clGetProgramInfo(program, param_name, param_value, param_value_size_ret);
    }
    
    protected int getInfoSizeArraySize(CLProgram program, int param_name) {
      switch (param_name) {
        case 4453:
          return getInfoInt(program, 4450);
      } 
      throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
    }
    
    public CLKernel[] createKernelsInProgram(CLProgram program) {
      IntBuffer numBuffer = APIUtil.getBufferInt();
      CL10.clCreateKernelsInProgram(program, null, numBuffer);
      int num_kernels = numBuffer.get(0);
      if (num_kernels == 0)
        return null; 
      PointerBuffer kernelIDs = APIUtil.getBufferPointer(num_kernels);
      CL10.clCreateKernelsInProgram(program, kernelIDs, null);
      CLKernel[] kernels = new CLKernel[num_kernels];
      for (int i = 0; i < num_kernels; i++)
        kernels[i] = program.getCLKernel(kernelIDs.get(i)); 
      return kernels;
    }
    
    public CLDevice[] getInfoDevices(CLProgram program) {
      program.checkValid();
      int size = getInfoInt(program, 4450);
      PointerBuffer buffer = APIUtil.getBufferPointer(size);
      CL10.clGetProgramInfo(program, 4451, buffer.getBuffer(), (PointerBuffer)null);
      CLPlatform platform = program.getParent().getParent();
      CLDevice[] array = new CLDevice[size];
      for (int i = 0; i < size; i++)
        array[i] = platform.getCLDevice(buffer.get(i)); 
      return array;
    }
    
    public ByteBuffer getInfoBinaries(CLProgram program, ByteBuffer target) {
      program.checkValid();
      PointerBuffer sizes = getSizesBuffer(program, 4453);
      int totalSize = 0;
      for (int i = 0; i < sizes.limit(); i++)
        totalSize = (int)(totalSize + sizes.get(i)); 
      if (target == null) {
        target = BufferUtils.createByteBuffer(totalSize);
      } else if (LWJGLUtil.DEBUG) {
        BufferChecks.checkBuffer(target, totalSize);
      } 
      CL10.clGetProgramInfo(program, sizes, target, (PointerBuffer)null);
      return target;
    }
    
    public ByteBuffer[] getInfoBinaries(CLProgram program, ByteBuffer[] target) {
      program.checkValid();
      if (target == null) {
        PointerBuffer sizes = getSizesBuffer(program, 4453);
        target = new ByteBuffer[sizes.remaining()];
        for (int i = 0; i < sizes.remaining(); i++)
          target[i] = BufferUtils.createByteBuffer((int)sizes.get(i)); 
      } else if (LWJGLUtil.DEBUG) {
        PointerBuffer sizes = getSizesBuffer(program, 4453);
        if (target.length < sizes.remaining())
          throw new IllegalArgumentException("The target array is not big enough: " + sizes.remaining() + " buffers are required."); 
        for (int i = 0; i < target.length; i++)
          BufferChecks.checkBuffer(target[i], (int)sizes.get(i)); 
      } 
      CL10.clGetProgramInfo(program, target, null);
      return target;
    }
    
    public String getBuildInfoString(CLProgram program, CLDevice device, int param_name) {
      program.checkValid();
      int bytes = getBuildSizeRet(program, device, param_name);
      if (bytes <= 1)
        return null; 
      ByteBuffer buffer = APIUtil.getBufferByte(bytes);
      CL10.clGetProgramBuildInfo(program, device, param_name, buffer, null);
      buffer.limit(bytes - 1);
      return APIUtil.getString(buffer);
    }
    
    public int getBuildInfoInt(CLProgram program, CLDevice device, int param_name) {
      program.checkValid();
      ByteBuffer buffer = APIUtil.getBufferByte(4);
      CL10.clGetProgramBuildInfo(program, device, param_name, buffer, null);
      return buffer.getInt(0);
    }
    
    private static int getBuildSizeRet(CLProgram program, CLDevice device, int param_name) {
      PointerBuffer bytes = APIUtil.getBufferPointer();
      int errcode = CL10.clGetProgramBuildInfo(program, device, param_name, null, bytes);
      if (errcode != 0)
        throw new IllegalArgumentException("Invalid parameter specified: " + LWJGLUtil.toHexString(param_name)); 
      return (int)bytes.get(0);
    }
  }
  
  static final InfoUtil<CLSampler> CL_SAMPLER_UTIL = new InfoUtilAbstract<CLSampler>() {
      protected int getInfo(CLSampler sampler, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
        return CL10.clGetSamplerInfo(sampler, param_name, param_value, param_value_size_ret);
      }
    };
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\InfoUtilFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */