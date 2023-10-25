package org.lwjgl.opencl;

import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.PointerWrapper;

public final class EXTMigrateMemobject {
  public static final int CL_MIGRATE_MEM_OBJECT_HOST_EXT = 1;
  
  public static final int CL_COMMAND_MIGRATE_MEM_OBJECT_EXT = 16448;
  
  public static int clEnqueueMigrateMemObjectEXT(CLCommandQueue command_queue, PointerBuffer mem_objects, long flags, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueMigrateMemObjectEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(mem_objects, 1);
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueMigrateMemObjectEXT(command_queue.getPointer(), mem_objects.remaining(), MemoryUtil.getAddress(mem_objects), flags, (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
  
  static native int nclEnqueueMigrateMemObjectEXT(long paramLong1, int paramInt1, long paramLong2, long paramLong3, int paramInt2, long paramLong4, long paramLong5, long paramLong6);
  
  public static int clEnqueueMigrateMemObjectEXT(CLCommandQueue command_queue, CLMem mem_object, long flags, PointerBuffer event_wait_list, PointerBuffer event) {
    long function_pointer = CLCapabilities.clEnqueueMigrateMemObjectEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    if (event_wait_list != null)
      BufferChecks.checkDirect(event_wait_list); 
    if (event != null)
      BufferChecks.checkBuffer(event, 1); 
    int __result = nclEnqueueMigrateMemObjectEXT(command_queue.getPointer(), 1, APIUtil.getPointer((PointerWrapper)mem_object), flags, (event_wait_list == null) ? 0 : event_wait_list.remaining(), MemoryUtil.getAddressSafe(event_wait_list), MemoryUtil.getAddressSafe(event), function_pointer);
    if (__result == 0)
      command_queue.registerCLEvent(event); 
    return __result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\EXTMigrateMemobject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */