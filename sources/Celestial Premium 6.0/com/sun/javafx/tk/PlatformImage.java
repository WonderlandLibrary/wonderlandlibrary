/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.image.PixelFormat
 *  javafx.scene.image.PixelReader
 *  javafx.scene.image.WritablePixelFormat
 */
package com.sun.javafx.tk;

import com.sun.javafx.geom.Rectangle;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

public interface PlatformImage {
    public float getPixelScale();

    public int getArgb(int var1, int var2);

    public void setArgb(int var1, int var2, int var3);

    public PixelFormat getPlatformPixelFormat();

    public boolean isWritable();

    public PlatformImage promoteToWritableImage();

    public <T extends Buffer> void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat<T> var5, T var6, int var7);

    public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat<ByteBuffer> var5, byte[] var6, int var7, int var8);

    public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat<IntBuffer> var5, int[] var6, int var7, int var8);

    public <T extends Buffer> void setPixels(int var1, int var2, int var3, int var4, PixelFormat<T> var5, T var6, int var7);

    public void setPixels(int var1, int var2, int var3, int var4, PixelFormat<ByteBuffer> var5, byte[] var6, int var7, int var8);

    public void setPixels(int var1, int var2, int var3, int var4, PixelFormat<IntBuffer> var5, int[] var6, int var7, int var8);

    public void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7);

    public void bufferDirty(Rectangle var1);
}

