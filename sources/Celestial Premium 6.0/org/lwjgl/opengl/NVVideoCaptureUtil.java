/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.ContextGL;
import org.lwjgl.opengl.GLContext;

public final class NVVideoCaptureUtil {
    private NVVideoCaptureUtil() {
    }

    private static void checkExtension() {
        if (LWJGLUtil.CHECKS && !GLContext.getCapabilities().GL_NV_video_capture) {
            throw new IllegalStateException("NV_video_capture is not supported");
        }
    }

    private static ByteBuffer getPeerInfo() {
        return ContextGL.getCurrentContext().getPeerInfo().getHandle();
    }

    public static boolean glBindVideoCaptureDeviceNV(int video_slot, long device) {
        NVVideoCaptureUtil.checkExtension();
        return NVVideoCaptureUtil.nglBindVideoCaptureDeviceNV(NVVideoCaptureUtil.getPeerInfo(), video_slot, device);
    }

    private static native boolean nglBindVideoCaptureDeviceNV(ByteBuffer var0, int var1, long var2);

    public static int glEnumerateVideoCaptureDevicesNV(LongBuffer devices) {
        NVVideoCaptureUtil.checkExtension();
        if (devices != null) {
            BufferChecks.checkBuffer(devices, 1);
        }
        return NVVideoCaptureUtil.nglEnumerateVideoCaptureDevicesNV(NVVideoCaptureUtil.getPeerInfo(), devices, devices == null ? 0 : devices.position());
    }

    private static native int nglEnumerateVideoCaptureDevicesNV(ByteBuffer var0, LongBuffer var1, int var2);

    public static boolean glLockVideoCaptureDeviceNV(long device) {
        NVVideoCaptureUtil.checkExtension();
        return NVVideoCaptureUtil.nglLockVideoCaptureDeviceNV(NVVideoCaptureUtil.getPeerInfo(), device);
    }

    private static native boolean nglLockVideoCaptureDeviceNV(ByteBuffer var0, long var1);

    public static boolean glQueryVideoCaptureDeviceNV(long device, int attribute, IntBuffer value) {
        NVVideoCaptureUtil.checkExtension();
        BufferChecks.checkBuffer(value, 1);
        return NVVideoCaptureUtil.nglQueryVideoCaptureDeviceNV(NVVideoCaptureUtil.getPeerInfo(), device, attribute, value, value.position());
    }

    private static native boolean nglQueryVideoCaptureDeviceNV(ByteBuffer var0, long var1, int var3, IntBuffer var4, int var5);

    public static boolean glReleaseVideoCaptureDeviceNV(long device) {
        NVVideoCaptureUtil.checkExtension();
        return NVVideoCaptureUtil.nglReleaseVideoCaptureDeviceNV(NVVideoCaptureUtil.getPeerInfo(), device);
    }

    private static native boolean nglReleaseVideoCaptureDeviceNV(ByteBuffer var0, long var1);
}

