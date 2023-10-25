/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d;

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
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseResourceFactory;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.shape.BasicShapeRep;
import com.sun.prism.j2d.J2DPresentable;
import com.sun.prism.j2d.J2DPrismGraphics;
import com.sun.prism.j2d.J2DRTTexture;
import com.sun.prism.j2d.J2DTexture;
import com.sun.prism.j2d.J2DTexturePool;
import com.sun.prism.shape.ShapeRep;
import java.awt.Graphics2D;
import java.util.Map;
import java.util.WeakHashMap;

class J2DResourceFactory
extends BaseResourceFactory {
    private static final Map<Image, Texture> clampTexCache = new WeakHashMap<Image, Texture>();
    private static final Map<Image, Texture> repeatTexCache = new WeakHashMap<Image, Texture>();
    private static final Map<Image, Texture> mipmapTexCache = new WeakHashMap<Image, Texture>();
    private Screen screen;
    private static ShapeRep theRep = new BasicShapeRep();

    J2DResourceFactory(Screen screen) {
        super(clampTexCache, repeatTexCache, mipmapTexCache);
        this.screen = screen;
    }

    J2DPrismGraphics createJ2DPrismGraphics(J2DPresentable j2DPresentable, Graphics2D graphics2D) {
        return new J2DPrismGraphics(j2DPresentable, graphics2D);
    }

    @Override
    public TextureResourcePool getTextureResourcePool() {
        return J2DTexturePool.instance;
    }

    Screen getScreen() {
        return this.screen;
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
        return theRep;
    }

    @Override
    public ShapeRep createPathRep() {
        return theRep;
    }

    @Override
    public Presentable createPresentable(PresentableState presentableState) {
        return J2DPresentable.create(presentableState, this);
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
    public RTTexture createRTTexture(int n, int n2, Texture.WrapMode wrapMode, boolean bl) {
        return this.createRTTexture(n, n2, wrapMode);
    }

    @Override
    public RTTexture createRTTexture(int n, int n2, Texture.WrapMode wrapMode) {
        J2DTexturePool j2DTexturePool = J2DTexturePool.instance;
        long l = j2DTexturePool.estimateRTTextureSize(n, n2, false);
        if (!j2DTexturePool.prepareForAllocation(l)) {
            return null;
        }
        return new J2DRTTexture(n, n2, this);
    }

    @Override
    public Texture createTexture(PixelFormat pixelFormat, Texture.Usage usage, Texture.WrapMode wrapMode, int n, int n2) {
        return J2DTexture.create(pixelFormat, wrapMode, n, n2);
    }

    @Override
    public Texture createTexture(PixelFormat pixelFormat, Texture.Usage usage, Texture.WrapMode wrapMode, int n, int n2, boolean bl) {
        return this.createTexture(pixelFormat, usage, wrapMode, n, n2);
    }

    @Override
    public Texture createTexture(MediaFrame mediaFrame) {
        mediaFrame.holdFrame();
        if (mediaFrame.getPixelFormat() != PixelFormat.INT_ARGB_PRE) {
            MediaFrame mediaFrame2 = mediaFrame.convertToFormat(PixelFormat.INT_ARGB_PRE);
            mediaFrame.releaseFrame();
            mediaFrame = mediaFrame2;
            if (null == mediaFrame) {
                return null;
            }
        }
        J2DTexture j2DTexture = J2DTexture.create(mediaFrame.getPixelFormat(), Texture.WrapMode.CLAMP_TO_EDGE, mediaFrame.getWidth(), mediaFrame.getHeight());
        mediaFrame.releaseFrame();
        return j2DTexture;
    }

    @Override
    public boolean isCompatibleTexture(Texture texture) {
        return texture instanceof J2DTexture;
    }

    @Override
    protected boolean canClampToZero() {
        return false;
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
    public void dispose() {
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

