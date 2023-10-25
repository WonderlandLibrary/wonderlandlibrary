/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public final class MultiTexture
implements Texture {
    private int width;
    private int height;
    private PixelFormat format;
    private Texture.WrapMode wrapMode;
    private boolean linearFiltering = true;
    private final ArrayList<Texture> textures;
    private int lastImageSerial;

    public MultiTexture(PixelFormat pixelFormat, Texture.WrapMode wrapMode, int n, int n2) {
        this.width = n;
        this.height = n2;
        this.format = pixelFormat;
        this.wrapMode = wrapMode;
        this.textures = new ArrayList(4);
    }

    private MultiTexture(MultiTexture multiTexture, Texture.WrapMode wrapMode) {
        this(multiTexture.format, wrapMode, multiTexture.width, multiTexture.height);
        for (int i = 0; i < multiTexture.textureCount(); ++i) {
            Texture texture = multiTexture.getTexture(i);
            this.setTexture(texture.getSharedTexture(wrapMode), i);
        }
        this.linearFiltering = multiTexture.linearFiltering;
        this.lastImageSerial = multiTexture.lastImageSerial;
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
        MultiTexture multiTexture = new MultiTexture(this, wrapMode);
        multiTexture.lock();
        return multiTexture;
    }

    public int textureCount() {
        return this.textures.size();
    }

    public void setTexture(Texture texture, int n) {
        if (!texture.getWrapMode().isCompatibleWith(this.wrapMode)) {
            throw new IllegalArgumentException("texture wrap mode must match multi-texture mode");
        }
        if (this.textures.size() < n + 1) {
            for (int i = this.textures.size(); i < n; ++i) {
                this.textures.add(null);
            }
            this.textures.add(texture);
        } else {
            this.textures.set(n, texture);
        }
        texture.setLinearFiltering(this.linearFiltering);
    }

    public Texture getTexture(int n) {
        return this.textures.get(n);
    }

    public Texture[] getTextures() {
        return this.textures.toArray(new Texture[this.textures.size()]);
    }

    public void removeTexture(Texture texture) {
        this.textures.remove(texture);
    }

    public void removeTexture(int n) {
        this.textures.remove(n);
    }

    @Override
    public PixelFormat getPixelFormat() {
        return this.format;
    }

    @Override
    public int getPhysicalWidth() {
        return this.width;
    }

    @Override
    public int getPhysicalHeight() {
        return this.height;
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
        return this.width;
    }

    @Override
    public int getContentHeight() {
        return this.height;
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
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override
    public void update(Image image, int n, int n2) {
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override
    public void update(Image image, int n, int n2, int n3, int n4) {
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override
    public void update(Image image, int n, int n2, int n3, int n4, boolean bl) {
        throw new UnsupportedOperationException("Update from Image not supported");
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        throw new UnsupportedOperationException("Update from generic Buffer not supported");
    }

    @Override
    public void update(MediaFrame mediaFrame, boolean bl) {
        if (mediaFrame.getPixelFormat() == PixelFormat.MULTI_YCbCr_420) {
            int n = mediaFrame.getEncodedWidth();
            int n2 = mediaFrame.getEncodedHeight();
            for (int i = 0; i < mediaFrame.planeCount(); ++i) {
                Texture texture = this.textures.get(i);
                if (null == texture) continue;
                int n3 = n;
                int n4 = n2;
                if (i == 2 || i == 1) {
                    n3 /= 2;
                    n4 /= 2;
                }
                ByteBuffer byteBuffer = mediaFrame.getBufferForPlane(i);
                texture.update(byteBuffer, PixelFormat.BYTE_ALPHA, 0, 0, 0, 0, n3, n4, mediaFrame.strideForPlane(i), bl);
            }
        } else {
            throw new IllegalArgumentException("Invalid pixel format in MediaFrame");
        }
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
    public boolean getLinearFiltering() {
        return this.linearFiltering;
    }

    @Override
    public void setLinearFiltering(boolean bl) {
        this.linearFiltering = bl;
        for (Texture texture : this.textures) {
            texture.setLinearFiltering(bl);
        }
    }

    @Override
    public void lock() {
        for (Texture texture : this.textures) {
            texture.lock();
        }
    }

    @Override
    public void unlock() {
        for (Texture texture : this.textures) {
            texture.unlock();
        }
    }

    @Override
    public boolean isLocked() {
        for (Texture texture : this.textures) {
            if (!texture.isLocked()) continue;
            return true;
        }
        return false;
    }

    @Override
    public int getLockCount() {
        int n = 0;
        for (Texture texture : this.textures) {
            n = Math.max(n, texture.getLockCount());
        }
        return n;
    }

    @Override
    public void assertLocked() {
        for (Texture texture : this.textures) {
            texture.assertLocked();
        }
    }

    @Override
    public void makePermanent() {
        for (Texture texture : this.textures) {
            texture.makePermanent();
        }
    }

    @Override
    public void contentsUseful() {
        for (Texture texture : this.textures) {
            texture.contentsUseful();
        }
    }

    @Override
    public void contentsNotUseful() {
        for (Texture texture : this.textures) {
            texture.contentsNotUseful();
        }
    }

    @Override
    public boolean isSurfaceLost() {
        for (Texture texture : this.textures) {
            if (!texture.isSurfaceLost()) continue;
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        for (Texture texture : this.textures) {
            texture.dispose();
        }
        this.textures.clear();
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
    public void setContentWidth(int n) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setContentHeight(int n) {
        throw new UnsupportedOperationException("Not supported.");
    }
}

