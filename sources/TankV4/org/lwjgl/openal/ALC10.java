package org.lwjgl.openal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLException;
import org.lwjgl.MemoryUtil;

public final class ALC10 {
   static final HashMap contexts = new HashMap();
   static final HashMap devices = new HashMap();
   public static final int ALC_INVALID = 0;
   public static final int ALC_FALSE = 0;
   public static final int ALC_TRUE = 1;
   public static final int ALC_NO_ERROR = 0;
   public static final int ALC_MAJOR_VERSION = 4096;
   public static final int ALC_MINOR_VERSION = 4097;
   public static final int ALC_ATTRIBUTES_SIZE = 4098;
   public static final int ALC_ALL_ATTRIBUTES = 4099;
   public static final int ALC_DEFAULT_DEVICE_SPECIFIER = 4100;
   public static final int ALC_DEVICE_SPECIFIER = 4101;
   public static final int ALC_EXTENSIONS = 4102;
   public static final int ALC_FREQUENCY = 4103;
   public static final int ALC_REFRESH = 4104;
   public static final int ALC_SYNC = 4105;
   public static final int ALC_INVALID_DEVICE = 40961;
   public static final int ALC_INVALID_CONTEXT = 40962;
   public static final int ALC_INVALID_ENUM = 40963;
   public static final int ALC_INVALID_VALUE = 40964;
   public static final int ALC_OUT_OF_MEMORY = 40965;

   static native void initNativeStubs() throws LWJGLException;

   public static String alcGetString(ALCdevice var0, int var1) {
      ByteBuffer var2 = nalcGetString(getDevice(var0), var1);
      Util.checkALCError(var0);
      return MemoryUtil.decodeUTF8(var2);
   }

   static native ByteBuffer nalcGetString(long var0, int var2);

   public static void alcGetInteger(ALCdevice var0, int var1, IntBuffer var2) {
      BufferChecks.checkDirect(var2);
      nalcGetIntegerv(getDevice(var0), var1, var2.remaining(), MemoryUtil.getAddress(var2));
      Util.checkALCError(var0);
   }

   static native void nalcGetIntegerv(long var0, int var2, int var3, long var4);

   public static ALCdevice alcOpenDevice(String var0) {
      ByteBuffer var1 = MemoryUtil.encodeUTF8(var0);
      long var2 = nalcOpenDevice(MemoryUtil.getAddressSafe(var1));
      if (var2 != 0L) {
         ALCdevice var4 = new ALCdevice(var2);
         HashMap var5;
         synchronized(var5 = devices){}
         devices.put(var2, var4);
         return var4;
      } else {
         return null;
      }
   }

   static native long nalcOpenDevice(long var0);

   public static boolean alcCloseDevice(ALCdevice var0) {
      boolean var1 = nalcCloseDevice(getDevice(var0));
      HashMap var2;
      synchronized(var2 = devices){}
      var0.setInvalid();
      devices.remove(new Long(var0.device));
      return var1;
   }

   static native boolean nalcCloseDevice(long var0);

   public static ALCcontext alcCreateContext(ALCdevice var0, IntBuffer var1) {
      long var2 = nalcCreateContext(getDevice(var0), MemoryUtil.getAddressSafe(var1));
      Util.checkALCError(var0);
      if (var2 != 0L) {
         ALCcontext var4 = new ALCcontext(var2);
         HashMap var5;
         synchronized(var5 = contexts){}
         contexts.put(var2, var4);
         var0.addContext(var4);
         return var4;
      } else {
         return null;
      }
   }

   static native long nalcCreateContext(long var0, long var2);

   public static int alcMakeContextCurrent(ALCcontext var0) {
      return nalcMakeContextCurrent(getContext(var0));
   }

   static native int nalcMakeContextCurrent(long var0);

   public static void alcProcessContext(ALCcontext var0) {
      nalcProcessContext(getContext(var0));
   }

   static native void nalcProcessContext(long var0);

   public static ALCcontext alcGetCurrentContext() {
      ALCcontext var0 = null;
      long var1 = nalcGetCurrentContext();
      if (var1 != 0L) {
         HashMap var3;
         synchronized(var3 = contexts){}
         var0 = (ALCcontext)contexts.get(var1);
      }

      return var0;
   }

   static native long nalcGetCurrentContext();

   public static ALCdevice alcGetContextsDevice(ALCcontext var0) {
      ALCdevice var1 = null;
      long var2 = nalcGetContextsDevice(getContext(var0));
      if (var2 != 0L) {
         HashMap var4;
         synchronized(var4 = devices){}
         var1 = (ALCdevice)devices.get(var2);
      }

      return var1;
   }

   static native long nalcGetContextsDevice(long var0);

   public static void alcSuspendContext(ALCcontext var0) {
      nalcSuspendContext(getContext(var0));
   }

   static native void nalcSuspendContext(long var0);

   public static void alcDestroyContext(ALCcontext var0) {
      HashMap var1;
      synchronized(var1 = contexts){}
      ALCdevice var2 = alcGetContextsDevice(var0);
      nalcDestroyContext(getContext(var0));
      var2.removeContext(var0);
      var0.setInvalid();
   }

   static native void nalcDestroyContext(long var0);

   public static int alcGetError(ALCdevice var0) {
      return nalcGetError(getDevice(var0));
   }

   static native int nalcGetError(long var0);

   public static boolean alcIsExtensionPresent(ALCdevice var0, String var1) {
      ByteBuffer var2 = MemoryUtil.encodeASCII(var1);
      boolean var3 = nalcIsExtensionPresent(getDevice(var0), MemoryUtil.getAddress(var2));
      Util.checkALCError(var0);
      return var3;
   }

   private static native boolean nalcIsExtensionPresent(long var0, long var2);

   public static int alcGetEnumValue(ALCdevice var0, String var1) {
      ByteBuffer var2 = MemoryUtil.encodeASCII(var1);
      int var3 = nalcGetEnumValue(getDevice(var0), MemoryUtil.getAddress(var2));
      Util.checkALCError(var0);
      return var3;
   }

   private static native int nalcGetEnumValue(long var0, long var2);

   static long getDevice(ALCdevice var0) {
      if (var0 != null) {
         Util.checkALCValidDevice(var0);
         return var0.device;
      } else {
         return 0L;
      }
   }

   static long getContext(ALCcontext var0) {
      if (var0 != null) {
         Util.checkALCValidContext(var0);
         return var0.context;
      } else {
         return 0L;
      }
   }
}
