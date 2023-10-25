/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.png;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import com.sun.javafx.iio.png.PNGDescriptor;
import com.sun.javafx.iio.png.PNGImageLoader2;
import java.io.IOException;
import java.io.InputStream;

public class PNGImageLoaderFactory
implements ImageLoaderFactory {
    private static final PNGImageLoaderFactory theInstance = new PNGImageLoaderFactory();

    private PNGImageLoaderFactory() {
    }

    public static final ImageLoaderFactory getInstance() {
        return theInstance;
    }

    @Override
    public ImageFormatDescription getFormatDescription() {
        return PNGDescriptor.getInstance();
    }

    @Override
    public ImageLoader createImageLoader(InputStream inputStream) throws IOException {
        return new PNGImageLoader2(inputStream);
    }
}

