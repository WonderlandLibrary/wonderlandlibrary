/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 */
package com.sun.javafx.iio;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageLoadListener;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import com.sun.javafx.iio.ImageStorageException;
import com.sun.javafx.iio.bmp.BMPImageLoaderFactory;
import com.sun.javafx.iio.common.ImageTools;
import com.sun.javafx.iio.gif.GIFImageLoaderFactory;
import com.sun.javafx.iio.ios.IosImageLoaderFactory;
import com.sun.javafx.iio.jpeg.JPEGImageLoaderFactory;
import com.sun.javafx.iio.png.PNGImageLoaderFactory;
import com.sun.javafx.util.DataURI;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageStorage {
    private static final HashMap<ImageFormatDescription.Signature, ImageLoaderFactory> loaderFactoriesBySignature;
    private static final ImageLoaderFactory[] loaderFactories;
    private static final boolean isIOS;
    private static int maxSignatureLength;

    public static ImageFormatDescription[] getSupportedDescriptions() {
        ImageFormatDescription[] arrimageFormatDescription = new ImageFormatDescription[loaderFactories.length];
        for (int i = 0; i < loaderFactories.length; ++i) {
            arrimageFormatDescription[i] = loaderFactories[i].getFormatDescription();
        }
        return arrimageFormatDescription;
    }

    public static int getNumBands(ImageType imageType) {
        int n = -1;
        switch (imageType) {
            case GRAY: 
            case PALETTE: 
            case PALETTE_ALPHA: 
            case PALETTE_ALPHA_PRE: 
            case PALETTE_TRANS: {
                n = 1;
                break;
            }
            case GRAY_ALPHA: 
            case GRAY_ALPHA_PRE: {
                n = 2;
                break;
            }
            case RGB: {
                n = 3;
                break;
            }
            case RGBA: 
            case RGBA_PRE: {
                n = 4;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown ImageType " + imageType);
            }
        }
        return n;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void addImageLoaderFactory(ImageLoaderFactory imageLoaderFactory) {
        ImageFormatDescription imageFormatDescription = imageLoaderFactory.getFormatDescription();
        for (ImageFormatDescription.Signature signature : imageFormatDescription.getSignatures()) {
            loaderFactoriesBySignature.put(signature, imageLoaderFactory);
        }
        Class<ImageStorage> class_ = ImageStorage.class;
        synchronized (ImageStorage.class) {
            maxSignatureLength = -1;
            // ** MonitorExit[var2_2] (shouldn't be in output)
            return;
        }
    }

    public static ImageFrame[] loadAll(InputStream inputStream, ImageLoadListener imageLoadListener, double d, double d2, boolean bl, float f, boolean bl2) throws ImageStorageException {
        ImageFrame[] arrimageFrame;
        block8: {
            ImageLoader imageLoader = null;
            arrimageFrame = null;
            try {
                imageLoader = isIOS ? IosImageLoaderFactory.getInstance().createImageLoader(inputStream) : ImageStorage.getLoaderBySignature(inputStream, imageLoadListener);
                if (imageLoader != null) {
                    arrimageFrame = ImageStorage.loadAll(imageLoader, d, d2, bl, f, bl2);
                    break block8;
                }
                throw new ImageStorageException("No loader for image data");
            }
            catch (ImageStorageException imageStorageException) {
                throw imageStorageException;
            }
            catch (IOException iOException) {
                throw new ImageStorageException(iOException.getMessage(), iOException);
            }
            finally {
                if (imageLoader != null) {
                    imageLoader.dispose();
                }
            }
        }
        return arrimageFrame;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ImageFrame[] loadAll(String string, ImageLoadListener imageLoadListener, double d, double d2, boolean bl, float f, boolean bl2) throws ImageStorageException {
        ImageFrame[] arrimageFrame;
        block21: {
            if (string == null || string.isEmpty()) {
                throw new ImageStorageException("URL can't be null or empty");
            }
            arrimageFrame = null;
            InputStream inputStream = null;
            ImageLoader imageLoader = null;
            try {
                float f2 = 1.0f;
                try {
                    if (f >= 1.5f) {
                        try {
                            String string2 = ImageTools.getScaledImageName(string);
                            inputStream = ImageTools.createInputStream(string2);
                            f2 = 2.0f;
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                    }
                    if (inputStream == null) {
                        try {
                            inputStream = ImageTools.createInputStream(string);
                        }
                        catch (IOException iOException) {
                            DataURI dataURI = DataURI.tryParse(string);
                            if (dataURI != null) {
                                String string3 = dataURI.getMimeType();
                                if (string3 != null && !"image".equalsIgnoreCase(dataURI.getMimeType())) {
                                    throw new IllegalArgumentException("Unexpected MIME type: " + dataURI.getMimeType());
                                }
                                inputStream = new ByteArrayInputStream(dataURI.getData());
                            }
                            throw iOException;
                        }
                    }
                    imageLoader = isIOS ? IosImageLoaderFactory.getInstance().createImageLoader(inputStream) : ImageStorage.getLoaderBySignature(inputStream, imageLoadListener);
                }
                catch (Exception exception) {
                    throw new ImageStorageException(exception.getMessage(), exception);
                }
                if (imageLoader != null) {
                    arrimageFrame = ImageStorage.loadAll(imageLoader, d, d2, bl, f2, bl2);
                    break block21;
                }
                throw new ImageStorageException("No loader for image data");
            }
            finally {
                if (imageLoader != null) {
                    imageLoader.dispose();
                }
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                catch (IOException iOException) {}
            }
        }
        return arrimageFrame;
    }

    private static synchronized int getMaxSignatureLength() {
        if (maxSignatureLength < 0) {
            maxSignatureLength = 0;
            for (ImageFormatDescription.Signature signature : loaderFactoriesBySignature.keySet()) {
                int n = signature.getLength();
                if (maxSignatureLength >= n) continue;
                maxSignatureLength = n;
            }
        }
        return maxSignatureLength;
    }

    private static ImageFrame[] loadAll(ImageLoader imageLoader, double d, double d2, boolean bl, float f, boolean bl2) throws ImageStorageException {
        ImageFrame[] arrimageFrame = null;
        ArrayList<ImageFrame> arrayList = new ArrayList<ImageFrame>();
        int n = 0;
        ImageFrame imageFrame = null;
        int n2 = (int)Math.round(d * (double)f);
        int n3 = (int)Math.round(d2 * (double)f);
        while (true) {
            try {
                imageFrame = imageLoader.load(n++, n2, n3, bl, bl2);
            }
            catch (Exception exception) {
                if (n > 1) break;
                throw new ImageStorageException(exception.getMessage(), exception);
            }
            if (imageFrame == null) break;
            imageFrame.setPixelScale(f);
            arrayList.add(imageFrame);
        }
        int n4 = arrayList.size();
        if (n4 > 0) {
            arrimageFrame = new ImageFrame[n4];
            arrayList.toArray(arrimageFrame);
        }
        return arrimageFrame;
    }

    private static ImageLoader getLoaderBySignature(InputStream inputStream, ImageLoadListener imageLoadListener) throws IOException {
        byte[] arrby = new byte[ImageStorage.getMaxSignatureLength()];
        ImageTools.readFully(inputStream, arrby);
        for (Map.Entry<ImageFormatDescription.Signature, ImageLoaderFactory> entry : loaderFactoriesBySignature.entrySet()) {
            if (!entry.getKey().matches(arrby)) continue;
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
            SequenceInputStream sequenceInputStream = new SequenceInputStream(byteArrayInputStream, inputStream);
            ImageLoader imageLoader = entry.getValue().createImageLoader(sequenceInputStream);
            if (imageLoadListener != null) {
                imageLoader.addListener(imageLoadListener);
            }
            return imageLoader;
        }
        return null;
    }

    private ImageStorage() {
    }

    static {
        isIOS = PlatformUtil.isIOS();
        loaderFactories = isIOS ? new ImageLoaderFactory[]{IosImageLoaderFactory.getInstance()} : new ImageLoaderFactory[]{GIFImageLoaderFactory.getInstance(), JPEGImageLoaderFactory.getInstance(), PNGImageLoaderFactory.getInstance(), BMPImageLoaderFactory.getInstance()};
        loaderFactoriesBySignature = new HashMap(loaderFactories.length);
        for (int i = 0; i < loaderFactories.length; ++i) {
            ImageStorage.addImageLoaderFactory(loaderFactories[i]);
        }
    }

    public static final class ImageType
    extends Enum<ImageType> {
        public static final /* enum */ ImageType GRAY = new ImageType();
        public static final /* enum */ ImageType GRAY_ALPHA = new ImageType();
        public static final /* enum */ ImageType GRAY_ALPHA_PRE = new ImageType();
        public static final /* enum */ ImageType PALETTE = new ImageType();
        public static final /* enum */ ImageType PALETTE_ALPHA = new ImageType();
        public static final /* enum */ ImageType PALETTE_ALPHA_PRE = new ImageType();
        public static final /* enum */ ImageType PALETTE_TRANS = new ImageType();
        public static final /* enum */ ImageType RGB = new ImageType();
        public static final /* enum */ ImageType RGBA = new ImageType();
        public static final /* enum */ ImageType RGBA_PRE = new ImageType();
        private static final /* synthetic */ ImageType[] $VALUES;

        public static ImageType[] values() {
            return (ImageType[])$VALUES.clone();
        }

        public static ImageType valueOf(String string) {
            return Enum.valueOf(ImageType.class, string);
        }

        private static /* synthetic */ ImageType[] $values() {
            return new ImageType[]{GRAY, GRAY_ALPHA, GRAY_ALPHA_PRE, PALETTE, PALETTE_ALPHA, PALETTE_ALPHA_PRE, PALETTE_TRANS, RGB, RGBA, RGBA_PRE};
        }

        static {
            $VALUES = ImageType.$values();
        }
    }
}

