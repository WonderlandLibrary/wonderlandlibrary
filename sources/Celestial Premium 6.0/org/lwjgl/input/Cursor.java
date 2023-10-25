/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.input;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.OpenGLPackageAccess;

public class Cursor {
    public static final int CURSOR_ONE_BIT_TRANSPARENCY = 1;
    public static final int CURSOR_8_BIT_ALPHA = 2;
    public static final int CURSOR_ANIMATION = 4;
    private final CursorElement[] cursors;
    private int index;
    private boolean destroyed;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Cursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if ((Cursor.getCapabilities() & 1) == 0) {
                throw new LWJGLException("Native cursors not supported");
            }
            BufferChecks.checkBufferSize(images, width * height * numImages);
            if (delays != null) {
                BufferChecks.checkBufferSize(delays, numImages);
            }
            if (!Mouse.isCreated()) {
                throw new IllegalStateException("Mouse must be created before creating cursor objects");
            }
            if (width * height * numImages > images.remaining()) {
                throw new IllegalArgumentException("width*height*numImages > images.remaining()");
            }
            if (xHotspot >= width || xHotspot < 0) {
                throw new IllegalArgumentException("xHotspot > width || xHotspot < 0");
            }
            if (yHotspot >= height || yHotspot < 0) {
                throw new IllegalArgumentException("yHotspot > height || yHotspot < 0");
            }
            Sys.initialize();
            yHotspot = height - 1 - yHotspot;
            this.cursors = Cursor.createCursors(width, height, xHotspot, yHotspot, numImages, images, delays);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getMinCursorSize() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if (!Mouse.isCreated()) {
                throw new IllegalStateException("Mouse must be created.");
            }
            return Mouse.getImplementation().getMinCursorSize();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getMaxCursorSize() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if (!Mouse.isCreated()) {
                throw new IllegalStateException("Mouse must be created.");
            }
            return Mouse.getImplementation().getMaxCursorSize();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getCapabilities() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if (Mouse.getImplementation() != null) {
                return Mouse.getImplementation().getNativeCursorCapabilities();
            }
            return OpenGLPackageAccess.createImplementation().getNativeCursorCapabilities();
        }
    }

    private static CursorElement[] createCursors(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
        CursorElement[] cursors;
        IntBuffer images_copy = BufferUtils.createIntBuffer(images.remaining());
        Cursor.flipImages(width, height, numImages, images, images_copy);
        switch (LWJGLUtil.getPlatform()) {
            case 2: {
                Cursor.convertARGBtoABGR(images_copy);
                cursors = new CursorElement[numImages];
                for (int i = 0; i < numImages; ++i) {
                    Object handle = Mouse.getImplementation().createCursor(width, height, xHotspot, yHotspot, 1, images_copy, null);
                    long delay = delays != null ? (long)delays.get(i) : 0L;
                    long timeout = System.currentTimeMillis();
                    cursors[i] = new CursorElement(handle, delay, timeout);
                    images_copy.position(width * height * (i + 1));
                }
                break;
            }
            case 3: {
                cursors = new CursorElement[numImages];
                for (int i = 0; i < numImages; ++i) {
                    int size = width * height;
                    for (int j = 0; j < size; ++j) {
                        int index = j + i * size;
                        int alpha = images_copy.get(index) >> 24 & 0xFF;
                        if (alpha == 255) continue;
                        images_copy.put(index, 0);
                    }
                    Object handle = Mouse.getImplementation().createCursor(width, height, xHotspot, yHotspot, 1, images_copy, null);
                    long delay = delays != null ? (long)delays.get(i) : 0L;
                    long timeout = System.currentTimeMillis();
                    cursors[i] = new CursorElement(handle, delay, timeout);
                    images_copy.position(width * height * (i + 1));
                }
                break;
            }
            case 1: {
                Object handle = Mouse.getImplementation().createCursor(width, height, xHotspot, yHotspot, numImages, images_copy, delays);
                CursorElement cursor_element = new CursorElement(handle, -1L, -1L);
                cursors = new CursorElement[]{cursor_element};
                break;
            }
            default: {
                throw new RuntimeException("Unknown OS");
            }
        }
        return cursors;
    }

    private static void convertARGBtoABGR(IntBuffer imageBuffer) {
        for (int i = 0; i < imageBuffer.limit(); ++i) {
            int argbColor = imageBuffer.get(i);
            byte alpha = (byte)(argbColor >>> 24);
            byte blue = (byte)(argbColor >>> 16);
            byte green = (byte)(argbColor >>> 8);
            byte red = (byte)argbColor;
            int abgrColor = ((alpha & 0xFF) << 24) + ((red & 0xFF) << 16) + ((green & 0xFF) << 8) + (blue & 0xFF);
            imageBuffer.put(i, abgrColor);
        }
    }

    private static void flipImages(int width, int height, int numImages, IntBuffer images, IntBuffer images_copy) {
        for (int i = 0; i < numImages; ++i) {
            int start_index = i * width * height;
            Cursor.flipImage(width, height, start_index, images, images_copy);
        }
    }

    private static void flipImage(int width, int height, int start_index, IntBuffer images, IntBuffer images_copy) {
        for (int y = 0; y < height >> 1; ++y) {
            int index_y_1 = y * width + start_index;
            int index_y_2 = (height - y - 1) * width + start_index;
            for (int x = 0; x < width; ++x) {
                int index1 = index_y_1 + x;
                int index2 = index_y_2 + x;
                int temp_pixel = images.get(index1 + images.position());
                images_copy.put(index1, images.get(index2 + images.position()));
                images_copy.put(index2, temp_pixel);
            }
        }
    }

    Object getHandle() {
        this.checkValid();
        return this.cursors[this.index].cursorHandle;
    }

    private void checkValid() {
        if (this.destroyed) {
            throw new IllegalStateException("The cursor is destroyed");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void destroy() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if (this.destroyed) {
                return;
            }
            if (Mouse.getNativeCursor() == this) {
                try {
                    Mouse.setNativeCursor(null);
                }
                catch (LWJGLException e) {
                    // empty catch block
                }
            }
            for (CursorElement cursor : this.cursors) {
                Mouse.getImplementation().destroyCursor(cursor.cursorHandle);
            }
            this.destroyed = true;
        }
    }

    protected void setTimeout() {
        this.checkValid();
        this.cursors[this.index].timeout = System.currentTimeMillis() + this.cursors[this.index].delay;
    }

    protected boolean hasTimedOut() {
        this.checkValid();
        return this.cursors.length > 1 && this.cursors[this.index].timeout < System.currentTimeMillis();
    }

    protected void nextCursor() {
        this.checkValid();
        ++this.index;
        this.index %= this.cursors.length;
    }

    private static class CursorElement {
        final Object cursorHandle;
        final long delay;
        long timeout;

        CursorElement(Object cursorHandle, long delay, long timeout) {
            this.cursorHandle = cursorHandle;
            this.delay = delay;
            this.timeout = timeout;
        }
    }
}

