/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.ios;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageDescriptor;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.util.Map;

public class IosImageLoader
extends ImageLoaderImpl {
    public static final int GRAY = 0;
    public static final int GRAY_ALPHA = 1;
    public static final int GRAY_ALPHA_PRE = 2;
    public static final int PALETTE = 3;
    public static final int PALETTE_ALPHA = 4;
    public static final int PALETTE_ALPHA_PRE = 5;
    public static final int PALETTE_TRANS = 6;
    public static final int RGB = 7;
    public static final int RGBA = 8;
    public static final int RGBA_PRE = 9;
    private static final Map<Integer, ImageStorage.ImageType> COLOR_SPACE_MAPPING;
    private long structPointer;
    private int inWidth;
    private int inHeight;
    private int nImages;
    private boolean isDisposed = false;
    private int delayTime;
    private int loopCount;

    private static native void initNativeLoading();

    private native long loadImage(InputStream var1, boolean var2) throws IOException;

    private native long loadImageFromURL(String var1, boolean var2) throws IOException;

    private native void resizeImage(long var1, int var3, int var4);

    private native byte[] getImageBuffer(long var1, int var3);

    private native int getNumberOfComponents(long var1);

    private native int getColorSpaceCode(long var1);

    private native int getDelayTime(long var1);

    private static native void disposeLoader(long var0);

    private void setInputParameters(int n, int n2, int n3, int n4) {
        this.inWidth = n;
        this.inHeight = n2;
        this.nImages = n3;
        this.loopCount = n4;
    }

    private void updateProgress(float f) {
        this.updateImageProgress(f);
    }

    private boolean shouldReportProgress() {
        return this.listeners != null && !this.listeners.isEmpty();
    }

    private void checkNativePointer() throws IOException {
        if (this.structPointer == 0L) {
            throw new IOException("Unable to initialize image native loader!");
        }
    }

    private void retrieveDelayTime() {
        if (this.nImages > 1) {
            this.delayTime = this.getDelayTime(this.structPointer);
        }
    }

    public IosImageLoader(String string, ImageDescriptor imageDescriptor) throws IOException {
        super(imageDescriptor);
        try {
            URL uRL = new URL(string);
        }
        catch (MalformedURLException malformedURLException) {
            throw new IllegalArgumentException("Image loader: Malformed URL!");
        }
        try {
            this.structPointer = this.loadImageFromURL(string, this.shouldReportProgress());
        }
        catch (IOException iOException) {
            this.dispose();
            throw iOException;
        }
        this.checkNativePointer();
        this.retrieveDelayTime();
    }

    public IosImageLoader(InputStream inputStream, ImageDescriptor imageDescriptor) throws IOException {
        super(imageDescriptor);
        if (inputStream == null) {
            throw new IllegalArgumentException("Image loader: input stream == null");
        }
        try {
            this.structPointer = this.loadImage(inputStream, this.shouldReportProgress());
        }
        catch (IOException iOException) {
            this.dispose();
            throw iOException;
        }
        this.checkNativePointer();
        this.retrieveDelayTime();
    }

    @Override
    public synchronized void dispose() {
        if (!this.isDisposed && this.structPointer != 0L) {
            this.isDisposed = true;
            IosImageLoader.disposeLoader(this.structPointer);
            this.structPointer = 0L;
        }
    }

    @Override
    public ImageFrame load(int n, int n2, int n3, boolean bl, boolean bl2) throws IOException {
        if (n >= this.nImages) {
            this.dispose();
            return null;
        }
        int[] arrn = ImageTools.computeDimensions(this.inWidth, this.inHeight, n2, n3, bl);
        n2 = arrn[0];
        n3 = arrn[1];
        ImageMetadata imageMetadata = new ImageMetadata(null, true, null, null, null, this.delayTime == 0 ? null : Integer.valueOf(this.delayTime), this.nImages > 1 ? Integer.valueOf(this.loopCount) : null, n2, n3, null, null, null);
        this.updateImageMetadata(imageMetadata);
        this.resizeImage(this.structPointer, n2, n3);
        int n4 = this.getNumberOfComponents(this.structPointer);
        int n5 = this.getColorSpaceCode(this.structPointer);
        ImageStorage.ImageType imageType = COLOR_SPACE_MAPPING.get(n5);
        byte[] arrby = this.getImageBuffer(this.structPointer, n);
        return new ImageFrame(imageType, ByteBuffer.wrap(arrby), n2, n3, n2 * n4, null, imageMetadata);
    }

    static {
        Object object = AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("nativeiio");
            return null;
        });
        COLOR_SPACE_MAPPING = Map.of(0, ImageStorage.ImageType.GRAY, 1, ImageStorage.ImageType.GRAY_ALPHA, 2, ImageStorage.ImageType.GRAY_ALPHA_PRE, 3, ImageStorage.ImageType.PALETTE, 4, ImageStorage.ImageType.PALETTE_ALPHA, 5, ImageStorage.ImageType.PALETTE_ALPHA_PRE, 6, ImageStorage.ImageType.PALETTE_TRANS, 7, ImageStorage.ImageType.RGB, 8, ImageStorage.ImageType.RGBA, 9, ImageStorage.ImageType.RGBA_PRE);
        IosImageLoader.initNativeLoading();
    }
}

