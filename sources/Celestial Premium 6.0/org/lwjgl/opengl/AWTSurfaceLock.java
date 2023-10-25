/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.awt.Canvas;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class AWTSurfaceLock {
    private static final int WAIT_DELAY_MILLIS = 100;
    private final ByteBuffer lock_buffer = AWTSurfaceLock.createHandle();
    private boolean firstLockSucceeded;

    AWTSurfaceLock() {
    }

    private static native ByteBuffer createHandle();

    public ByteBuffer lockAndGetHandle(Canvas component) throws LWJGLException {
        while (!this.privilegedLockAndInitHandle(component)) {
            LWJGLUtil.log("Could not get drawing surface info, retrying...");
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException e) {
                LWJGLUtil.log("Interrupted while retrying: " + e);
            }
        }
        return this.lock_buffer;
    }

    private boolean privilegedLockAndInitHandle(final Canvas component) throws LWJGLException {
        if (this.firstLockSucceeded) {
            return AWTSurfaceLock.lockAndInitHandle(this.lock_buffer, component);
        }
        try {
            this.firstLockSucceeded = AccessController.doPrivileged(new PrivilegedExceptionAction<Boolean>(){

                @Override
                public Boolean run() throws LWJGLException {
                    return AWTSurfaceLock.lockAndInitHandle(AWTSurfaceLock.this.lock_buffer, component);
                }
            });
            return this.firstLockSucceeded;
        }
        catch (PrivilegedActionException e) {
            throw (LWJGLException)e.getException();
        }
    }

    private static native boolean lockAndInitHandle(ByteBuffer var0, Canvas var1) throws LWJGLException;

    void unlock() throws LWJGLException {
        AWTSurfaceLock.nUnlock(this.lock_buffer);
    }

    private static native void nUnlock(ByteBuffer var0) throws LWJGLException;
}

