/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.ios;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.common.ImageDescriptor;

public class IosDescriptor
extends ImageDescriptor {
    private static final String formatName = "PNGorJPEGorBMP";
    private static final String[] extensions = new String[]{"bmp", "png", "jpg", "jpeg", "gif"};
    private static final ImageFormatDescription.Signature[] signatures = new ImageFormatDescription.Signature[]{new ImageFormatDescription.Signature(-1, -40), new ImageFormatDescription.Signature(-119, 80, 78, 71, 13, 10, 26, 10), new ImageFormatDescription.Signature(66, 77), new ImageFormatDescription.Signature(71, 73, 70, 56, 55, 97), new ImageFormatDescription.Signature(71, 73, 70, 56, 57, 97)};
    private static ImageDescriptor theInstance = null;

    private IosDescriptor() {
        super(formatName, extensions, signatures);
    }

    public static synchronized ImageDescriptor getInstance() {
        if (theInstance == null) {
            theInstance = new IosDescriptor();
        }
        return theInstance;
    }
}

