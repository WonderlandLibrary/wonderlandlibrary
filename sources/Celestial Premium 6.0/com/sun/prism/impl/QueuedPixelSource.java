/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.prism.PixelSource;
import com.sun.prism.impl.BufferUtil;
import java.lang.ref.WeakReference;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class QueuedPixelSource
implements PixelSource {
    private volatile Pixels beingConsumed;
    private volatile Pixels enqueued;
    private final List<WeakReference<Pixels>> saved = new ArrayList<WeakReference<Pixels>>(3);
    private final boolean useDirectBuffers;

    public QueuedPixelSource(boolean bl) {
        this.useDirectBuffers = bl;
    }

    @Override
    public synchronized Pixels getLatestPixels() {
        if (this.beingConsumed != null) {
            throw new IllegalStateException("already consuming pixels: " + this.beingConsumed);
        }
        if (this.enqueued != null) {
            this.beingConsumed = this.enqueued;
            this.enqueued = null;
        }
        return this.beingConsumed;
    }

    @Override
    public synchronized void doneWithPixels(Pixels pixels) {
        if (this.beingConsumed != pixels) {
            throw new IllegalStateException("wrong pixels buffer: " + pixels + " != " + this.beingConsumed);
        }
        this.beingConsumed = null;
    }

    @Override
    public synchronized void skipLatestPixels() {
        if (this.beingConsumed != null) {
            throw new IllegalStateException("cannot skip while processing: " + this.beingConsumed);
        }
        this.enqueued = null;
    }

    private boolean usesSameBuffer(Pixels pixels, Pixels pixels2) {
        if (pixels == pixels2) {
            return true;
        }
        if (pixels == null || pixels2 == null) {
            return false;
        }
        return pixels.getBuffer() == pixels2.getBuffer();
    }

    public synchronized Pixels getUnusedPixels(int n, int n2, float f, float f2) {
        WeakReference<Pixels> weakReference;
        int n3 = 0;
        IntBuffer intBuffer = null;
        while (n3 < this.saved.size()) {
            weakReference = this.saved.get(n3);
            Pixels pixels = (Pixels)weakReference.get();
            if (pixels == null) {
                this.saved.remove(n3);
                continue;
            }
            if (this.usesSameBuffer(pixels, this.beingConsumed) || this.usesSameBuffer(pixels, this.enqueued)) {
                ++n3;
                continue;
            }
            if (pixels.getWidthUnsafe() == n && pixels.getHeightUnsafe() == n2 && pixels.getScaleXUnsafe() == f && pixels.getScaleYUnsafe() == f2) {
                return pixels;
            }
            this.saved.remove(n3);
            intBuffer = (IntBuffer)pixels.getPixels();
            if (intBuffer.capacity() >= n * n2) break;
            intBuffer = null;
        }
        if (intBuffer == null) {
            int n4 = n * n2;
            intBuffer = this.useDirectBuffers ? BufferUtil.newIntBuffer(n4) : IntBuffer.allocate(n4);
        }
        weakReference = Application.GetApplication().createPixels(n, n2, intBuffer, f, f2);
        this.saved.add(new WeakReference<Object>(weakReference));
        return weakReference;
    }

    public synchronized void enqueuePixels(Pixels pixels) {
        this.enqueued = pixels;
    }
}

