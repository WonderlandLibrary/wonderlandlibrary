/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.jpeg;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.common.ImageDescriptor;

public class JPEGDescriptor
extends ImageDescriptor {
    public static final int SOI = 216;
    private static final String formatName = "JPEG";
    private static final String[] extensions = new String[]{"jpg", "jpeg"};
    private static final ImageFormatDescription.Signature[] signatures = new ImageFormatDescription.Signature[]{new ImageFormatDescription.Signature(-1, -40)};
    private static ImageDescriptor theInstance = null;

    private JPEGDescriptor() {
        super(formatName, extensions, signatures);
    }

    public static synchronized ImageDescriptor getInstance() {
        if (theInstance == null) {
            theInstance = new JPEGDescriptor();
        }
        return theInstance;
    }
}

