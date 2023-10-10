package org.lwjgl.openal;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLException;
import org.lwjgl.MemoryUtil;

public final class AL10 {
   public static final int AL_INVALID = -1;
   public static final int AL_NONE = 0;
   public static final int AL_FALSE = 0;
   public static final int AL_TRUE = 1;
   public static final int AL_SOURCE_TYPE = 4135;
   public static final int AL_SOURCE_ABSOLUTE = 513;
   public static final int AL_SOURCE_RELATIVE = 514;
   public static final int AL_CONE_INNER_ANGLE = 4097;
   public static final int AL_CONE_OUTER_ANGLE = 4098;
   public static final int AL_PITCH = 4099;
   public static final int AL_POSITION = 4100;
   public static final int AL_DIRECTION = 4101;
   public static final int AL_VELOCITY = 4102;
   public static final int AL_LOOPING = 4103;
   public static final int AL_BUFFER = 4105;
   public static final int AL_GAIN = 4106;
   public static final int AL_MIN_GAIN = 4109;
   public static final int AL_MAX_GAIN = 4110;
   public static final int AL_ORIENTATION = 4111;
   public static final int AL_REFERENCE_DISTANCE = 4128;
   public static final int AL_ROLLOFF_FACTOR = 4129;
   public static final int AL_CONE_OUTER_GAIN = 4130;
   public static final int AL_MAX_DISTANCE = 4131;
   public static final int AL_CHANNEL_MASK = 12288;
   public static final int AL_SOURCE_STATE = 4112;
   public static final int AL_INITIAL = 4113;
   public static final int AL_PLAYING = 4114;
   public static final int AL_PAUSED = 4115;
   public static final int AL_STOPPED = 4116;
   public static final int AL_BUFFERS_QUEUED = 4117;
   public static final int AL_BUFFERS_PROCESSED = 4118;
   public static final int AL_FORMAT_MONO8 = 4352;
   public static final int AL_FORMAT_MONO16 = 4353;
   public static final int AL_FORMAT_STEREO8 = 4354;
   public static final int AL_FORMAT_STEREO16 = 4355;
   public static final int AL_FORMAT_VORBIS_EXT = 65539;
   public static final int AL_FREQUENCY = 8193;
   public static final int AL_BITS = 8194;
   public static final int AL_CHANNELS = 8195;
   public static final int AL_SIZE = 8196;
   /** @deprecated */
   public static final int AL_DATA = 8197;
   public static final int AL_UNUSED = 8208;
   public static final int AL_PENDING = 8209;
   public static final int AL_PROCESSED = 8210;
   public static final int AL_NO_ERROR = 0;
   public static final int AL_INVALID_NAME = 40961;
   public static final int AL_INVALID_ENUM = 40962;
   public static final int AL_INVALID_VALUE = 40963;
   public static final int AL_INVALID_OPERATION = 40964;
   public static final int AL_OUT_OF_MEMORY = 40965;
   public static final int AL_VENDOR = 45057;
   public static final int AL_VERSION = 45058;
   public static final int AL_RENDERER = 45059;
   public static final int AL_EXTENSIONS = 45060;
   public static final int AL_DOPPLER_FACTOR = 49152;
   public static final int AL_DOPPLER_VELOCITY = 49153;
   public static final int AL_DISTANCE_MODEL = 53248;
   public static final int AL_INVERSE_DISTANCE = 53249;
   public static final int AL_INVERSE_DISTANCE_CLAMPED = 53250;

   private AL10() {
   }

   static native void initNativeStubs() throws LWJGLException;

   public static void alEnable(int var0) {
      nalEnable(var0);
   }

   static native void nalEnable(int var0);

   public static void alDisable(int var0) {
      nalDisable(var0);
   }

   static native void nalDisable(int var0);

   public static boolean alIsEnabled(int var0) {
      boolean var1 = nalIsEnabled(var0);
      return var1;
   }

   static native boolean nalIsEnabled(int var0);

   public static boolean alGetBoolean(int var0) {
      boolean var1 = nalGetBoolean(var0);
      return var1;
   }

   static native boolean nalGetBoolean(int var0);

   public static int alGetInteger(int var0) {
      int var1 = nalGetInteger(var0);
      return var1;
   }

   static native int nalGetInteger(int var0);

   public static float alGetFloat(int var0) {
      float var1 = nalGetFloat(var0);
      return var1;
   }

   static native float nalGetFloat(int var0);

   public static double alGetDouble(int var0) {
      double var1 = nalGetDouble(var0);
      return var1;
   }

   static native double nalGetDouble(int var0);

   public static void alGetInteger(int var0, IntBuffer var1) {
      BufferChecks.checkBuffer((IntBuffer)var1, 1);
      nalGetIntegerv(var0, MemoryUtil.getAddress(var1));
   }

   static native void nalGetIntegerv(int var0, long var1);

   public static void alGetFloat(int var0, FloatBuffer var1) {
      BufferChecks.checkBuffer((FloatBuffer)var1, 1);
      nalGetFloatv(var0, MemoryUtil.getAddress(var1));
   }

   static native void nalGetFloatv(int var0, long var1);

   public static void alGetDouble(int var0, DoubleBuffer var1) {
      BufferChecks.checkBuffer((DoubleBuffer)var1, 1);
      nalGetDoublev(var0, MemoryUtil.getAddress(var1));
   }

   static native void nalGetDoublev(int var0, long var1);

   public static String alGetString(int var0) {
      String var1 = nalGetString(var0);
      return var1;
   }

   static native String nalGetString(int var0);

   public static int alGetError() {
      int var0 = nalGetError();
      return var0;
   }

   static native int nalGetError();

   public static boolean alIsExtensionPresent(String var0) {
      BufferChecks.checkNotNull(var0);
      boolean var1 = nalIsExtensionPresent(var0);
      return var1;
   }

   static native boolean nalIsExtensionPresent(String var0);

   public static int alGetEnumValue(String var0) {
      BufferChecks.checkNotNull(var0);
      int var1 = nalGetEnumValue(var0);
      return var1;
   }

   static native int nalGetEnumValue(String var0);

   public static void alListeneri(int var0, int var1) {
      nalListeneri(var0, var1);
   }

   static native void nalListeneri(int var0, int var1);

   public static void alListenerf(int var0, float var1) {
      nalListenerf(var0, var1);
   }

   static native void nalListenerf(int var0, float var1);

   public static void alListener(int var0, FloatBuffer var1) {
      BufferChecks.checkBuffer((FloatBuffer)var1, 1);
      nalListenerfv(var0, MemoryUtil.getAddress(var1));
   }

   static native void nalListenerfv(int var0, long var1);

   public static void alListener3f(int var0, float var1, float var2, float var3) {
      nalListener3f(var0, var1, var2, var3);
   }

   static native void nalListener3f(int var0, float var1, float var2, float var3);

   public static int alGetListeneri(int var0) {
      int var1 = nalGetListeneri(var0);
      return var1;
   }

   static native int nalGetListeneri(int var0);

   public static float alGetListenerf(int var0) {
      float var1 = nalGetListenerf(var0);
      return var1;
   }

   static native float nalGetListenerf(int var0);

   public static void alGetListener(int var0, FloatBuffer var1) {
      BufferChecks.checkBuffer((FloatBuffer)var1, 1);
      nalGetListenerfv(var0, MemoryUtil.getAddress(var1));
   }

   static native void nalGetListenerfv(int var0, long var1);

   public static void alGenSources(IntBuffer var0) {
      BufferChecks.checkDirect(var0);
      nalGenSources(var0.remaining(), MemoryUtil.getAddress(var0));
   }

   static native void nalGenSources(int var0, long var1);

   public static int alGenSources() {
      int var0 = nalGenSources2(1);
      return var0;
   }

   static native int nalGenSources2(int var0);

   public static void alDeleteSources(IntBuffer var0) {
      BufferChecks.checkDirect(var0);
      nalDeleteSources(var0.remaining(), MemoryUtil.getAddress(var0));
   }

   static native void nalDeleteSources(int var0, long var1);

   public static void alDeleteSources(int var0) {
      nalDeleteSources2(1, var0);
   }

   static native void nalDeleteSources2(int var0, int var1);

   public static boolean alIsSource(int var0) {
      boolean var1 = nalIsSource(var0);
      return var1;
   }

   static native boolean nalIsSource(int var0);

   public static void alSourcei(int var0, int var1, int var2) {
      nalSourcei(var0, var1, var2);
   }

   static native void nalSourcei(int var0, int var1, int var2);

   public static void alSourcef(int var0, int var1, float var2) {
      nalSourcef(var0, var1, var2);
   }

   static native void nalSourcef(int var0, int var1, float var2);

   public static void alSource(int var0, int var1, FloatBuffer var2) {
      BufferChecks.checkBuffer((FloatBuffer)var2, 1);
      nalSourcefv(var0, var1, MemoryUtil.getAddress(var2));
   }

   static native void nalSourcefv(int var0, int var1, long var2);

   public static void alSource3f(int var0, int var1, float var2, float var3, float var4) {
      nalSource3f(var0, var1, var2, var3, var4);
   }

   static native void nalSource3f(int var0, int var1, float var2, float var3, float var4);

   public static int alGetSourcei(int var0, int var1) {
      int var2 = nalGetSourcei(var0, var1);
      return var2;
   }

   static native int nalGetSourcei(int var0, int var1);

   public static float alGetSourcef(int var0, int var1) {
      float var2 = nalGetSourcef(var0, var1);
      return var2;
   }

   static native float nalGetSourcef(int var0, int var1);

   public static void alGetSource(int var0, int var1, FloatBuffer var2) {
      BufferChecks.checkBuffer((FloatBuffer)var2, 1);
      nalGetSourcefv(var0, var1, MemoryUtil.getAddress(var2));
   }

   static native void nalGetSourcefv(int var0, int var1, long var2);

   public static void alSourcePlay(IntBuffer var0) {
      BufferChecks.checkDirect(var0);
      nalSourcePlayv(var0.remaining(), MemoryUtil.getAddress(var0));
   }

   static native void nalSourcePlayv(int var0, long var1);

   public static void alSourcePause(IntBuffer var0) {
      BufferChecks.checkDirect(var0);
      nalSourcePausev(var0.remaining(), MemoryUtil.getAddress(var0));
   }

   static native void nalSourcePausev(int var0, long var1);

   public static void alSourceStop(IntBuffer var0) {
      BufferChecks.checkDirect(var0);
      nalSourceStopv(var0.remaining(), MemoryUtil.getAddress(var0));
   }

   static native void nalSourceStopv(int var0, long var1);

   public static void alSourceRewind(IntBuffer var0) {
      BufferChecks.checkDirect(var0);
      nalSourceRewindv(var0.remaining(), MemoryUtil.getAddress(var0));
   }

   static native void nalSourceRewindv(int var0, long var1);

   public static void alSourcePlay(int var0) {
      nalSourcePlay(var0);
   }

   static native void nalSourcePlay(int var0);

   public static void alSourcePause(int var0) {
      nalSourcePause(var0);
   }

   static native void nalSourcePause(int var0);

   public static void alSourceStop(int var0) {
      nalSourceStop(var0);
   }

   static native void nalSourceStop(int var0);

   public static void alSourceRewind(int var0) {
      nalSourceRewind(var0);
   }

   static native void nalSourceRewind(int var0);

   public static void alGenBuffers(IntBuffer var0) {
      BufferChecks.checkDirect(var0);
      nalGenBuffers(var0.remaining(), MemoryUtil.getAddress(var0));
   }

   static native void nalGenBuffers(int var0, long var1);

   public static int alGenBuffers() {
      int var0 = nalGenBuffers2(1);
      return var0;
   }

   static native int nalGenBuffers2(int var0);

   public static void alDeleteBuffers(IntBuffer var0) {
      BufferChecks.checkDirect(var0);
      nalDeleteBuffers(var0.remaining(), MemoryUtil.getAddress(var0));
   }

   static native void nalDeleteBuffers(int var0, long var1);

   public static void alDeleteBuffers(int var0) {
      nalDeleteBuffers2(1, var0);
   }

   static native void nalDeleteBuffers2(int var0, int var1);

   public static boolean alIsBuffer(int var0) {
      boolean var1 = nalIsBuffer(var0);
      return var1;
   }

   static native boolean nalIsBuffer(int var0);

   public static void alBufferData(int var0, int var1, ByteBuffer var2, int var3) {
      BufferChecks.checkDirect(var2);
      nalBufferData(var0, var1, MemoryUtil.getAddress(var2), var2.remaining(), var3);
   }

   public static void alBufferData(int var0, int var1, IntBuffer var2, int var3) {
      BufferChecks.checkDirect(var2);
      nalBufferData(var0, var1, MemoryUtil.getAddress(var2), var2.remaining() << 2, var3);
   }

   public static void alBufferData(int var0, int var1, ShortBuffer var2, int var3) {
      BufferChecks.checkDirect(var2);
      nalBufferData(var0, var1, MemoryUtil.getAddress(var2), var2.remaining() << 1, var3);
   }

   static native void nalBufferData(int var0, int var1, long var2, int var4, int var5);

   public static int alGetBufferi(int var0, int var1) {
      int var2 = nalGetBufferi(var0, var1);
      return var2;
   }

   static native int nalGetBufferi(int var0, int var1);

   public static float alGetBufferf(int var0, int var1) {
      float var2 = nalGetBufferf(var0, var1);
      return var2;
   }

   static native float nalGetBufferf(int var0, int var1);

   public static void alSourceQueueBuffers(int var0, IntBuffer var1) {
      BufferChecks.checkDirect(var1);
      nalSourceQueueBuffers(var0, var1.remaining(), MemoryUtil.getAddress(var1));
   }

   static native void nalSourceQueueBuffers(int var0, int var1, long var2);

   public static void alSourceQueueBuffers(int var0, int var1) {
      nalSourceQueueBuffers2(var0, 1, var1);
   }

   static native void nalSourceQueueBuffers2(int var0, int var1, int var2);

   public static void alSourceUnqueueBuffers(int var0, IntBuffer var1) {
      BufferChecks.checkDirect(var1);
      nalSourceUnqueueBuffers(var0, var1.remaining(), MemoryUtil.getAddress(var1));
   }

   static native void nalSourceUnqueueBuffers(int var0, int var1, long var2);

   public static int alSourceUnqueueBuffers(int var0) {
      int var1 = nalSourceUnqueueBuffers2(var0, 1);
      return var1;
   }

   static native int nalSourceUnqueueBuffers2(int var0, int var1);

   public static void alDistanceModel(int var0) {
      nalDistanceModel(var0);
   }

   static native void nalDistanceModel(int var0);

   public static void alDopplerFactor(float var0) {
      nalDopplerFactor(var0);
   }

   static native void nalDopplerFactor(float var0);

   public static void alDopplerVelocity(float var0) {
      nalDopplerVelocity(var0);
   }

   static native void nalDopplerVelocity(float var0);
}
