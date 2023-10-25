package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class CL11 {
  public static final int CL_MISALIGNED_SUB_BUFFER_OFFSET = -13;
  
  public static final int CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST = -14;
  
  public static final int CL_INVALID_PROPERTY = -64;
  
  public static final int CL_VERSION_1_1 = 1;
  
  public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_HALF = 4148;
  
  public static final int CL_DEVICE_HOST_UNIFIED_MEMORY = 4149;
  
  public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_CHAR = 4150;
  
  public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_SHORT = 4151;
  
  public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_INT = 4152;
  
  public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_LONG = 4153;
  
  public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_FLOAT = 4154;
  
  public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_DOUBLE = 4155;
  
  public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_HALF = 4156;
  
  public static final int CL_DEVICE_OPENCL_C_VERSION = 4157;
  
  public static final int CL_FP_SOFT_FLOAT = 64;
  
  public static final int CL_CONTEXT_NUM_DEVICES = 4227;
  
  public static final int CL_Rx = 4282;
  
  public static final int CL_RGx = 4283;
  
  public static final int CL_RGBx = 4284;
  
  public static final int CL_MEM_ASSOCIATED_MEMOBJECT = 4359;
  
  public static final int CL_MEM_OFFSET = 4360;
  
  public static final int CL_ADDRESS_MIRRORED_REPEAT = 4404;
  
  public static final int CL_KERNEL_PREFERRED_WORK_GROUP_SIZE_MULTIPLE = 4531;
  
  public static final int CL_KERNEL_PRIVATE_MEM_SIZE = 4532;
  
  public static final int CL_EVENT_CONTEXT = 4564;
  
  public static final int CL_COMMAND_READ_BUFFER_RECT = 4609;
  
  public static final int CL_COMMAND_WRITE_BUFFER_RECT = 4610;
  
  public static final int CL_COMMAND_COPY_BUFFER_RECT = 4611;
  
  public static final int CL_COMMAND_USER = 4612;
  
  public static final int CL_BUFFER_CREATE_TYPE_REGION = 4640;
  
  public static CLMem clCreateSubBuffer(CLMem buffer, long flags, int buffer_create_type, ByteBuffer buffer_create_info, IntBuffer errcode_ret) {
    long function_pointer = CLCapabilities.clCreateSubBuffer;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_create_info, 2 * PointerBuffer.getPointerSize());
    if (errcode_ret != null)
      BufferChecks.checkBuffer(errcode_ret, 1); 
    CLMem __result = CLMem.create(nclCreateSubBuffer(buffer.getPointer(), flags, buffer_create_type, MemoryUtil.getAddress(buffer_create_info), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), buffer.getParent());
    return __result;
  }
  
  static native long nclCreateSubBuffer(long paramLong1, long paramLong2, int paramInt, long paramLong3, long paramLong4, long paramLong5);
  
  public static int clSetMemObjectDestructorCallback(CLMem memobj, CLMemObjectDestructorCallback pfn_notify) {
    long function_pointer = CLCapabilities.clSetMemObjectDestructorCallback;
    BufferChecks.checkFunctionAddress(function_pointer);
    long user_data = CallbackUtil.createGlobalRef(pfn_notify);
    int __result = 0;
    try {
      __result = nclSetMemObjectDestructorCallback(memobj.getPointer(), pfn_notify.getPointer(), user_data, function_pointer);
      return __result;
    } finally {
      CallbackUtil.checkCallback(__result, user_data);
    } 
  }
  
  static native int nclSetMemObjectDestructorCallback(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, ByteBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, DoubleBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, FloatBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, IntBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, LongBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  public static int clEnqueueReadBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_read, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, ShortBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueReadBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueReadBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_read, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  static native int nclEnqueueReadBufferRect(long paramLong1, long paramLong2, int paramInt1, long paramLong3, long paramLong4, long paramLong5, long paramLong6, long paramLong7, long paramLong8, long paramLong9, long paramLong10, int paramInt2, long paramLong11, long paramLong12, long paramLong13);
  
  public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, ByteBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, DoubleBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, FloatBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, IntBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, LongBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  public static int clEnqueueWriteBufferRect(CLCommandQueue command_queue, CLMem buffer, int blocking_write, PointerBuffer buffer_offset, PointerBuffer host_offset, PointerBuffer region, long buffer_row_pitch, long buffer_slice_pitch, long host_row_pitch, long host_slice_pitch, ShortBuffer ptr, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueWriteBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(buffer_offset, 3);
    BufferChecks.checkBuffer(host_offset, 3);
    BufferChecks.checkBuffer(region, 3);
    BufferChecks.checkBuffer(ptr, CLChecks.calculateBufferRectSize(host_offset, region, host_row_pitch, host_slice_pitch));
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueWriteBufferRect(command_queue.getPointer(), buffer.getPointer(), blocking_write, MemoryUtil.getAddress(buffer_offset), MemoryUtil.getAddress(host_offset), MemoryUtil.getAddress(region), buffer_row_pitch, buffer_slice_pitch, host_row_pitch, host_slice_pitch, MemoryUtil.getAddress(ptr), (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  static native int nclEnqueueWriteBufferRect(long paramLong1, long paramLong2, int paramInt1, long paramLong3, long paramLong4, long paramLong5, long paramLong6, long paramLong7, long paramLong8, long paramLong9, long paramLong10, int paramInt2, long paramLong11, long paramLong12, long paramLong13);
  
  public static int clEnqueueCopyBufferRect(CLCommandQueue command_queue, CLMem src_buffer, CLMem dst_buffer, PointerBuffer src_origin, PointerBuffer dst_origin, PointerBuffer region, long src_row_pitch, long src_slice_pitch, long dst_row_pitch, long dst_slice_pitch, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueCopyBufferRect;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(src_origin, 3);
    BufferChecks.checkBuffer(dst_origin, 3);
    BufferChecks.checkBuffer(region, 3);
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueCopyBufferRect(command_queue.getPointer(), src_buffer.getPointer(), dst_buffer.getPointer(), MemoryUtil.getAddress(src_origin), MemoryUtil.getAddress(dst_origin), MemoryUtil.getAddress(region), src_row_pitch, src_slice_pitch, dst_row_pitch, dst_slice_pitch, (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  static native int nclEnqueueCopyBufferRect(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6, long paramLong7, long paramLong8, long paramLong9, long paramLong10, int paramInt, long paramLong11, long paramLong12, long paramLong13);
  
  public static CLEvent clCreateUserEvent(CLContext context, IntBuffer errcode_ret) {
    long function_pointer = CLCapabilities.clCreateUserEvent;
    BufferChecks.checkFunctionAddress(function_pointer);
    if (errcode_ret != null)
      BufferChecks.checkBuffer(errcode_ret, 1); 
    CLEvent __result = new CLEvent(nclCreateUserEvent(context.getPointer(), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
    return __result;
  }
  
  static native long nclCreateUserEvent(long paramLong1, long paramLong2, long paramLong3);
  
  public static int clSetUserEventStatus(CLEvent event, int execution_status) {
    long function_pointer = CLCapabilities.clSetUserEventStatus;
    BufferChecks.checkFunctionAddress(function_pointer);
    int __result = nclSetUserEventStatus(event.getPointer(), execution_status, function_pointer);
    return __result;
  }
  
  static native int nclSetUserEventStatus(long paramLong1, int paramInt, long paramLong2);
  
  public static int clSetEventCallback(CLEvent event, int command_exec_callback_type, CLEventCallback pfn_notify) {
    long function_pointer = CLCapabilities.clSetEventCallback;
    BufferChecks.checkFunctionAddress(function_pointer);
    long user_data = CallbackUtil.createGlobalRef(pfn_notify);
    pfn_notify.setRegistry(event.getParentRegistry());
    int __result = 0;
    try {
      __result = nclSetEventCallback(event.getPointer(), command_exec_callback_type, pfn_notify.getPointer(), user_data, function_pointer);
      return __result;
    } finally {
      CallbackUtil.checkCallback(__result, user_data);
    } 
  }
  
  static native int nclSetEventCallback(long paramLong1, int paramInt, long paramLong2, long paramLong3, long paramLong4);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CL11.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */