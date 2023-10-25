/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.Mesh;
import com.sun.prism.MeshView;
import com.sun.prism.PhongMaterial;
import com.sun.prism.PixelFormat;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseResourceFactory;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.shape.BasicRoundRectRep;
import com.sun.prism.impl.shape.BasicShapeRep;
import com.sun.prism.shape.ShapeRep;
import com.sun.prism.sw.SWArgbPreTexture;
import com.sun.prism.sw.SWContext;
import com.sun.prism.sw.SWPresentable;
import com.sun.prism.sw.SWRTTexture;
import com.sun.prism.sw.SWTexture;
import com.sun.prism.sw.SWTexturePool;
import java.util.Map;
import java.util.WeakHashMap;

final class SWResourceFactory
extends BaseResourceFactory
implements ResourceFactory {
    private static final Map<Image, Texture> clampTexCache = new WeakHashMap<Image, Texture>();
    private static final Map<Image, Texture> repeatTexCache = new WeakHashMap<Image, Texture>();
    private static final Map<Image, Texture> mipmapTexCache = new WeakHashMap<Image, Texture>();
    private static final ShapeRep theRep = new BasicShapeRep();
    private static final ShapeRep rectRep = new BasicRoundRectRep();
    private Screen screen;
    private final SWContext context;

    public SWResourceFactory(Screen screen) {
        super(clampTexCache, repeatTexCache, mipmapTexCache);
        this.screen = screen;
        this.context = new SWContext(this);
    }

    @Override
    public TextureResourcePool getTextureResourcePool() {
        return SWTexturePool.instance;
    }

    public Screen getScreen() {
        return this.screen;
    }

    SWContext getContext() {
        return this.context;
    }

    @Override
    public void dispose() {
        this.context.dispose();
    }

    @Override
    public ShapeRep createArcRep() {
        return theRep;
    }

    @Override
    public ShapeRep createEllipseRep() {
        return theRep;
    }

    @Override
    public ShapeRep createRoundRectRep() {
        return rectRep;
    }

    @Override
    public ShapeRep createPathRep() {
        return theRep;
    }

    @Override
    public Presentable createPresentable(PresentableState presentableState) {
        if (PrismSettings.debug) {
            System.out.println("+ SWRF.createPresentable()");
        }
        return new SWPresentable(presentableState, this);
    }

    @Override
    public int getRTTWidth(int n, Texture.WrapMode wrapMode) {
        return n;
    }

    @Override
    public int getRTTHeight(int n, Texture.WrapMode wrapMode) {
        return n;
    }

    @Override
    public boolean isCompatibleTexture(Texture texture) {
        return texture instanceof SWTexture;
    }

    @Override
    public RTTexture createRTTexture(int n, int n2, Texture.WrapMode wrapMode, boolean bl) {
        return this.createRTTexture(n, n2, wrapMode);
    }

    @Override
    public RTTexture createRTTexture(int n, int n2, Texture.WrapMode wrapMode) {
        SWTexturePool sWTexturePool = SWTexturePool.instance;
        long l = sWTexturePool.estimateRTTextureSize(n, n2, false);
        if (!sWTexturePool.prepareForAllocation(l)) {
            return null;
        }
        return new SWRTTexture(this, n, n2);
    }

    @Override
    public int getMaximumTextureSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isFormatSupported(PixelFormat pixelFormat) {
        switch (pixelFormat) {
            case BYTE_RGB: 
            case BYTE_GRAY: 
            case INT_ARGB_PRE: 
            case BYTE_BGRA_PRE: {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean canClampToZero() {
        return false;
    }

    @Override
    public Texture createTexture(MediaFrame mediaFrame) {
        return new SWArgbPreTexture(this, Texture.WrapMode.CLAMP_TO_EDGE, mediaFrame.getWidth(), mediaFrame.getHeight());
    }

    @Override
    public Texture createTexture(PixelFormat pixelFormat, Texture.Usage usage, Texture.WrapMode wrapMode, int n, int n2) {
        SWTexturePool sWTexturePool = SWTexturePool.instance;
        long l = sWTexturePool.estimateTextureSize(n, n2, pixelFormat);
        if (!sWTexturePool.prepareForAllocation(l)) {
            return null;
        }
        return SWTexture.create(this, pixelFormat, wrapMode, n, n2);
    }

    @Override
    public Texture createTexture(PixelFormat pixelFormat, Texture.Usage usage, Texture.WrapMode wrapMode, int n, int n2, boolean bl) {
        return this.createTexture(pixelFormat, usage, wrapMode, n, n2);
    }

    @Override
    public PhongMaterial createPhongMaterial() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MeshView createMeshView(Mesh mesh) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mesh createMesh() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

