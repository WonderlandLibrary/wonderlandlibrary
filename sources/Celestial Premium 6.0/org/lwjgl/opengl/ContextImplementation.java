/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.PeerInfo;

interface ContextImplementation {
    public ByteBuffer create(PeerInfo var1, IntBuffer var2, ByteBuffer var3) throws LWJGLException;

    public void swapBuffers() throws LWJGLException;

    public void releaseDrawable(ByteBuffer var1) throws LWJGLException;

    public void releaseCurrentContext() throws LWJGLException;

    public void update(ByteBuffer var1);

    public void makeCurrent(PeerInfo var1, ByteBuffer var2) throws LWJGLException;

    public boolean isCurrent(ByteBuffer var1) throws LWJGLException;

    public void setSwapInterval(int var1);

    public void destroy(PeerInfo var1, ByteBuffer var2) throws LWJGLException;
}

