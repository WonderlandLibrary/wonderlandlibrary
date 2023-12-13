/*
 * Decompiled with CFR 0.152.
 */
package jaco.a;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.PixelGrabber;
import java.net.URL;
import javax.swing.ImageIcon;

public final class b {
    static {
        try {
            float[] fArray = new float[9];
            int n2 = 0;
            while (n2 < fArray.length) {
                fArray[n2] = 0.11111111f;
                ++n2;
            }
            new ConvolveOp(new Kernel(3, 3, fArray), 1, null);
            return;
        }
        catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
    }

    private static BufferedImage a(int n2, int n3, boolean bl) {
        try {
            Object object = GraphicsEnvironment.getLocalGraphicsEnvironment();
            object = ((GraphicsEnvironment)object).getDefaultScreenDevice();
            object = ((GraphicsDevice)object).getDefaultConfiguration();
            return ((GraphicsConfiguration)object).createCompatibleImage(n2, n3, bl ? 3 : 1);
        }
        catch (Throwable throwable) {
            return new BufferedImage(n2, n3, bl ? 2 : 1);
        }
    }

    public static BufferedImage a(URL object) {
        if (((Image)(object = new ImageIcon((URL)object).getImage())).getWidth(null) == -1 || ((Image)object).getHeight(null) == -1) {
            return null;
        }
        boolean bl = false;
        bl = false;
        Object object2 = object;
        BufferedImage bufferedImage = b.a(0 + ((Image)object2).getWidth(null), 0 + ((Image)object2).getHeight(null), b.a((Image)object2));
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.drawImage((Image)object2, 0, 0, null);
        graphics2D.dispose();
        return bufferedImage;
    }

    private static BufferedImage c(BufferedImage bufferedImage, float f2) {
        BufferedImage bufferedImage2 = bufferedImage;
        BufferedImage bufferedImage3 = bufferedImage2;
        bufferedImage3 = b.a(bufferedImage2.getWidth(), bufferedImage3.getHeight(), bufferedImage3.getColorModel().hasAlpha());
        a.a.a.b b2 = new a.a.a.b();
        b2.a(f2);
        b2.filter(bufferedImage, bufferedImage3);
        return bufferedImage3;
    }

    public static BufferedImage a(BufferedImage bufferedImage, float f2) {
        return b.c(bufferedImage, f2);
    }

    public static BufferedImage b(BufferedImage bufferedImage, float f2) {
        return b.c(bufferedImage, -f2);
    }

    private static boolean a(Image object) {
        if (object instanceof BufferedImage) {
            object = (BufferedImage)object;
            return ((BufferedImage)object).getColorModel().hasAlpha();
        }
        object = new PixelGrabber((Image)object, 0, 0, 1, 1, false);
        try {
            ((PixelGrabber)object).grabPixels();
        }
        catch (InterruptedException interruptedException) {}
        return ((PixelGrabber)object).getColorModel().hasAlpha();
    }
}

