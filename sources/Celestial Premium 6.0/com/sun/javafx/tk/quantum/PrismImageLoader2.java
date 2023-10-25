/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PlatformLogger
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageLoadListener;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.ImageStorageException;
import com.sun.javafx.logging.PlatformLogger;
import com.sun.javafx.runtime.async.AbstractRemoteResource;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.prism.Image;
import com.sun.prism.impl.PrismSettings;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class PrismImageLoader2
implements ImageLoader {
    private static PlatformLogger imageioLogger = null;
    private Image[] images;
    private int[] delayTimes;
    private int loopCount;
    private double width;
    private double height;
    private float pixelScale;
    private Exception exception;

    public PrismImageLoader2(String string, double d, double d2, boolean bl, float f, boolean bl2) {
        this.loadAll(string, d, d2, bl, f, bl2);
    }

    public PrismImageLoader2(InputStream inputStream, double d, double d2, boolean bl, boolean bl2) {
        this.loadAll(inputStream, d, d2, bl, bl2);
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public int getFrameCount() {
        if (this.images == null) {
            return 0;
        }
        return this.images.length;
    }

    @Override
    public PlatformImage getFrame(int n) {
        if (this.images == null) {
            return null;
        }
        return this.images[n];
    }

    @Override
    public int getFrameDelay(int n) {
        if (this.images == null) {
            return 0;
        }
        return this.delayTimes[n];
    }

    @Override
    public int getLoopCount() {
        if (this.images == null) {
            return 0;
        }
        return this.loopCount;
    }

    @Override
    public Exception getException() {
        return this.exception;
    }

    private void loadAll(String string, double d, double d2, boolean bl, float f, boolean bl2) {
        PrismLoadListener prismLoadListener = new PrismLoadListener();
        try {
            ImageFrame[] arrimageFrame = ImageStorage.loadAll(string, (ImageLoadListener)prismLoadListener, d, d2, bl, f, bl2);
            this.convertAll(arrimageFrame);
        }
        catch (ImageStorageException imageStorageException) {
            this.handleException(imageStorageException);
        }
        catch (Exception exception) {
            this.handleException(exception);
        }
    }

    private void loadAll(InputStream inputStream, double d, double d2, boolean bl, boolean bl2) {
        PrismLoadListener prismLoadListener = new PrismLoadListener();
        try {
            ImageFrame[] arrimageFrame = ImageStorage.loadAll(inputStream, (ImageLoadListener)prismLoadListener, d, d2, bl, 1.0f, bl2);
            this.convertAll(arrimageFrame);
        }
        catch (ImageStorageException imageStorageException) {
            this.handleException(imageStorageException);
        }
        catch (Exception exception) {
            this.handleException(exception);
        }
    }

    private void handleException(ImageStorageException imageStorageException) {
        Throwable throwable = imageStorageException.getCause();
        if (throwable instanceof Exception) {
            this.handleException((Exception)throwable);
        } else {
            this.handleException((Exception)imageStorageException);
        }
    }

    private void handleException(Exception exception) {
        if (PrismSettings.verbose) {
            exception.printStackTrace(System.err);
        }
        this.exception = exception;
    }

    private void convertAll(ImageFrame[] arrimageFrame) {
        int n = arrimageFrame.length;
        this.images = new Image[n];
        this.delayTimes = new int[n];
        for (int i = 0; i < n; ++i) {
            ImageFrame imageFrame = arrimageFrame[i];
            this.images[i] = Image.convertImageFrame(imageFrame);
            ImageMetadata imageMetadata = imageFrame.getMetadata();
            if (imageMetadata != null) {
                Integer n2;
                Integer n3 = imageMetadata.delayTime;
                if (n3 != null) {
                    this.delayTimes[i] = n3;
                }
                if ((n2 = imageMetadata.loopCount) != null) {
                    this.loopCount = n2;
                }
            }
            if (i != 0) continue;
            this.width = imageFrame.getWidth();
            this.height = imageFrame.getHeight();
        }
    }

    private static synchronized PlatformLogger getImageioLogger() {
        if (imageioLogger == null) {
            imageioLogger = PlatformLogger.getLogger((String)"javafx.scene.image");
        }
        return imageioLogger;
    }

    private class PrismLoadListener
    implements ImageLoadListener {
        private PrismLoadListener() {
        }

        @Override
        public void imageLoadWarning(com.sun.javafx.iio.ImageLoader imageLoader, String string) {
            PrismImageLoader2.getImageioLogger().warning(string);
        }

        @Override
        public void imageLoadProgress(com.sun.javafx.iio.ImageLoader imageLoader, float f) {
        }

        @Override
        public void imageLoadMetaData(com.sun.javafx.iio.ImageLoader imageLoader, ImageMetadata imageMetadata) {
        }
    }

    static final class AsyncImageLoader
    extends AbstractRemoteResource<PrismImageLoader2> {
        private static final ExecutorService BG_LOADING_EXECUTOR = AsyncImageLoader.createExecutor();
        private final AccessControlContext acc;
        double width;
        double height;
        boolean preserveRatio;
        boolean smooth;

        public AsyncImageLoader(AsyncOperationListener<PrismImageLoader2> asyncOperationListener, String string, double d, double d2, boolean bl, boolean bl2) {
            super(string, asyncOperationListener);
            this.width = d;
            this.height = d2;
            this.preserveRatio = bl;
            this.smooth = bl2;
            this.acc = AccessController.getContext();
        }

        @Override
        protected PrismImageLoader2 processStream(InputStream inputStream) throws IOException {
            return new PrismImageLoader2(inputStream, this.width, this.height, this.preserveRatio, this.smooth);
        }

        @Override
        public PrismImageLoader2 call() throws IOException {
            try {
                return AccessController.doPrivileged(() -> (PrismImageLoader2)AsyncImageLoader.super.call(), this.acc);
            }
            catch (PrivilegedActionException privilegedActionException) {
                Throwable throwable = privilegedActionException.getCause();
                if (throwable instanceof IOException) {
                    throw (IOException)throwable;
                }
                throw new UndeclaredThrowableException(throwable);
            }
        }

        @Override
        public void start() {
            BG_LOADING_EXECUTOR.execute(this.future);
        }

        private static ExecutorService createExecutor() {
            ThreadGroup threadGroup = AccessController.doPrivileged(() -> new ThreadGroup(QuantumToolkit.getFxUserThread().getThreadGroup(), "Background image loading thread pool"));
            ThreadFactory threadFactory = runnable -> AccessController.doPrivileged(() -> {
                Thread thread = new Thread(threadGroup, runnable);
                thread.setPriority(1);
                return thread;
            });
            ExecutorService executorService = Executors.newCachedThreadPool(threadFactory);
            ((ThreadPoolExecutor)executorService).setKeepAliveTime(1L, TimeUnit.SECONDS);
            return executorService;
        }
    }
}

