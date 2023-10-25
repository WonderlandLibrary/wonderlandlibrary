package org.lwjgl.openal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class ALC11 {
  public static final int ALC_DEFAULT_ALL_DEVICES_SPECIFIER = 4114;
  
  public static final int ALC_ALL_DEVICES_SPECIFIER = 4115;
  
  public static final int ALC_CAPTURE_DEVICE_SPECIFIER = 784;
  
  public static final int ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER = 785;
  
  public static final int ALC_CAPTURE_SAMPLES = 786;
  
  public static final int ALC_MONO_SOURCES = 4112;
  
  public static final int ALC_STEREO_SOURCES = 4113;
  
  public static ALCdevice alcCaptureOpenDevice(String devicename, int frequency, int format, int buffersize) {
    ByteBuffer buffer = MemoryUtil.encodeASCII(devicename);
    long device_address = nalcCaptureOpenDevice(MemoryUtil.getAddressSafe(buffer), frequency, format, buffersize);
    if (device_address != 0L) {
      ALCdevice device = new ALCdevice(device_address);
      synchronized (ALC10.devices) {
        ALC10.devices.put(Long.valueOf(device_address), device);
      } 
      return device;
    } 
    return null;
  }
  
  private static native long nalcCaptureOpenDevice(long paramLong, int paramInt1, int paramInt2, int paramInt3);
  
  public static boolean alcCaptureCloseDevice(ALCdevice device) {
    boolean result = nalcCaptureCloseDevice(ALC10.getDevice(device));
    synchronized (ALC10.devices) {
      device.setInvalid();
      ALC10.devices.remove(new Long(device.device));
    } 
    return result;
  }
  
  static native boolean nalcCaptureCloseDevice(long paramLong);
  
  public static void alcCaptureStart(ALCdevice device) {
    nalcCaptureStart(ALC10.getDevice(device));
  }
  
  static native void nalcCaptureStart(long paramLong);
  
  public static void alcCaptureStop(ALCdevice device) {
    nalcCaptureStop(ALC10.getDevice(device));
  }
  
  static native void nalcCaptureStop(long paramLong);
  
  public static void alcCaptureSamples(ALCdevice device, ByteBuffer buffer, int samples) {
    nalcCaptureSamples(ALC10.getDevice(device), MemoryUtil.getAddress(buffer), samples);
  }
  
  static native void nalcCaptureSamples(long paramLong1, long paramLong2, int paramInt);
  
  static native void initNativeStubs() throws LWJGLException;
  
  static boolean initialize() {
    try {
      IntBuffer ib = BufferUtils.createIntBuffer(2);
      ALC10.alcGetInteger(AL.getDevice(), 4096, ib);
      ib.position(1);
      ALC10.alcGetInteger(AL.getDevice(), 4097, ib);
      int major = ib.get(0);
      int minor = ib.get(1);
      if (major >= 1)
        if (major > 1 || minor >= 1) {
          initNativeStubs();
          AL11.initNativeStubs();
        }  
    } catch (LWJGLException le) {
      LWJGLUtil.log("failed to initialize ALC11: " + le);
      return false;
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\openal\ALC11.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */