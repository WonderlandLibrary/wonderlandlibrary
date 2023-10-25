/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.png;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.common.ImageDescriptor;

public class PNGDescriptor
extends ImageDescriptor {
    private static final String formatName = "PNG";
    private static final String[] extensions = new String[]{"png"};
    private static final ImageFormatDescription.Signature[] signatures = new ImageFormatDescription.Signature[]{new ImageFormatDescription.Signature(-119, 80, 78, 71, 13, 10, 26, 10)};
    private static ImageDescriptor theInstance = null;

    private PNGDescriptor() {
        super(formatName, extensions, signatures);
    }

    public static synchronized ImageDescriptor getInstance() {
        if (theInstance == null) {
            theInstance = new PNGDescriptor();
        }
        return theInstance;
    }
}

