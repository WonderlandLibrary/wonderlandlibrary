/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.image.PixelWriter
 *  javafx.scene.image.WritableImage
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.MouseButton
 *  javafx.scene.paint.Color
 *  javafx.stage.Screen
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.image.PixelUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public abstract class GlassRobot {
    public static final int MOUSE_LEFT_BTN = 1;
    public static final int MOUSE_RIGHT_BTN = 2;
    public static final int MOUSE_MIDDLE_BTN = 4;
    public static final int MOUSE_BACK_BTN = 8;
    public static final int MOUSE_FORWARD_BTN = 16;

    public abstract void create();

    public abstract void destroy();

    public abstract void keyPress(KeyCode var1);

    public abstract void keyRelease(KeyCode var1);

    public abstract double getMouseX();

    public abstract double getMouseY();

    public abstract void mouseMove(double var1, double var3);

    public abstract void mousePress(MouseButton ... var1);

    public abstract void mouseRelease(MouseButton ... var1);

    public abstract void mouseWheel(int var1);

    public abstract Color getPixelColor(double var1, double var3);

    public void getScreenCapture(int n, int n2, int n3, int n4, int[] arrn, boolean bl) {
        throw new InternalError("not implemented");
    }

    public WritableImage getScreenCapture(WritableImage writableImage, double d, double d2, double d3, double d4, boolean bl) {
        int n;
        int n2;
        int[] arrn;
        if (d3 <= 0.0) {
            throw new IllegalArgumentException("width must be > 0");
        }
        if (d4 <= 0.0) {
            throw new IllegalArgumentException("height must be > 0");
        }
        Screen screen = Screen.getPrimary();
        Objects.requireNonNull(screen);
        double d5 = screen.getOutputScaleX();
        double d6 = screen.getOutputScaleY();
        if (d5 == 1.0 && d6 == 1.0) {
            arrn = new int[(int)(d3 * d4)];
            this.getScreenCapture((int)d, (int)d2, (int)d3, (int)d4, arrn, bl);
            n2 = (int)d3;
            n = (int)d4;
        } else {
            int n3 = (int)Math.floor(d * d5);
            int n4 = (int)Math.floor(d2 * d6);
            int n5 = (int)Math.ceil((d + d3) * d5);
            int n6 = (int)Math.ceil((d2 + d4) * d6);
            int n7 = n5 - n3;
            int n8 = n6 - n4;
            int[] arrn2 = new int[n7 * n8];
            this.getScreenCapture(n3, n4, n7, n8, arrn2, bl);
            n2 = n7;
            n = n8;
            if (!bl) {
                arrn = arrn2;
            } else {
                arrn = new int[(int)(d3 * d4)];
                int n9 = 0;
                int n10 = 0;
                while ((double)n10 < d4) {
                    double d7 = (d2 + (double)n10 + 0.5) * d6 - (double)((float)n4 + 0.5f);
                    int n11 = (int)Math.floor(d7);
                    int n12 = (int)((d7 - (double)n11) * 256.0);
                    int n13 = 0;
                    while ((double)n13 < d3) {
                        double d8 = (d + (double)n13 + 0.5) * d5 - (double)((float)n3 + 0.5f);
                        int n14 = (int)Math.floor(d8);
                        int n15 = (int)((d8 - (double)n14) * 256.0);
                        arrn[n9++] = GlassRobot.interp(arrn2, n14, n11, n7, n8, n15, n12);
                        ++n13;
                    }
                    ++n10;
                }
                n2 = (int)d3;
                n = (int)d4;
            }
        }
        return GlassRobot.convertFromPixels(writableImage, Application.GetApplication().createPixels(n2, n, IntBuffer.wrap(arrn)));
    }

    public static int convertToRobotMouseButton(MouseButton[] arrmouseButton) {
        int n = 0;
        block7: for (MouseButton mouseButton : arrmouseButton) {
            switch (mouseButton) {
                case PRIMARY: {
                    n |= 1;
                    continue block7;
                }
                case SECONDARY: {
                    n |= 2;
                    continue block7;
                }
                case MIDDLE: {
                    n |= 4;
                    continue block7;
                }
                case BACK: {
                    n |= 8;
                    continue block7;
                }
                case FORWARD: {
                    n |= 0x10;
                    continue block7;
                }
                default: {
                    throw new IllegalArgumentException("MouseButton: " + mouseButton + " not supported by Robot");
                }
            }
        }
        return n;
    }

    public static Color convertFromIntArgb(int n) {
        int n2 = n >> 24 & 0xFF;
        int n3 = n >> 16 & 0xFF;
        int n4 = n >> 8 & 0xFF;
        int n5 = n & 0xFF;
        return new Color((double)n3 / 255.0, (double)n4 / 255.0, (double)n5 / 255.0, (double)n2 / 255.0);
    }

    protected static WritableImage convertFromPixels(WritableImage writableImage, Pixels pixels) {
        int n;
        Objects.requireNonNull(pixels);
        int n2 = pixels.getWidth();
        int n3 = pixels.getHeight();
        if (writableImage == null || writableImage.getWidth() != (double)n2 || writableImage.getHeight() != (double)n3) {
            writableImage = new WritableImage(n2, n3);
        }
        if ((n = pixels.getBytesPerComponent()) == 4) {
            IntBuffer intBuffer = (IntBuffer)pixels.getPixels();
            GlassRobot.writeIntBufferToImage(intBuffer, writableImage);
        } else if (n == 1) {
            ByteBuffer byteBuffer = (ByteBuffer)pixels.getPixels();
            GlassRobot.writeByteBufferToImage(byteBuffer, writableImage);
        } else {
            throw new IllegalArgumentException("bytesPerComponent must be either 4 or 1 but was: " + n);
        }
        return writableImage;
    }

    private static void writeIntBufferToImage(IntBuffer intBuffer, WritableImage writableImage) {
        Objects.requireNonNull(writableImage);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        double d = writableImage.getWidth();
        double d2 = writableImage.getHeight();
        int n = 0;
        while ((double)n < d2) {
            int n2 = 0;
            while ((double)n2 < d) {
                int n3 = intBuffer.get();
                pixelWriter.setArgb(n2, n, n3);
                ++n2;
            }
            ++n;
        }
    }

    private static void writeByteBufferToImage(ByteBuffer byteBuffer, WritableImage writableImage) {
        Objects.requireNonNull(writableImage);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        double d = writableImage.getWidth();
        double d2 = writableImage.getHeight();
        int n = Pixels.getNativeFormat();
        int n2 = 0;
        while ((double)n2 < d2) {
            int n3 = 0;
            while ((double)n3 < d) {
                if (n == 1) {
                    pixelWriter.setArgb(n3, n2, PixelUtils.PretoNonPre(GlassRobot.bgraPreToRgbaPre(byteBuffer.getInt())));
                } else if (n == 2) {
                    pixelWriter.setArgb(n3, n2, byteBuffer.getInt());
                } else {
                    throw new IllegalArgumentException("format must be either BYTE_BGRA_PRE or BYTE_ARGB");
                }
                ++n3;
            }
            ++n2;
        }
    }

    private static int bgraPreToRgbaPre(int n) {
        return Integer.reverseBytes(n);
    }

    private static int interp(int[] arrn, int n, int n2, int n3, int n4, int n5, int n6) {
        int n7;
        int n8 = 256 - n5;
        int n9 = 256 - n6;
        int n10 = n2 * n3 + n;
        int n11 = n7 = n < 0 || n2 < 0 || n >= n3 || n2 >= n4 ? 0 : arrn[n10];
        if (n6 == 0) {
            if (n5 == 0) {
                return n7;
            }
            int n12 = n2 < 0 || n + 1 >= n3 || n2 >= n4 ? 0 : arrn[n10 + 1];
            return GlassRobot.interp(n7, n12, n8, n5);
        }
        if (n5 == 0) {
            int n13 = n < 0 || n >= n3 || n2 + 1 >= n4 ? 0 : arrn[n10 + n3];
            return GlassRobot.interp(n7, n13, n9, n6);
        }
        int n14 = n2 < 0 || n + 1 >= n3 || n2 >= n4 ? 0 : arrn[n10 + 1];
        int n15 = n < 0 || n >= n3 || n2 + 1 >= n4 ? 0 : arrn[n10 + n3];
        int n16 = n + 1 >= n3 || n2 + 1 >= n4 ? 0 : arrn[n10 + n3 + 1];
        return GlassRobot.interp(GlassRobot.interp(n7, n14, n8, n5), GlassRobot.interp(n15, n16, n8, n5), n9, n6);
    }

    private static int interp(int n, int n2, int n3, int n4) {
        int n5 = n >> 24 & 0xFF;
        int n6 = n >> 16 & 0xFF;
        int n7 = n >> 8 & 0xFF;
        int n8 = n & 0xFF;
        int n9 = n2 >> 24 & 0xFF;
        int n10 = n2 >> 16 & 0xFF;
        int n11 = n2 >> 8 & 0xFF;
        int n12 = n2 & 0xFF;
        int n13 = n5 * n3 + n9 * n4 >> 8;
        int n14 = n6 * n3 + n10 * n4 >> 8;
        int n15 = n7 * n3 + n11 * n4 >> 8;
        int n16 = n8 * n3 + n12 * n4 >> 8;
        return n13 << 24 | n14 << 16 | n15 << 8 | n16;
    }
}

