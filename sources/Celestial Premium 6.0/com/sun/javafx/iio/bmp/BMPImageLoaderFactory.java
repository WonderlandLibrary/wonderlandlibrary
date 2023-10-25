/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import com.sun.javafx.iio.bmp.BMPDescriptor;
import com.sun.javafx.iio.bmp.BMPImageLoader;
import java.io.IOException;
import java.io.InputStream;

public final class BMPImageLoaderFactory
implements ImageLoaderFactory {
    private static final BMPImageLoaderFactory theInstance = new BMPImageLoaderFactory();

    public static ImageLoaderFactory getInstance() {
        return theInstance;
    }

    @Override
    public ImageFormatDescription getFormatDescription() {
        return BMPDescriptor.theInstance;
    }

    @Override
    public ImageLoader createImageLoader(InputStream inputStream) throws IOException {
        return new BMPImageLoader(inputStream);
    }
}

