/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;

public interface InputImplementation {
    public boolean hasWheel();

    public int getButtonCount();

    public void createMouse() throws LWJGLException;

    public void destroyMouse();

    public void pollMouse(IntBuffer var1, ByteBuffer var2);

    public void readMouse(ByteBuffer var1);

    public void grabMouse(boolean var1);

    public int getNativeCursorCapabilities();

    public void setCursorPosition(int var1, int var2);

    public void setNativeCursor(Object var1) throws LWJGLException;

    public int getMinCursorSize();

    public int getMaxCursorSize();

    public void createKeyboard() throws LWJGLException;

    public void destroyKeyboard();

    public void pollKeyboard(ByteBuffer var1);

    public void readKeyboard(ByteBuffer var1);

    public Object createCursor(int var1, int var2, int var3, int var4, int var5, IntBuffer var6, IntBuffer var7) throws LWJGLException;

    public void destroyCursor(Object var1);

    public int getWidth();

    public int getHeight();

    public boolean isInsideWindow();
}

