/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PlatformLogger
 */
package com.sun.prism.d3d;

import com.sun.javafx.logging.PlatformLogger;
import com.sun.prism.Image;
import com.sun.prism.PhongMaterial;
import com.sun.prism.Texture;
import com.sun.prism.TextureMap;
import com.sun.prism.d3d.D3DContext;
import com.sun.prism.d3d.D3DTexture;
import com.sun.prism.impl.BasePhongMaterial;
import com.sun.prism.impl.Disposer;

class D3DPhongMaterial
extends BasePhongMaterial
implements PhongMaterial {
    static int count = 0;
    private final D3DContext context;
    private final long nativeHandle;
    private TextureMap[] maps = new TextureMap[MAX_MAP_TYPE];

    private D3DPhongMaterial(D3DContext d3DContext, long l, Disposer.Record record) {
        super(record);
        this.context = d3DContext;
        this.nativeHandle = l;
        ++count;
    }

    static D3DPhongMaterial create(D3DContext d3DContext) {
        long l = d3DContext.createD3DPhongMaterial();
        return new D3DPhongMaterial(d3DContext, l, new D3DPhongMaterialDisposerRecord(d3DContext, l));
    }

    long getNativeHandle() {
        return this.nativeHandle;
    }

    @Override
    public void setDiffuseColor(float f, float f2, float f3, float f4) {
        this.context.setDiffuseColor(this.nativeHandle, f, f2, f3, f4);
    }

    @Override
    public void setSpecularColor(boolean bl, float f, float f2, float f3, float f4) {
        this.context.setSpecularColor(this.nativeHandle, bl, f, f2, f3, f4);
    }

    @Override
    public void setTextureMap(TextureMap textureMap) {
        this.maps[textureMap.getType().ordinal()] = textureMap;
    }

    private Texture setupTexture(TextureMap textureMap, boolean bl) {
        Image image = textureMap.getImage();
        Texture texture = image == null ? null : this.context.getResourceFactory().getCachedTexture(image, Texture.WrapMode.REPEAT, bl);
        long l = texture != null ? ((D3DTexture)texture).getNativeTextureObject() : 0L;
        this.context.setMap(this.nativeHandle, textureMap.getType().ordinal(), l);
        return texture;
    }

    @Override
    public void lockTextureMaps() {
        for (int i = 0; i < MAX_MAP_TYPE; ++i) {
            Texture texture = this.maps[i].getTexture();
            if (!this.maps[i].isDirty() && texture != null) {
                texture.lock();
                if (!texture.isSurfaceLost()) continue;
            }
            boolean bl = i == PhongMaterial.DIFFUSE || i == PhongMaterial.SELF_ILLUM;
            texture = this.setupTexture(this.maps[i], bl);
            this.maps[i].setTexture(texture);
            this.maps[i].setDirty(false);
            if (this.maps[i].getImage() == null || texture != null) continue;
            String string = PhongMaterial.class.getName();
            PlatformLogger.getLogger((String)string).warning("Warning: Low on texture resources. Cannot create texture.");
        }
    }

    @Override
    public void unlockTextureMaps() {
        for (int i = 0; i < MAX_MAP_TYPE; ++i) {
            Texture texture = this.maps[i].getTexture();
            if (texture == null) continue;
            texture.unlock();
        }
    }

    @Override
    public boolean isValid() {
        return !this.context.isDisposed();
    }

    @Override
    public void dispose() {
        this.disposerRecord.dispose();
        --count;
    }

    public int getCount() {
        return count;
    }

    static class D3DPhongMaterialDisposerRecord
    implements Disposer.Record {
        private final D3DContext context;
        private long nativeHandle;

        D3DPhongMaterialDisposerRecord(D3DContext d3DContext, long l) {
            this.context = d3DContext;
            this.nativeHandle = l;
        }

        void traceDispose() {
        }

        @Override
        public void dispose() {
            if (this.nativeHandle != 0L) {
                this.traceDispose();
                this.context.releaseD3DPhongMaterial(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}

