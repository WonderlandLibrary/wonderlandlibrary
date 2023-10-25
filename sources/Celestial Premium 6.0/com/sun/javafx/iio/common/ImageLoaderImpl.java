/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.common;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoadListener;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageMetadata;
import java.util.HashSet;

public abstract class ImageLoaderImpl
implements ImageLoader {
    protected ImageFormatDescription formatDescription;
    protected HashSet<ImageLoadListener> listeners;
    protected int lastPercentDone = -1;

    protected ImageLoaderImpl(ImageFormatDescription imageFormatDescription) {
        if (imageFormatDescription == null) {
            throw new IllegalArgumentException("formatDescription == null!");
        }
        this.formatDescription = imageFormatDescription;
    }

    @Override
    public final ImageFormatDescription getFormatDescription() {
        return this.formatDescription;
    }

    @Override
    public final void addListener(ImageLoadListener imageLoadListener) {
        if (this.listeners == null) {
            this.listeners = new HashSet();
        }
        this.listeners.add(imageLoadListener);
    }

    @Override
    public final void removeListener(ImageLoadListener imageLoadListener) {
        if (this.listeners != null) {
            this.listeners.remove(imageLoadListener);
        }
    }

    protected void emitWarning(String string) {
        if (this.listeners != null && !this.listeners.isEmpty()) {
            for (ImageLoadListener imageLoadListener : this.listeners) {
                imageLoadListener.imageLoadWarning(this, string);
            }
        }
    }

    protected void updateImageProgress(float f) {
        int n;
        int n2;
        if (this.listeners != null && !this.listeners.isEmpty() && (n2 = 5) * (n = (int)f) / n2 % n2 == 0 && n != this.lastPercentDone) {
            this.lastPercentDone = n;
            for (ImageLoadListener imageLoadListener : this.listeners) {
                imageLoadListener.imageLoadProgress(this, n);
            }
        }
    }

    protected void updateImageMetadata(ImageMetadata imageMetadata) {
        if (this.listeners != null && !this.listeners.isEmpty()) {
            for (ImageLoadListener imageLoadListener : this.listeners) {
                imageLoadListener.imageLoadMetaData(this, imageMetadata);
            }
        }
    }
}

