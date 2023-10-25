/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util;

import java.nio.ByteBuffer;
import org.lwjgl.util.ReadableColor;

public interface WritableColor {
    public void set(int var1, int var2, int var3, int var4);

    public void set(byte var1, byte var2, byte var3, byte var4);

    public void set(int var1, int var2, int var3);

    public void set(byte var1, byte var2, byte var3);

    public void setRed(int var1);

    public void setGreen(int var1);

    public void setBlue(int var1);

    public void setAlpha(int var1);

    public void setRed(byte var1);

    public void setGreen(byte var1);

    public void setBlue(byte var1);

    public void setAlpha(byte var1);

    public void readRGBA(ByteBuffer var1);

    public void readRGB(ByteBuffer var1);

    public void readARGB(ByteBuffer var1);

    public void readBGRA(ByteBuffer var1);

    public void readBGR(ByteBuffer var1);

    public void readABGR(ByteBuffer var1);

    public void setColor(ReadableColor var1);
}

