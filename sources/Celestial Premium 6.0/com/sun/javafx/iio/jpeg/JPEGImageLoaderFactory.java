/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.jpeg;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import com.sun.javafx.iio.jpeg.JPEGDescriptor;
import com.sun.javafx.iio.jpeg.JPEGImageLoader;
import java.io.IOException;
import java.io.InputStream;

public class JPEGImageLoaderFactory
implements ImageLoaderFactory {
    private static final JPEGImageLoaderFactory theInstance = new JPEGImageLoaderFactory();

    private JPEGImageLoaderFactory() {
    }

    public static final ImageLoaderFactory getInstance() {
        return theInstance;
    }

    @Override
    public ImageFormatDescription getFormatDescription() {
        return JPEGDescriptor.getInstance();
    }

    @Override
    public ImageLoader createImageLoader(InputStream inputStream) throws IOException {
        return new JPEGImageLoader(inputStream);
    }
}

