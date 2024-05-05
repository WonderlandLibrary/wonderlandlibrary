/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.imageout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import me.kiras.aimwhere.libraries.slick.Image;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.imageout.ImageWriter;
import me.kiras.aimwhere.libraries.slick.imageout.ImageWriterFactory;

public class ImageOut {
    private static final boolean DEFAULT_ALPHA_WRITE = false;
    public static String TGA = "tga";
    public static String PNG = "png";
    public static String JPG = "jpg";

    public static String[] getSupportedFormats() {
        return ImageWriterFactory.getSupportedFormats();
    }

    public static void write(Image image2, String format, OutputStream out) throws SlickException {
        ImageOut.write(image2, format, out, false);
    }

    public static void write(Image image2, String format, OutputStream out, boolean writeAlpha) throws SlickException {
        try {
            ImageWriter writer = ImageWriterFactory.getWriterForFormat(format);
            writer.saveImage(image2, format, out, writeAlpha);
        }
        catch (IOException e) {
            throw new SlickException("Unable to write out the image in format: " + format, e);
        }
    }

    public static void write(Image image2, String dest) throws SlickException {
        ImageOut.write(image2, dest, false);
    }

    public static void write(Image image2, String dest, boolean writeAlpha) throws SlickException {
        try {
            int ext = dest.lastIndexOf(46);
            if (ext < 0) {
                throw new SlickException("Unable to determine format from: " + dest);
            }
            String format = dest.substring(ext + 1);
            ImageOut.write(image2, format, new FileOutputStream(dest), writeAlpha);
        }
        catch (IOException e) {
            throw new SlickException("Unable to write to the destination: " + dest, e);
        }
    }

    public static void write(Image image2, String format, String dest) throws SlickException {
        ImageOut.write(image2, format, dest, false);
    }

    public static void write(Image image2, String format, String dest, boolean writeAlpha) throws SlickException {
        try {
            ImageOut.write(image2, format, new FileOutputStream(dest), writeAlpha);
        }
        catch (IOException e) {
            throw new SlickException("Unable to write to the destination: " + dest, e);
        }
    }
}

