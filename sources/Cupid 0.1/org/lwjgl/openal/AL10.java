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
  
  static native void initNativeStubs() throws LWJGLException;
  
  public static void alEnable(int capability) {
    nalEnable(capability);
  }
  
  static native void nalEnable(int paramInt);
  
  public static void alDisable(int capability) {
    nalDisable(capability);
  }
  
  static native void nalDisable(int paramInt);
  
  public static boolean alIsEnabled(int capability) {
    boolean __result = nalIsEnabled(capability);
    return __result;
  }
  
  static native boolean nalIsEnabled(int paramInt);
  
  public static boolean alGetBoolean(int pname) {
    boolean __result = nalGetBoolean(pname);
    return __result;
  }
  
  static native boolean nalGetBoolean(int paramInt);
  
  public static int alGetInteger(int pname) {
    int __result = nalGetInteger(pname);
    return __result;
  }
  
  static native int nalGetInteger(int paramInt);
  
  public static float alGetFloat(int pname) {
    float __result = nalGetFloat(pname);
    return __result;
  }
  
  static native float nalGetFloat(int paramInt);
  
  public static double alGetDouble(int pname) {
    double __result = nalGetDouble(pname);
    return __result;
  }
  
  static native double nalGetDouble(int paramInt);
  
  public static void alGetInteger(int pname, IntBuffer data) {
    BufferChecks.checkBuffer(data, 1);
    nalGetIntegerv(pname, MemoryUtil.getAddress(data));
  }
  
  static native void nalGetIntegerv(int paramInt, long paramLong);
  
  public static void alGetFloat(int pname, FloatBuffer data) {
    BufferChecks.checkBuffer(data, 1);
    nalGetFloatv(pname, MemoryUtil.getAddress(data));
  }
  
  static native void nalGetFloatv(int paramInt, long paramLong);
  
  public static void alGetDouble(int pname, DoubleBuffer data) {
    BufferChecks.checkBuffer(data, 1);
    nalGetDoublev(pname, MemoryUtil.getAddress(data));
  }
  
  static native void nalGetDoublev(int paramInt, long paramLong);
  
  public static String alGetString(int pname) {
    String __result = nalGetString(pname);
    return __result;
  }
  
  static native String nalGetString(int paramInt);
  
  public static int alGetError() {
    int __result = nalGetError();
    return __result;
  }
  
  static native int nalGetError();
  
  public static boolean alIsExtensionPresent(String fname) {
    BufferChecks.checkNotNull(fname);
    boolean __result = nalIsExtensionPresent(fname);
    return __result;
  }
  
  static native boolean nalIsExtensionPresent(String paramString);
  
  public static int alGetEnumValue(String ename) {
    BufferChecks.checkNotNull(ename);
    int __result = nalGetEnumValue(ename);
    return __result;
  }
  
  static native int nalGetEnumValue(String paramString);
  
  public static void alListeneri(int pname, int value) {
    nalListeneri(pname, value);
  }
  
  static native void nalListeneri(int paramInt1, int paramInt2);
  
  public static void alListenerf(int pname, float value) {
    nalListenerf(pname, value);
  }
  
  static native void nalListenerf(int paramInt, float paramFloat);
  
  public static void alListener(int pname, FloatBuffer value) {
    BufferChecks.checkBuffer(value, 1);
    nalListenerfv(pname, MemoryUtil.getAddress(value));
  }
  
  static native void nalListenerfv(int paramInt, long paramLong);
  
  public static void alListener3f(int pname, float v1, float v2, float v3) {
    nalListener3f(pname, v1, v2, v3);
  }
  
  static native void nalListener3f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3);
  
  public static int alGetListeneri(int pname) {
    int __result = nalGetListeneri(pname);
    return __result;
  }
  
  static native int nalGetListeneri(int paramInt);
  
  public static float alGetListenerf(int pname) {
    float __result = nalGetListenerf(pname);
    return __result;
  }
  
  static native float nalGetListenerf(int paramInt);
  
  public static void alGetListener(int pname, FloatBuffer floatdata) {
    BufferChecks.checkBuffer(floatdata, 1);
    nalGetListenerfv(pname, MemoryUtil.getAddress(floatdata));
  }
  
  static native void nalGetListenerfv(int paramInt, long paramLong);
  
  public static void alGenSources(IntBuffer sources) {
    BufferChecks.checkDirect(sources);
    nalGenSources(sources.remaining(), MemoryUtil.getAddress(sources));
  }
  
  static native void nalGenSources(int paramInt, long paramLong);
  
  public static int alGenSources() {
    int __result = nalGenSources2(1);
    return __result;
  }
  
  static native int nalGenSources2(int paramInt);
  
  public static void alDeleteSources(IntBuffer sources) {
    BufferChecks.checkDirect(sources);
    nalDeleteSources(sources.remaining(), MemoryUtil.getAddress(sources));
  }
  
  static native void nalDeleteSources(int paramInt, long paramLong);
  
  public static void alDeleteSources(int source) {
    nalDeleteSources2(1, source);
  }
  
  static native void nalDeleteSources2(int paramInt1, int paramInt2);
  
  public static boolean alIsSource(int id) {
    boolean __result = nalIsSource(id);
    return __result;
  }
  
  static native boolean nalIsSource(int paramInt);
  
  public static void alSourcei(int source, int pname, int value) {
    nalSourcei(source, pname, value);
  }
  
  static native void nalSourcei(int paramInt1, int paramInt2, int paramInt3);
  
  public static void alSourcef(int source, int pname, float value) {
    nalSourcef(source, pname, value);
  }
  
  static native void nalSourcef(int paramInt1, int paramInt2, float paramFloat);
  
  public static void alSource(int source, int pname, FloatBuffer value) {
    BufferChecks.checkBuffer(value, 1);
    nalSourcefv(source, pname, MemoryUtil.getAddress(value));
  }
  
  static native void nalSourcefv(int paramInt1, int paramInt2, long paramLong);
  
  public static void alSource3f(int source, int pname, float v1, float v2, float v3) {
    nalSource3f(source, pname, v1, v2, v3);
  }
  
  static native void nalSource3f(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3);
  
  public static int alGetSourcei(int source, int pname) {
    int __result = nalGetSourcei(source, pname);
    return __result;
  }
  
  static native int nalGetSourcei(int paramInt1, int paramInt2);
  
  public static float alGetSourcef(int source, int pname) {
    float __result = nalGetSourcef(source, pname);
    return __result;
  }
  
  static native float nalGetSourcef(int paramInt1, int paramInt2);
  
  public static void alGetSource(int source, int pname, FloatBuffer floatdata) {
    BufferChecks.checkBuffer(floatdata, 1);
    nalGetSourcefv(source, pname, MemoryUtil.getAddress(floatdata));
  }
  
  static native void nalGetSourcefv(int paramInt1, int paramInt2, long paramLong);
  
  public static void alSourcePlay(IntBuffer sources) {
    BufferChecks.checkDirect(sources);
    nalSourcePlayv(sources.remaining(), MemoryUtil.getAddress(sources));
  }
  
  static native void nalSourcePlayv(int paramInt, long paramLong);
  
  public static void alSourcePause(IntBuffer sources) {
    BufferChecks.checkDirect(sources);
    nalSourcePausev(sources.remaining(), MemoryUtil.getAddress(sources));
  }
  
  static native void nalSourcePausev(int paramInt, long paramLong);
  
  public static void alSourceStop(IntBuffer sources) {
    BufferChecks.checkDirect(sources);
    nalSourceStopv(sources.remaining(), MemoryUtil.getAddress(sources));
  }
  
  static native void nalSourceStopv(int paramInt, long paramLong);
  
  public static void alSourceRewind(IntBuffer sources) {
    BufferChecks.checkDirect(sources);
    nalSourceRewindv(sources.remaining(), MemoryUtil.getAddress(sources));
  }
  
  static native void nalSourceRewindv(int paramInt, long paramLong);
  
  public static void alSourcePlay(int source) {
    nalSourcePlay(source);
  }
  
  static native void nalSourcePlay(int paramInt);
  
  public static void alSourcePause(int source) {
    nalSourcePause(source);
  }
  
  static native void nalSourcePause(int paramInt);
  
  public static void alSourceStop(int source) {
    nalSourceStop(source);
  }
  
  static native void nalSourceStop(int paramInt);
  
  public static void alSourceRewind(int source) {
    nalSourceRewind(source);
  }
  
  static native void nalSourceRewind(int paramInt);
  
  public static void alGenBuffers(IntBuffer buffers) {
    BufferChecks.checkDirect(buffers);
    nalGenBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers));
  }
  
  static native void nalGenBuffers(int paramInt, long paramLong);
  
  public static int alGenBuffers() {
    int __result = nalGenBuffers2(1);
    return __result;
  }
  
  static native int nalGenBuffers2(int paramInt);
  
  public static void alDeleteBuffers(IntBuffer buffers) {
    BufferChecks.checkDirect(buffers);
    nalDeleteBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers));
  }
  
  static native void nalDeleteBuffers(int paramInt, long paramLong);
  
  public static void alDeleteBuffers(int buffer) {
    nalDeleteBuffers2(1, buffer);
  }
  
  static native void nalDeleteBuffers2(int paramInt1, int paramInt2);
  
  public static boolean alIsBuffer(int buffer) {
    boolean __result = nalIsBuffer(buffer);
    return __result;
  }
  
  static native boolean nalIsBuffer(int paramInt);
  
  public static void alBufferData(int buffer, int format, ByteBuffer data, int freq) {
    BufferChecks.checkDirect(data);
    nalBufferData(buffer, format, MemoryUtil.getAddress(data), data.remaining(), freq);
  }
  
  public static void alBufferData(int buffer, int format, IntBuffer data, int freq) {
    BufferChecks.checkDirect(data);
    nalBufferData(buffer, format, MemoryUtil.getAddress(data), data.remaining() << 2, freq);
  }
  
  public static void alBufferData(int buffer, int format, ShortBuffer data, int freq) {
    BufferChecks.checkDirect(data);
    nalBufferData(buffer, format, MemoryUtil.getAddress(data), data.remaining() << 1, freq);
  }
  
  static native void nalBufferData(int paramInt1, int paramInt2, long paramLong, int paramInt3, int paramInt4);
  
  public static int alGetBufferi(int buffer, int pname) {
    int __result = nalGetBufferi(buffer, pname);
    return __result;
  }
  
  static native int nalGetBufferi(int paramInt1, int paramInt2);
  
  public static float alGetBufferf(int buffer, int pname) {
    float __result = nalGetBufferf(buffer, pname);
    return __result;
  }
  
  static native float nalGetBufferf(int paramInt1, int paramInt2);
  
  public static void alSourceQueueBuffers(int source, IntBuffer buffers) {
    BufferChecks.checkDirect(buffers);
    nalSourceQueueBuffers(source, buffers.remaining(), MemoryUtil.getAddress(buffers));
  }
  
  static native void nalSourceQueueBuffers(int paramInt1, int paramInt2, long paramLong);
  
  public static void alSourceQueueBuffers(int source, int buffer) {
    nalSourceQueueBuffers2(source, 1, buffer);
  }
  
  static native void nalSourceQueueBuffers2(int paramInt1, int paramInt2, int paramInt3);
  
  public static void alSourceUnqueueBuffers(int source, IntBuffer buffers) {
    BufferChecks.checkDirect(buffers);
    nalSourceUnqueueBuffers(source, buffers.remaining(), MemoryUtil.getAddress(buffers));
  }
  
  static native void nalSourceUnqueueBuffers(int paramInt1, int paramInt2, long paramLong);
  
  public static int alSourceUnqueueBuffers(int source) {
    int __result = nalSourceUnqueueBuffers2(source, 1);
    return __result;
  }
  
  static native int nalSourceUnqueueBuffers2(int paramInt1, int paramInt2);
  
  public static void alDistanceModel(int value) {
    nalDistanceModel(value);
  }
  
  static native void nalDistanceModel(int paramInt);
  
  public static void alDopplerFactor(float value) {
    nalDopplerFactor(value);
  }
  
  static native void nalDopplerFactor(float paramFloat);
  
  public static void alDopplerVelocity(float value) {
    nalDopplerVelocity(value);
  }
  
  static native void nalDopplerVelocity(float paramFloat);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\openal\AL10.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */