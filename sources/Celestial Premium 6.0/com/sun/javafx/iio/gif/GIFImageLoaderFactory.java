/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.gif;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import com.sun.javafx.iio.gif.GIFDescriptor;
import com.sun.javafx.iio.gif.GIFImageLoader2;
import java.io.IOException;
import java.io.InputStream;

public class GIFImageLoaderFactory
implements ImageLoaderFactory {
    private static final GIFImageLoaderFactory theInstance = new GIFImageLoaderFactory();

    private GIFImageLoaderFactory() {
    }

    public static final ImageLoaderFactory getInstance() {
        return theInstance;
    }

    @Override
    public ImageFormatDescription getFormatDescription() {
        return GIFDescriptor.getInstance();
    }

    @Override
    public ImageLoader createImageLoader(InputStream inputStream) throws IOException {
        return new GIFImageLoader2(inputStream);
    }
}

