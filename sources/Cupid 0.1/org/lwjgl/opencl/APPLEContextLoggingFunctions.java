package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

final class APPLEContextLoggingFunctions {
  static void clLogMessagesToSystemLogAPPLE(ByteBuffer errstr, ByteBuffer private_info, ByteBuffer user_data) {
    long function_pointer = CLCapabilities.clLogMessagesToSystemLogAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(errstr);
    BufferChecks.checkDirect(private_info);
    BufferChecks.checkDirect(user_data);
    nclLogMessagesToSystemLogAPPLE(MemoryUtil.getAddress(errstr), MemoryUtil.getAddress(private_info), private_info.remaining(), MemoryUtil.getAddress(user_data), function_pointer);
  }
  
  static native void nclLogMessagesToSystemLogAPPLE(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5);
  
  static void clLogMessagesToStdoutAPPLE(ByteBuffer errstr, ByteBuffer private_info, ByteBuffer user_data) {
    long function_pointer = CLCapabilities.clLogMessagesToStdoutAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(errstr);
    BufferChecks.checkDirect(private_info);
    BufferChecks.checkDirect(user_data);
    nclLogMessagesToStdoutAPPLE(MemoryUtil.getAddress(errstr), MemoryUtil.getAddress(private_info), private_info.remaining(), MemoryUtil.getAddress(user_data), function_pointer);
  }
  
  static native void nclLogMessagesToStdoutAPPLE(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5);
  
  static void clLogMessagesToStderrAPPLE(ByteBuffer errstr, ByteBuffer private_info, ByteBuffer user_data) {
    long function_pointer = CLCapabilities.clLogMessagesToStderrAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(errstr);
    BufferChecks.checkDirect(private_info);
    BufferChecks.checkDirect(user_data);
    nclLogMessagesToStderrAPPLE(MemoryUtil.getAddress(errstr), MemoryUtil.getAddress(private_info), private_info.remaining(), MemoryUtil.getAddress(user_data), function_pointer);
  }
  
  static native void nclLogMessagesToStderrAPPLE(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\APPLEContextLoggingFunctions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */