/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.awt.Canvas;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.DrawableLWJGL;
import org.lwjgl.opengl.InputImplementation;
import org.lwjgl.opengl.PeerInfo;
import org.lwjgl.opengl.PixelFormat;

interface DisplayImplementation
extends InputImplementation {
    public void createWindow(DrawableLWJGL var1, DisplayMode var2, Canvas var3, int var4, int var5) throws LWJGLException;

    public void destroyWindow();

    public void switchDisplayMode(DisplayMode var1) throws LWJGLException;

    public void resetDisplayMode();

    public int getGammaRampLength();

    public void setGammaRamp(FloatBuffer var1) throws LWJGLException;

    public String getAdapter();

    public String getVersion();

    public DisplayMode init() throws LWJGLException;

    public void setTitle(String var1);

    public boolean isCloseRequested();

    public boolean isVisible();

    public boolean isActive();

    public boolean isDirty();

    public PeerInfo createPeerInfo(PixelFormat var1, ContextAttribs var2) throws LWJGLException;

    public void update();

    public void reshape(int var1, int var2, int var3, int var4);

    public DisplayMode[] getAvailableDisplayModes() throws LWJGLException;

    public int getPbufferCapabilities();

    public boolean isBufferLost(PeerInfo var1);

    public PeerInfo createPbuffer(int var1, int var2, PixelFormat var3, ContextAttribs var4, IntBuffer var5, IntBuffer var6) throws LWJGLException;

    public void setPbufferAttrib(PeerInfo var1, int var2, int var3);

    public void bindTexImageToPbuffer(PeerInfo var1, int var2);

    public void releaseTexImageFromPbuffer(PeerInfo var1, int var2);

    public int setIcon(ByteBuffer[] var1);

    public void setResizable(boolean var1);

    public boolean wasResized();

    public int getWidth();

    public int getHeight();

    public int getX();

    public int getY();

    public float getPixelScaleFactor();
}

