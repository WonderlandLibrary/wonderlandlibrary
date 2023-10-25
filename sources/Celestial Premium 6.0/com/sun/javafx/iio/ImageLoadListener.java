/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio;

import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageMetadata;

public interface ImageLoadListener {
    public void imageLoadProgress(ImageLoader var1, float var2);

    public void imageLoadWarning(ImageLoader var1, String var2);

    public void imageLoadMetaData(ImageLoader var1, ImageMetadata var2);
}

