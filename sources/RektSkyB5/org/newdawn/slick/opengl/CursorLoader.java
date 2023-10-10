/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.opengl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.ImageDataFactory;
import org.newdawn.slick.opengl.LoadableImageData;
import org.newdawn.slick.opengl.TGAImageData;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public class CursorLoader {
    private static CursorLoader single = new CursorLoader();

    public static CursorLoader get() {
        return single;
    }

    private CursorLoader() {
    }

    public Cursor getCursor(String ref, int x2, int y2) throws IOException, LWJGLException {
        LoadableImageData imageData = null;
        imageData = ImageDataFactory.getImageDataFor(ref);
        imageData.configureEdging(false);
        ByteBuffer buf = imageData.loadImage(ResourceLoader.getResourceAsStream(ref), true, true, null);
        for (int i2 = 0; i2 < buf.limit(); i2 += 4) {
            byte red = buf.get(i2);
            byte green = buf.get(i2 + 1);
            byte blue = buf.get(i2 + 2);
            byte alpha = buf.get(i2 + 3);
            buf.put(i2 + 2, red);
            buf.put(i2 + 1, green);
            buf.put(i2, blue);
            buf.put(i2 + 3, alpha);
        }
        try {
            int yspot = imageData.getHeight() - y2 - 1;
            if (yspot < 0) {
                yspot = 0;
            }
            return new Cursor(imageData.getTexWidth(), imageData.getTexHeight(), x2, yspot, 1, buf.asIntBuffer(), null);
        }
        catch (Throwable e2) {
            Log.info("Chances are you cursor is too small for this platform");
            throw new LWJGLException(e2);
        }
    }

    public Cursor getCursor(ByteBuffer buf, int x2, int y2, int width, int height) throws IOException, LWJGLException {
        for (int i2 = 0; i2 < buf.limit(); i2 += 4) {
            byte red = buf.get(i2);
            byte green = buf.get(i2 + 1);
            byte blue = buf.get(i2 + 2);
            byte alpha = buf.get(i2 + 3);
            buf.put(i2 + 2, red);
            buf.put(i2 + 1, green);
            buf.put(i2, blue);
            buf.put(i2 + 3, alpha);
        }
        try {
            int yspot = height - y2 - 1;
            if (yspot < 0) {
                yspot = 0;
            }
            return new Cursor(width, height, x2, yspot, 1, buf.asIntBuffer(), null);
        }
        catch (Throwable e2) {
            Log.info("Chances are you cursor is too small for this platform");
            throw new LWJGLException(e2);
        }
    }

    public Cursor getCursor(ImageData imageData, int x2, int y2) throws IOException, LWJGLException {
        ByteBuffer buf = imageData.getImageBufferData();
        for (int i2 = 0; i2 < buf.limit(); i2 += 4) {
            byte red = buf.get(i2);
            byte green = buf.get(i2 + 1);
            byte blue = buf.get(i2 + 2);
            byte alpha = buf.get(i2 + 3);
            buf.put(i2 + 2, red);
            buf.put(i2 + 1, green);
            buf.put(i2, blue);
            buf.put(i2 + 3, alpha);
        }
        try {
            int yspot = imageData.getHeight() - y2 - 1;
            if (yspot < 0) {
                yspot = 0;
            }
            return new Cursor(imageData.getTexWidth(), imageData.getTexHeight(), x2, yspot, 1, buf.asIntBuffer(), null);
        }
        catch (Throwable e2) {
            Log.info("Chances are you cursor is too small for this platform");
            throw new LWJGLException(e2);
        }
    }

    public Cursor getAnimatedCursor(String ref, int x2, int y2, int width, int height, int[] cursorDelays) throws IOException, LWJGLException {
        IntBuffer cursorDelaysBuffer = ByteBuffer.allocateDirect(cursorDelays.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        for (int i2 = 0; i2 < cursorDelays.length; ++i2) {
            cursorDelaysBuffer.put(cursorDelays[i2]);
        }
        cursorDelaysBuffer.flip();
        TGAImageData imageData = new TGAImageData();
        ByteBuffer buf = imageData.loadImage(ResourceLoader.getResourceAsStream(ref), false, null);
        return new Cursor(width, height, x2, y2, cursorDelays.length, buf.asIntBuffer(), cursorDelaysBuffer);
    }
}

