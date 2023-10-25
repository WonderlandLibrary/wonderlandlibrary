/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.DefaultSysImplementation;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

final class WindowsSysImplementation
extends DefaultSysImplementation {
    private static final int JNI_VERSION = 24;

    WindowsSysImplementation() {
    }

    public int getRequiredJNIVersion() {
        return 24;
    }

    public long getTimerResolution() {
        return 1000L;
    }

    public long getTime() {
        return WindowsSysImplementation.nGetTime();
    }

    private static native long nGetTime();

    public boolean has64Bit() {
        return true;
    }

    private static long getHwnd() {
        if (!Display.isCreated()) {
            return 0L;
        }
        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<Long>(){

                @Override
                public Long run() throws Exception {
                    Method getImplementation_method = Display.class.getDeclaredMethod("getImplementation", new Class[0]);
                    getImplementation_method.setAccessible(true);
                    Object display_impl = getImplementation_method.invoke(null, new Object[0]);
                    Class<?> WindowsDisplay_class = Class.forName("org.lwjgl.opengl.WindowsDisplay");
                    Method getHwnd_method = WindowsDisplay_class.getDeclaredMethod("getHwnd", new Class[0]);
                    getHwnd_method.setAccessible(true);
                    return (Long)getHwnd_method.invoke(display_impl, new Object[0]);
                }
            });
        }
        catch (PrivilegedActionException e) {
            throw new Error(e);
        }
    }

    public void alert(String title, String message) {
        if (!Display.isCreated()) {
            WindowsSysImplementation.initCommonControls();
        }
        LWJGLUtil.log(String.format("*** Alert *** %s\n%s\n", title, message));
        ByteBuffer titleText = MemoryUtil.encodeUTF16(title);
        ByteBuffer messageText = MemoryUtil.encodeUTF16(message);
        WindowsSysImplementation.nAlert(WindowsSysImplementation.getHwnd(), MemoryUtil.getAddress(titleText), MemoryUtil.getAddress(messageText));
    }

    private static native void nAlert(long var0, long var2, long var4);

    private static native void initCommonControls();

    public boolean openURL(String url) {
        try {
            LWJGLUtil.execPrivileged(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
            return true;
        }
        catch (Exception e) {
            LWJGLUtil.log("Failed to open url (" + url + "): " + e.getMessage());
            return false;
        }
    }

    public String getClipboard() {
        return WindowsSysImplementation.nGetClipboard();
    }

    private static native String nGetClipboard();

    static {
        Sys.initialize();
    }
}

