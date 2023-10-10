package org.lwjgl.openal;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLException;
import org.lwjgl.MemoryUtil;

public final class AL11 {
   public static final int AL_SEC_OFFSET = 4132;
   public static final int AL_SAMPLE_OFFSET = 4133;
   public static final int AL_BYTE_OFFSET = 4134;
   public static final int AL_STATIC = 4136;
   public static final int AL_STREAMING = 4137;
   public static final int AL_UNDETERMINED = 4144;
   public static final int AL_ILLEGAL_COMMAND = 40964;
   public static final int AL_SPEED_OF_SOUND = 49155;
   public static final int AL_LINEAR_DISTANCE = 53251;
   public static final int AL_LINEAR_DISTANCE_CLAMPED = 53252;
   public static final int AL_EXPONENT_DISTANCE = 53253;
   public static final int AL_EXPONENT_DISTANCE_CLAMPED = 53254;

   private AL11() {
   }

   static native void initNativeStubs() throws LWJGLException;

   public static void alListener3i(int var0, int var1, int var2, int var3) {
      nalListener3i(var0, var1, var2, var3);
   }

   static native void nalListener3i(int var0, int var1, int var2, int var3);

   public static void alGetListeneri(int var0, FloatBuffer var1) {
      BufferChecks.checkBuffer((FloatBuffer)var1, 1);
      nalGetListeneriv(var0, MemoryUtil.getAddress(var1));
   }

   static native void nalGetListeneriv(int var0, long var1);

   public static void alSource3i(int var0, int var1, int var2, int var3, int var4) {
      nalSource3i(var0, var1, var2, var3, var4);
   }

   static native void nalSource3i(int var0, int var1, int var2, int var3, int var4);

   public static void alSource(int var0, int var1, IntBuffer var2) {
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nalSourceiv(var0, var1, MemoryUtil.getAddress(var2));
   }

   static native void nalSourceiv(int var0, int var1, long var2);

   public static void alBufferf(int var0, int var1, float var2) {
      nalBufferf(var0, var1, var2);
   }

   static native void nalBufferf(int var0, int var1, float var2);

   public static void alBuffer3f(int var0, int var1, float var2, float var3, float var4) {
      nalBuffer3f(var0, var1, var2, var3, var4);
   }

   static native void nalBuffer3f(int var0, int var1, float var2, float var3, float var4);

   public static void alBuffer(int var0, int var1, FloatBuffer var2) {
      BufferChecks.checkBuffer((FloatBuffer)var2, 1);
      nalBufferfv(var0, var1, MemoryUtil.getAddress(var2));
   }

   static native void nalBufferfv(int var0, int var1, long var2);

   public static void alBufferi(int var0, int var1, int var2) {
      nalBufferi(var0, var1, var2);
   }

   static native void nalBufferi(int var0, int var1, int var2);

   public static void alBuffer3i(int var0, int var1, int var2, int var3, int var4) {
      nalBuffer3i(var0, var1, var2, var3, var4);
   }

   static native void nalBuffer3i(int var0, int var1, int var2, int var3, int var4);

   public static void alBuffer(int var0, int var1, IntBuffer var2) {
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nalBufferiv(var0, var1, MemoryUtil.getAddress(var2));
   }

   static native void nalBufferiv(int var0, int var1, long var2);

   public static int alGetBufferi(int var0, int var1) {
      int var2 = nalGetBufferi(var0, var1);
      return var2;
   }

   static native int nalGetBufferi(int var0, int var1);

   public static void alGetBuffer(int var0, int var1, IntBuffer var2) {
      BufferChecks.checkBuffer((IntBuffer)var2, 1);
      nalGetBufferiv(var0, var1, MemoryUtil.getAddress(var2));
   }

   static native void nalGetBufferiv(int var0, int var1, long var2);

   public static float alGetBufferf(int var0, int var1) {
      float var2 = nalGetBufferf(var0, var1);
      return var2;
   }

   static native float nalGetBufferf(int var0, int var1);

   public static void alGetBuffer(int var0, int var1, FloatBuffer var2) {
      BufferChecks.checkBuffer((FloatBuffer)var2, 1);
      nalGetBufferfv(var0, var1, MemoryUtil.getAddress(var2));
   }

   static native void nalGetBufferfv(int var0, int var1, long var2);

   public static void alSpeedOfSound(float var0) {
      nalSpeedOfSound(var0);
   }

   static native void nalSpeedOfSound(float var0);
}
