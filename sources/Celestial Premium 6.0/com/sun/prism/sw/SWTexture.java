/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.sw.SWArgbPreTexture;
import com.sun.prism.sw.SWMaskTexture;
import com.sun.prism.sw.SWResourceFactory;

abstract class SWTexture
implements Texture {
    boolean allocated = false;
    int physicalWidth;
    int physicalHeight;
    int contentWidth;
    int contentHeight;
    private SWResourceFactory factory;
    private int lastImageSerial;
    private final Texture.WrapMode wrapMode;
    private boolean linearFiltering = true;
    private int lockcount;
    boolean permanent;
    int employcount;

    static Texture create(SWResourceFactory sWResourceFactory, PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n, int n2) {
        switch (pixelFormat) {
            case BYTE_ALPHA: {
                return new SWMaskTexture(sWResourceFactory, wrapMode, n, n2);
            }
        }
        return new SWArgbPreTexture(sWResourceFactory, wrapMode, n, n2);
    }

    SWTexture(SWResourceFactory sWResourceFactory, Texture.WrapMode wrapMode, int n, int n2) {
        this.factory = sWResourceFactory;
        this.wrapMode = wrapMode;
        this.physicalWidth = n;
        this.physicalHeight = n2;
        this.contentWidth = n;
        this.contentHeight = n2;
        this.lock();
    }

    SWTexture(SWTexture sWTexture, Texture.WrapMode wrapMode) {
        this.allocated = sWTexture.allocated;
        this.physicalWidth = sWTexture.physicalWidth;
        this.physicalHeight = sWTexture.physicalHeight;
        this.contentWidth = sWTexture.contentWidth;
        this.contentHeight = sWTexture.contentHeight;
        this.factory = sWTexture.factory;
        this.lastImageSerial = sWTexture.lastImageSerial;
        this.linearFiltering = sWTexture.linearFiltering;
        this.wrapMode = wrapMode;
        this.lock();
    }

    SWResourceFactory getResourceFactory() {
        return this.factory;
    }

    int getOffset() {
        return 0;
    }

    @Override
    public void lock() {
        ++this.lockcount;
    }

    @Override
    public void unlock() {
        this.assertLocked();
        --this.lockcount;
    }

    @Override
    public boolean isLocked() {
        return this.lockcount > 0;
    }

    @Override
    public int getLockCount() {
        return this.lockcount;
    }

    @Override
    public void assertLocked() {
        if (this.lockcount <= 0) {
            throw new IllegalStateException("texture not locked");
        }
    }

    @Override
    public void makePermanent() {
        this.permanent = true;
    }

    @Override
    public void contentsUseful() {
        this.assertLocked();
        ++this.employcount;
    }

    @Override
    public void contentsNotUseful() {
        if (this.employcount <= 0) {
            throw new IllegalStateException("Resource obsoleted too many times");
        }
        --this.employcount;
    }

    @Override
    public boolean isSurfaceLost() {
        return false;
    }

    @Override
    public void dispose() {
    }

    @Override
    public int getPhysicalWidth() {
        return this.physicalWidth;
    }

    @Override
    public int getPhysicalHeight() {
        return this.physicalHeight;
    }

    @Override
    public int getContentX() {
        return 0;
    }

    @Override
    public int getContentY() {
        return 0;
    }

    @Override
    public int getContentWidth() {
        return this.contentWidth;
    }

    @Override
    public void setContentWidth(int n) {
        if (n > this.physicalWidth) {
            throw new IllegalArgumentException("contentWidth cannot exceed physicalWidth");
        }
        this.contentWidth = n;
    }

    @Override
    public int getContentHeight() {
        return this.contentHeight;
    }

    @Override
    public void setContentHeight(int n) {
        if (n > this.physicalHeight) {
            throw new IllegalArgumentException("contentHeight cannot exceed physicalHeight");
        }
        this.contentHeight = n;
    }

    @Override
    public int getMaxContentWidth() {
        return this.getPhysicalWidth();
    }

    @Override
    public int getMaxContentHeight() {
        return this.getPhysicalHeight();
    }

    @Override
    public int getLastImageSerial() {
        return this.lastImageSerial;
    }

    @Override
    public void setLastImageSerial(int n) {
        this.lastImageSerial = n;
    }

    @Override
    public void update(Image image) {
        this.update(image, 0, 0);
    }

    @Override
    public void update(Image image, int n, int n2) {
        this.update(image, n, n2, image.getWidth(), image.getHeight());
    }

    @Override
    public void update(Image image, int n, int n2, int n3, int n4) {
        this.update(image, n, n2, n3, n4, false);
    }

    @Override
    public void update(Image image, int n, int n2, int n3, int n4, boolean bl) {
        if (PrismSettings.debug) {
            System.out.println("IMG.Bytes per pixel: " + image.getBytesPerPixelUnit());
            System.out.println("IMG.scanline: " + image.getScanlineStride());
        }
        this.update(image.getPixelBuffer(), image.getPixelFormat(), n, n2, image.getMinX(), image.getMinY(), n3, n4, image.getScanlineStride(), bl);
    }

    @Override
    public Texture.WrapMode getWrapMode() {
        return this.wrapMode;
    }

    @Override
    public boolean getUseMipmap() {
        return false;
    }

    @Override
    public Texture getSharedTexture(Texture.WrapMode wrapMode) {
        this.assertLocked();
        if (this.wrapMode == wrapMode) {
            this.lock();
            return this;
        }
        switch (wrapMode) {
            case REPEAT: {
                if (this.wrapMode == Texture.WrapMode.CLAMP_TO_EDGE) break;
                return null;
            }
            case CLAMP_TO_EDGE: {
                if (this.wrapMode == Texture.WrapMode.REPEAT) break;
                return null;
            }
            default: {
                return null;
            }
        }
        return this.createSharedLockedTexture(wrapMode);
    }

    @Override
    public boolean getLinearFiltering() {
        return this.linearFiltering;
    }

    @Override
    public void setLinearFiltering(boolean bl) {
        this.linearFiltering = bl;
    }

    void allocate() {
        if (this.allocated) {
            return;
        }
        if (PrismSettings.debug) {
            System.out.println("PCS Texture allocating buffer: " + this + ", " + this.physicalWidth + "x" + this.physicalHeight);
        }
        this.allocateBuffer();
        this.allocated = true;
    }

    abstract void allocateBuffer();

    abstract Texture createSharedLockedTexture(Texture.WrapMode var1);
}

