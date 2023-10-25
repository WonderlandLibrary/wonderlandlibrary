/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Context;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.PixelFormatLWJGL;

interface DrawableLWJGL
extends Drawable {
    public void setPixelFormat(PixelFormatLWJGL var1) throws LWJGLException;

    public void setPixelFormat(PixelFormatLWJGL var1, ContextAttribs var2) throws LWJGLException;

    public PixelFormatLWJGL getPixelFormat();

    public Context getContext();

    public Context createSharedContext() throws LWJGLException;

    public void checkGLError();

    public void setSwapInterval(int var1);

    public void swapBuffers() throws LWJGLException;

    public void initContext(float var1, float var2, float var3);
}

