package org.lwjgl.openal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
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

   public static ALCdevice alcCaptureOpenDevice(String var0, int var1, int var2, int var3) {
      ByteBuffer var4 = MemoryUtil.encodeASCII(var0);
      long var5 = nalcCaptureOpenDevice(MemoryUtil.getAddressSafe(var4), var1, var2, var3);
      if (var5 != 0L) {
         ALCdevice var7 = new ALCdevice(var5);
         HashMap var8;
         synchronized(var8 = ALC10.devices){}
         ALC10.devices.put(var5, var7);
         return var7;
      } else {
         return null;
      }
   }

   private static native long nalcCaptureOpenDevice(long var0, int var2, int var3, int var4);

   public static boolean alcCaptureCloseDevice(ALCdevice var0) {
      boolean var1 = nalcCaptureCloseDevice(ALC10.getDevice(var0));
      HashMap var2;
      synchronized(var2 = ALC10.devices){}
      var0.setInvalid();
      ALC10.devices.remove(new Long(var0.device));
      return var1;
   }

   static native boolean nalcCaptureCloseDevice(long var0);

   public static void alcCaptureStart(ALCdevice var0) {
      nalcCaptureStart(ALC10.getDevice(var0));
   }

   static native void nalcCaptureStart(long var0);

   public static void alcCaptureStop(ALCdevice var0) {
      nalcCaptureStop(ALC10.getDevice(var0));
   }

   static native void nalcCaptureStop(long var0);

   public static void alcCaptureSamples(ALCdevice var0, ByteBuffer var1, int var2) {
      nalcCaptureSamples(ALC10.getDevice(var0), MemoryUtil.getAddress(var1), var2);
   }

   static native void nalcCaptureSamples(long var0, long var2, int var4);

   static native void initNativeStubs() throws LWJGLException;

   static boolean initialize() {
      try {
         IntBuffer var0 = BufferUtils.createIntBuffer(2);
         ALC10.alcGetInteger(AL.getDevice(), 4096, var0);
         var0.position(1);
         ALC10.alcGetInteger(AL.getDevice(), 4097, var0);
         int var1 = var0.get(0);
         int var2 = var0.get(1);
         if (var1 >= 1 && (var1 > 1 || var2 >= 1)) {
            initNativeStubs();
            AL11.initNativeStubs();
         }

         return true;
      } catch (LWJGLException var3) {
         LWJGLUtil.log("failed to initialize ALC11: " + var3);
         return false;
      }
   }
}
