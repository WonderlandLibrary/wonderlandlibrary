/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import java.io.IOException;
import java.io.InputStream;

public interface ImageLoaderFactory {
    public ImageFormatDescription getFormatDescription();

    public ImageLoader createImageLoader(InputStream var1) throws IOException;
}

