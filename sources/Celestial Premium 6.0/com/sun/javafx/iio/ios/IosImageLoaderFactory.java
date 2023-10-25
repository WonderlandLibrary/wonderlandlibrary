/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.ios;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import com.sun.javafx.iio.ios.IosDescriptor;
import com.sun.javafx.iio.ios.IosImageLoader;
import java.io.IOException;
import java.io.InputStream;

public class IosImageLoaderFactory
implements ImageLoaderFactory {
    private static IosImageLoaderFactory theInstance;

    private IosImageLoaderFactory() {
    }

    public static final synchronized IosImageLoaderFactory getInstance() {
        if (theInstance == null) {
            theInstance = new IosImageLoaderFactory();
        }
        return theInstance;
    }

    @Override
    public ImageFormatDescription getFormatDescription() {
        return IosDescriptor.getInstance();
    }

    @Override
    public ImageLoader createImageLoader(InputStream inputStream) throws IOException {
        return new IosImageLoader(inputStream, IosDescriptor.getInstance());
    }

    public ImageLoader createImageLoader(String string) throws IOException {
        return new IosImageLoader(string, IosDescriptor.getInstance());
    }
}

