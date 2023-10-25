/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.openal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCdevice;

public final class ALC11 {
    public static final int ALC_DEFAULT_ALL_DEVICES_SPECIFIER = 4114;
    public static final int ALC_ALL_DEVICES_SPECIFIER = 4115;
    public static final int ALC_CAPTURE_DEVICE_SPECIFIER = 784;
    public static final int ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER = 785;
    public static final int ALC_CAPTURE_SAMPLES = 786;
    public static final int ALC_MONO_SOURCES = 4112;
    public static final int ALC_STEREO_SOURCES = 4113;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ALCdevice alcCaptureOpenDevice(String devicename, int frequency, int format, int buffersize) {
        ByteBuffer buffer = MemoryUtil.encodeASCII(devicename);
        long device_address = ALC11.nalcCaptureOpenDevice(MemoryUtil.getAddressSafe(buffer), frequency, format, buffersize);
        if (device_address != 0L) {
            ALCdevice device = new ALCdevice(device_address);
            HashMap<Long, ALCdevice> hashMap = ALC10.devices;
            synchronized (hashMap) {
                ALC10.devices.put(device_address, device);
            }
            return device;
        }
        return null;
    }

    private static native long nalcCaptureOpenDevice(long var0, int var2, int var3, int var4);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean alcCaptureCloseDevice(ALCdevice device) {
        boolean result = ALC11.nalcCaptureCloseDevice(ALC10.getDevice(device));
        HashMap<Long, ALCdevice> hashMap = ALC10.devices;
        synchronized (hashMap) {
            device.setInvalid();
            ALC10.devices.remove(new Long(device.device));
        }
        return result;
    }

    static native boolean nalcCaptureCloseDevice(long var0);

    public static void alcCaptureStart(ALCdevice device) {
        ALC11.nalcCaptureStart(ALC10.getDevice(device));
    }

    static native void nalcCaptureStart(long var0);

    public static void alcCaptureStop(ALCdevice device) {
        ALC11.nalcCaptureStop(ALC10.getDevice(device));
    }

    static native void nalcCaptureStop(long var0);

    public static void alcCaptureSamples(ALCdevice device, ByteBuffer buffer, int samples) {
        ALC11.nalcCaptureSamples(ALC10.getDevice(device), MemoryUtil.getAddress(buffer), samples);
    }

    static native void nalcCaptureSamples(long var0, long var2, int var4);

    static native void initNativeStubs() throws LWJGLException;

    static boolean initialize() {
        try {
            IntBuffer ib = BufferUtils.createIntBuffer(2);
            ALC10.alcGetInteger(AL.getDevice(), 4096, ib);
            ib.position(1);
            ALC10.alcGetInteger(AL.getDevice(), 4097, ib);
            int major = ib.get(0);
            int minor = ib.get(1);
            if (major >= 1 && (major > 1 || minor >= 1)) {
                ALC11.initNativeStubs();
                AL11.initNativeStubs();
            }
        }
        catch (LWJGLException le) {
            LWJGLUtil.log("failed to initialize ALC11: " + le);
            return false;
        }
        return true;
    }
}

