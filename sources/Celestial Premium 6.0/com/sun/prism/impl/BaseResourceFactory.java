/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class BaseResourceFactory
implements ResourceFactory {
    private final Map<Image, Texture> clampTexCache;
    private final Map<Image, Texture> repeatTexCache;
    private final Map<Image, Texture> mipmapTexCache;
    private final WeakHashMap<ResourceFactoryListener, Boolean> listenerMap = new WeakHashMap();
    private boolean disposed = false;
    private Texture regionTexture;
    private Texture glyphTexture;
    private boolean superShaderAllowed;

    public BaseResourceFactory() {
        this(new WeakHashMap<Image, Texture>(), new WeakHashMap<Image, Texture>(), new WeakHashMap<Image, Texture>());
    }

    public BaseResourceFactory(Map<Image, Texture> map, Map<Image, Texture> map2, Map<Image, Texture> map3) {
        this.clampTexCache = map;
        this.repeatTexCache = map2;
        this.mipmapTexCache = map3;
    }

    @Override
    public void addFactoryListener(ResourceFactoryListener resourceFactoryListener) {
        this.listenerMap.put(resourceFactoryListener, Boolean.TRUE);
    }

    @Override
    public void removeFactoryListener(ResourceFactoryListener resourceFactoryListener) {
        this.listenerMap.remove(resourceFactoryListener);
    }

    @Override
    public boolean isDeviceReady() {
        return !this.isDisposed();
    }

    protected void clearTextureCache() {
        this.clearTextureCache(this.clampTexCache);
        this.clearTextureCache(this.repeatTexCache);
        this.clearTextureCache(this.mipmapTexCache);
    }

    protected void clearTextureCache(Map<Image, Texture> map) {
        Collection<Texture> collection = map.values();
        for (Texture texture : collection) {
            texture.dispose();
        }
        map.clear();
    }

    protected ResourceFactoryListener[] getFactoryListeners() {
        return this.listenerMap.keySet().toArray(new ResourceFactoryListener[0]);
    }

    private void disposeResources() {
        this.clampTexCache.clear();
        this.repeatTexCache.clear();
        this.mipmapTexCache.clear();
        if (this.regionTexture != null) {
            this.regionTexture.dispose();
            this.regionTexture = null;
        }
        if (this.glyphTexture != null) {
            this.glyphTexture.dispose();
            this.glyphTexture = null;
        }
    }

    protected void notifyReset() {
        ResourceFactoryListener[] arrresourceFactoryListener;
        this.disposeResources();
        for (ResourceFactoryListener resourceFactoryListener : arrresourceFactoryListener = this.getFactoryListeners()) {
            if (null == resourceFactoryListener) continue;
            resourceFactoryListener.factoryReset();
        }
    }

    @Override
    public void dispose() {
        ResourceFactoryListener[] arrresourceFactoryListener;
        this.disposeResources();
        this.disposed = true;
        for (ResourceFactoryListener resourceFactoryListener : arrresourceFactoryListener = this.getFactoryListeners()) {
            if (null == resourceFactoryListener) continue;
            resourceFactoryListener.factoryReleased();
        }
    }

    static long sizeWithMipMap(int n, int n2, PixelFormat pixelFormat) {
        long l = 0L;
        int n3 = pixelFormat.getBytesPerPixelUnit();
        while (n > 1 && n2 > 1) {
            l += (long)n * (long)n2;
            n = n + 1 >> 1;
            n2 = n2 + 1 >> 1;
        }
        return ++l * (long)n3;
    }

    @Override
    public Texture getCachedTexture(Image image, Texture.WrapMode wrapMode) {
        if (this.checkDisposed()) {
            return null;
        }
        return this.getCachedTexture(image, wrapMode, false);
    }

    @Override
    public Texture getCachedTexture(Image image, Texture.WrapMode wrapMode, boolean bl) {
        Object object;
        Map<Image, Texture> map;
        if (this.checkDisposed()) {
            return null;
        }
        if (image == null) {
            throw new IllegalArgumentException("Image must be non-null");
        }
        if (wrapMode == Texture.WrapMode.CLAMP_TO_EDGE) {
            if (bl) {
                throw new IllegalArgumentException("Mipmap not supported with CLAMP mode: useMipmap = " + bl + ", wrapMode = " + wrapMode);
            }
            map = this.clampTexCache;
        } else if (wrapMode == Texture.WrapMode.REPEAT) {
            map = bl ? this.mipmapTexCache : this.repeatTexCache;
        } else {
            throw new IllegalArgumentException("no caching for " + wrapMode);
        }
        Texture texture = map.get(image);
        if (texture != null) {
            texture.lock();
            if (texture.isSurfaceLost()) {
                map.remove(image);
                texture = null;
            }
        }
        if (!bl && texture == null && (object = (wrapMode == Texture.WrapMode.REPEAT ? this.clampTexCache : this.repeatTexCache).get(image)) != null) {
            object.lock();
            if (!object.isSurfaceLost() && (texture = object.getSharedTexture(wrapMode)) != null) {
                texture.contentsUseful();
                map.put(image, texture);
            }
            object.unlock();
        }
        object = image.getSerial().getIdRect();
        if (texture == null) {
            long l;
            int n = image.getWidth();
            int n2 = image.getHeight();
            TextureResourcePool textureResourcePool = this.getTextureResourcePool();
            long l2 = l = bl ? BaseResourceFactory.sizeWithMipMap(n, n2, image.getPixelFormat()) : textureResourcePool.estimateTextureSize(n, n2, image.getPixelFormat());
            if (!textureResourcePool.prepareForAllocation(l)) {
                return null;
            }
            texture = this.createTexture(image, Texture.Usage.DEFAULT, wrapMode, bl);
            if (texture != null) {
                texture.setLastImageSerial((Integer)object.getKey());
                map.put(image, texture);
            }
        } else if (texture.getLastImageSerial() != ((Integer)object.getKey()).intValue()) {
            if ((Integer)object.getKey() - texture.getLastImageSerial() == 1 && object.getValue() != null) {
                Rectangle rectangle = (Rectangle)object.getValue();
                texture.update(image.getPixelBuffer(), image.getPixelFormat(), rectangle.x, rectangle.y, rectangle.x, rectangle.y, rectangle.width, rectangle.height, image.getScanlineStride(), false);
            } else {
                texture.update(image, 0, 0, image.getWidth(), image.getHeight(), false);
            }
            texture.setLastImageSerial((Integer)object.getKey());
        }
        return texture;
    }

    @Override
    public Texture createTexture(Image image, Texture.Usage usage, Texture.WrapMode wrapMode) {
        if (this.checkDisposed()) {
            return null;
        }
        return this.createTexture(image, usage, wrapMode, false);
    }

    @Override
    public Texture createTexture(Image image, Texture.Usage usage, Texture.WrapMode wrapMode, boolean bl) {
        int n;
        int n2;
        if (this.checkDisposed()) {
            return null;
        }
        PixelFormat pixelFormat = image.getPixelFormat();
        Texture texture = this.createTexture(pixelFormat, usage, wrapMode, n2 = image.getWidth(), n = image.getHeight(), bl);
        if (texture != null) {
            texture.update(image, 0, 0, n2, n, true);
            texture.contentsUseful();
        }
        return texture;
    }

    @Override
    public Texture createMaskTexture(int n, int n2, Texture.WrapMode wrapMode) {
        return this.createTexture(PixelFormat.BYTE_ALPHA, Texture.Usage.DEFAULT, wrapMode, n, n2);
    }

    @Override
    public Texture createFloatTexture(int n, int n2) {
        return this.createTexture(PixelFormat.FLOAT_XYZW, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_ZERO, n, n2);
    }

    @Override
    public void setRegionTexture(Texture texture) {
        if (this.checkDisposed()) {
            return;
        }
        this.regionTexture = texture;
        this.superShaderAllowed = PrismSettings.superShader && this.regionTexture != null && this.glyphTexture != null;
    }

    @Override
    public Texture getRegionTexture() {
        return this.regionTexture;
    }

    @Override
    public void setGlyphTexture(Texture texture) {
        if (this.checkDisposed()) {
            return;
        }
        this.glyphTexture = texture;
        this.superShaderAllowed = PrismSettings.superShader && this.regionTexture != null && this.glyphTexture != null;
    }

    @Override
    public Texture getGlyphTexture() {
        return this.glyphTexture;
    }

    @Override
    public boolean isSuperShaderAllowed() {
        return this.superShaderAllowed;
    }

    protected boolean canClampToZero() {
        return true;
    }

    protected boolean canClampToEdge() {
        return true;
    }

    protected boolean canRepeat() {
        return true;
    }

    @Override
    public boolean isWrapModeSupported(Texture.WrapMode wrapMode) {
        switch (wrapMode) {
            case CLAMP_NOT_NEEDED: {
                return true;
            }
            case CLAMP_TO_EDGE: {
                return this.canClampToEdge();
            }
            case REPEAT: {
                return this.canRepeat();
            }
            case CLAMP_TO_ZERO: {
                return this.canClampToZero();
            }
            case CLAMP_TO_EDGE_SIMULATED: 
            case CLAMP_TO_ZERO_SIMULATED: 
            case REPEAT_SIMULATED: {
                throw new InternalError("Cannot test support for simulated wrap modes");
            }
        }
        throw new InternalError("Unrecognized wrap mode: " + wrapMode);
    }

    @Override
    public boolean isDisposed() {
        return this.disposed;
    }

    protected boolean checkDisposed() {
        if (PrismSettings.verbose && this.isDisposed()) {
            try {
                throw new IllegalStateException("attempt to use resource after factory is disposed");
            }
            catch (RuntimeException runtimeException) {
                runtimeException.printStackTrace();
            }
        }
        return this.isDisposed();
    }
}

