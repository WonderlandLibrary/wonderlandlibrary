/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.WindowsPeerInfo;

final class WindowsPbufferPeerInfo
extends WindowsPeerInfo {
    WindowsPbufferPeerInfo(int width, int height, PixelFormat pixel_format, IntBuffer pixelFormatCaps, IntBuffer pBufferAttribs) throws LWJGLException {
        WindowsPbufferPeerInfo.nCreate(this.getHandle(), width, height, pixel_format, pixelFormatCaps, pBufferAttribs);
    }

    private static native void nCreate(ByteBuffer var0, int var1, int var2, PixelFormat var3, IntBuffer var4, IntBuffer var5) throws LWJGLException;

    public boolean isBufferLost() {
        return WindowsPbufferPeerInfo.nIsBufferLost(this.getHandle());
    }

    private static native boolean nIsBufferLost(ByteBuffer var0);

    public void setPbufferAttrib(int attrib, int value) {
        WindowsPbufferPeerInfo.nSetPbufferAttrib(this.getHandle(), attrib, value);
    }

    private static native void nSetPbufferAttrib(ByteBuffer var0, int var1, int var2);

    public void bindTexImageToPbuffer(int buffer) {
        WindowsPbufferPeerInfo.nBindTexImageToPbuffer(this.getHandle(), buffer);
    }

    private static native void nBindTexImageToPbuffer(ByteBuffer var0, int var1);

    public void releaseTexImageFromPbuffer(int buffer) {
        WindowsPbufferPeerInfo.nReleaseTexImageFromPbuffer(this.getHandle(), buffer);
    }

    private static native void nReleaseTexImageFromPbuffer(ByteBuffer var0, int var1);

    public void destroy() {
        WindowsPbufferPeerInfo.nDestroy(this.getHandle());
    }

    private static native void nDestroy(ByteBuffer var0);

    protected void doLockAndInitHandle() throws LWJGLException {
    }

    protected void doUnlock() throws LWJGLException {
    }
}

