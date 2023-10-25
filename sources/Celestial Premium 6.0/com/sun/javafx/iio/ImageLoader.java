/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageLoadListener;
import java.io.IOException;

public interface ImageLoader {
    public ImageFormatDescription getFormatDescription();

    public void dispose();

    public void addListener(ImageLoadListener var1);

    public void removeListener(ImageLoadListener var1);

    public ImageFrame load(int var1, int var2, int var3, boolean var4, boolean var5) throws IOException;
}

