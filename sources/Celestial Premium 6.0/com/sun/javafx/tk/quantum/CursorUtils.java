/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Dimension2D
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Size;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.ImageCursorFrame;
import com.sun.javafx.iio.common.PushbroomScaler;
import com.sun.javafx.iio.common.ScalerFactory;
import com.sun.prism.Image;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.geometry.Dimension2D;

final class CursorUtils {
    private CursorUtils() {
    }

    public static Cursor getPlatformCursor(CursorFrame cursorFrame) {
        Cursor cursor = cursorFrame.getPlatformCursor(Cursor.class);
        if (cursor != null) {
            return cursor;
        }
        Cursor cursor2 = CursorUtils.createPlatformCursor(cursorFrame);
        cursorFrame.setPlatforCursor(Cursor.class, cursor2);
        return cursor2;
    }

    public static Dimension2D getBestCursorSize(int n, int n2) {
        Size size = Cursor.getBestSize(n, n2);
        return new Dimension2D((double)size.width, (double)size.height);
    }

    private static Cursor createPlatformCursor(CursorFrame cursorFrame) {
        Application application = Application.GetApplication();
        switch (cursorFrame.getCursorType()) {
            case CROSSHAIR: {
                return application.createCursor(3);
            }
            case TEXT: {
                return application.createCursor(2);
            }
            case WAIT: {
                return application.createCursor(14);
            }
            case DEFAULT: {
                return application.createCursor(1);
            }
            case OPEN_HAND: {
                return application.createCursor(5);
            }
            case CLOSED_HAND: {
                return application.createCursor(4);
            }
            case HAND: {
                return application.createCursor(6);
            }
            case H_RESIZE: {
                return application.createCursor(11);
            }
            case V_RESIZE: {
                return application.createCursor(12);
            }
            case MOVE: {
                return application.createCursor(19);
            }
            case DISAPPEAR: {
                return application.createCursor(13);
            }
            case SW_RESIZE: {
                return application.createCursor(15);
            }
            case SE_RESIZE: {
                return application.createCursor(16);
            }
            case NW_RESIZE: {
                return application.createCursor(17);
            }
            case NE_RESIZE: {
                return application.createCursor(18);
            }
            case N_RESIZE: 
            case S_RESIZE: {
                return application.createCursor(12);
            }
            case W_RESIZE: 
            case E_RESIZE: {
                return application.createCursor(11);
            }
            case NONE: {
                return application.createCursor(-1);
            }
            case IMAGE: {
                return CursorUtils.createPlatformImageCursor((ImageCursorFrame)cursorFrame);
            }
        }
        System.err.println("unhandled Cursor: " + cursorFrame.getCursorType());
        return application.createCursor(1);
    }

    private static Cursor createPlatformImageCursor(ImageCursorFrame imageCursorFrame) {
        return CursorUtils.createPlatformImageCursor(imageCursorFrame.getPlatformImage(), (float)imageCursorFrame.getHotspotX(), (float)imageCursorFrame.getHotspotY());
    }

    private static Cursor createPlatformImageCursor(Object object, float f, float f2) {
        ByteBuffer byteBuffer;
        if (object == null) {
            throw new IllegalArgumentException("QuantumToolkit.createImageCursor: no image");
        }
        assert (object instanceof Image);
        Image image = (Image)object;
        int n = image.getHeight();
        int n2 = image.getWidth();
        Dimension2D dimension2D = CursorUtils.getBestCursorSize(n2, n);
        float f3 = (float)dimension2D.getWidth();
        float f4 = (float)dimension2D.getHeight();
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return Application.GetApplication().createCursor(1);
        }
        switch (image.getPixelFormat()) {
            case INT_ARGB_PRE: {
                return CursorUtils.createPlatformImageCursor((int)f, (int)f2, n2, n, image.getPixelBuffer());
            }
            case BYTE_RGB: 
            case BYTE_BGRA_PRE: 
            case BYTE_GRAY: {
                byteBuffer = (ByteBuffer)image.getPixelBuffer();
                break;
            }
            default: {
                throw new IllegalArgumentException("QuantumToolkit.createImageCursor: bad image format");
            }
        }
        float f5 = f3 / (float)n2;
        float f6 = f4 / (float)n;
        int n3 = (int)(f * f5);
        int n4 = (int)(f2 * f6);
        PushbroomScaler pushbroomScaler = ScalerFactory.createScaler(n2, n, image.getBytesPerPixelUnit(), (int)f3, (int)f4, true);
        byte[] arrby = new byte[byteBuffer.limit()];
        int n5 = image.getScanlineStride();
        for (int i = 0; i < n; ++i) {
            byteBuffer.position(i * n5);
            byteBuffer.get(arrby, 0, n5);
            if (pushbroomScaler == null) continue;
            pushbroomScaler.putSourceScanline(arrby, 0);
        }
        byteBuffer.rewind();
        Image image2 = image.iconify(pushbroomScaler.getDestination(), (int)f3, (int)f4);
        return CursorUtils.createPlatformImageCursor(n3, n4, image2.getWidth(), image2.getHeight(), image2.getPixelBuffer());
    }

    private static Cursor createPlatformImageCursor(int n, int n2, int n3, int n4, Object object) {
        Application application = Application.GetApplication();
        return application.createCursor(n, n2, application.createPixels(n3, n4, (IntBuffer)object));
    }
}

