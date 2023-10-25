/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseGraphics;
import com.sun.prism.impl.GlyphCache;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.paint.PaintUtil;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.paint.Gradient;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseContext {
    private final Screen screen;
    private final ResourceFactory factory;
    private final VertexBuffer vertexBuffer;
    private boolean disposed = false;
    private static final int MIN_MASK_DIM = 1024;
    private Texture maskTex;
    private ByteBuffer maskBuffer;
    private ByteBuffer clearBuffer;
    private int curMaskRow;
    private int nextMaskRow;
    private int curMaskCol;
    private int highMaskCol;
    private Texture paintTex;
    private int[] paintPixels;
    private ByteBuffer paintBuffer;
    private Texture rectTex;
    private int rectTexMax;
    private Texture wrapRectTex;
    private Texture ovalTex;
    private final GeneralTransform3D perspectiveTransform = new GeneralTransform3D();
    private final Map<FontStrike, GlyphCache> greyGlyphCaches = new HashMap<FontStrike, GlyphCache>();
    private final Map<FontStrike, GlyphCache> lcdGlyphCaches = new HashMap<FontStrike, GlyphCache>();

    protected BaseContext(Screen screen, ResourceFactory resourceFactory, int n) {
        this.screen = screen;
        this.factory = resourceFactory;
        this.vertexBuffer = new VertexBuffer(this, n);
    }

    protected void setDeviceParametersFor2D() {
    }

    protected void setDeviceParametersFor3D() {
    }

    public Screen getAssociatedScreen() {
        return this.screen;
    }

    public ResourceFactory getResourceFactory() {
        return this.factory;
    }

    public VertexBuffer getVertexBuffer() {
        return this.vertexBuffer;
    }

    public void flushVertexBuffer() {
        if (this.checkDisposed()) {
            return;
        }
        this.vertexBuffer.flush();
    }

    protected final void flushMask() {
        if (this.curMaskRow > 0 || this.curMaskCol > 0) {
            this.maskTex.lock();
            this.maskTex.update(this.maskBuffer, this.maskTex.getPixelFormat(), 0, 0, 0, 0, this.highMaskCol, this.nextMaskRow, this.maskTex.getContentWidth(), true);
            this.maskTex.unlock();
            this.highMaskCol = 0;
            this.nextMaskRow = 0;
            this.curMaskCol = 0;
            this.curMaskRow = 0;
        }
    }

    public void drawQuads(float[] arrf, byte[] arrby, int n) {
        this.flushMask();
        this.renderQuads(arrf, arrby, n);
    }

    protected GeneralTransform3D getPerspectiveTransformNoClone() {
        return this.perspectiveTransform;
    }

    protected void setPerspectiveTransform(GeneralTransform3D generalTransform3D) {
        if (generalTransform3D == null) {
            this.perspectiveTransform.setIdentity();
        } else {
            this.perspectiveTransform.set(generalTransform3D);
        }
    }

    protected abstract void renderQuads(float[] var1, byte[] var2, int var3);

    public void setRenderTarget(BaseGraphics baseGraphics) {
        if (baseGraphics != null) {
            this.setRenderTarget(baseGraphics.getRenderTarget(), baseGraphics.getCameraNoClone(), baseGraphics.isDepthTest() && baseGraphics.isDepthBuffer(), baseGraphics.isState3D());
        } else {
            this.releaseRenderTarget();
        }
    }

    protected void releaseRenderTarget() {
    }

    protected abstract void setRenderTarget(RenderTarget var1, NGCamera var2, boolean var3, boolean var4);

    public abstract void validateClearOp(BaseGraphics var1);

    public abstract void validatePaintOp(BaseGraphics var1, BaseTransform var2, Texture var3, float var4, float var5, float var6, float var7);

    public abstract void validateTextureOp(BaseGraphics var1, BaseTransform var2, Texture var3, PixelFormat var4);

    public void clearGlyphCaches() {
        this.clearCaches(this.greyGlyphCaches);
        this.clearCaches(this.lcdGlyphCaches);
    }

    private void clearCaches(Map<FontStrike, GlyphCache> map) {
        Iterator<Object> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().clearDesc();
        }
        for (GlyphCache glyphCache : map.values()) {
            if (glyphCache == null) continue;
            glyphCache.clear();
        }
        map.clear();
    }

    public abstract RTTexture getLCDBuffer();

    public GlyphCache getGlyphCache(FontStrike fontStrike) {
        Map<FontStrike, GlyphCache> map = fontStrike.getAAMode() == 1 ? this.lcdGlyphCaches : this.greyGlyphCaches;
        return this.getGlyphCache(fontStrike, map);
    }

    public boolean isSuperShaderEnabled() {
        return false;
    }

    private GlyphCache getGlyphCache(FontStrike fontStrike, Map<FontStrike, GlyphCache> map) {
        if (this.checkDisposed()) {
            return null;
        }
        GlyphCache glyphCache = map.get(fontStrike);
        if (glyphCache == null) {
            glyphCache = new GlyphCache(this, fontStrike);
            map.put(fontStrike, glyphCache);
        }
        return glyphCache;
    }

    public Texture validateMaskTexture(MaskData maskData, boolean bl) {
        if (this.checkDisposed()) {
            return null;
        }
        int n = bl ? 1 : 0;
        int n2 = maskData.getWidth() + n + n;
        int n3 = maskData.getHeight() + n + n;
        int n4 = 0;
        int n5 = 0;
        if (this.maskTex != null) {
            this.maskTex.lock();
            if (this.maskTex.isSurfaceLost()) {
                this.maskTex = null;
            } else {
                n4 = this.maskTex.getContentWidth();
                n5 = this.maskTex.getContentHeight();
            }
        }
        if (this.maskTex == null || n4 < n2 || n5 < n3) {
            if (this.maskTex != null) {
                this.flushVertexBuffer();
                this.maskTex.dispose();
                this.maskTex = null;
            }
            this.maskBuffer = null;
            int n6 = Math.max(1024, Math.max(n2, n4));
            int n7 = Math.max(1024, Math.max(n3, n5));
            this.maskTex = this.getResourceFactory().createMaskTexture(n6, n7, Texture.WrapMode.CLAMP_NOT_NEEDED);
            this.maskBuffer = ByteBuffer.allocate(n6 * n7);
            if (this.clearBuffer == null || this.clearBuffer.capacity() < n6) {
                this.clearBuffer = null;
                this.clearBuffer = ByteBuffer.allocate(n6);
            }
            this.highMaskCol = 0;
            this.nextMaskRow = 0;
            this.curMaskCol = 0;
            this.curMaskRow = 0;
        }
        return this.maskTex;
    }

    public void updateMaskTexture(MaskData maskData, RectBounds rectBounds, boolean bl) {
        if (this.checkDisposed()) {
            return;
        }
        this.maskTex.assertLocked();
        int n = maskData.getWidth();
        int n2 = maskData.getHeight();
        int n3 = this.maskTex.getContentWidth();
        int n4 = this.maskTex.getContentHeight();
        int n5 = bl ? 1 : 0;
        int n6 = n + n5 + n5;
        int n7 = n2 + n5 + n5;
        if (this.curMaskCol + n6 > n3) {
            this.curMaskCol = 0;
            this.curMaskRow = this.nextMaskRow;
        }
        if (this.curMaskRow + n7 > n4) {
            this.flushVertexBuffer();
        }
        int n8 = this.curMaskRow * n3 + this.curMaskCol;
        ByteToBytePixelConverter byteToBytePixelConverter = ByteGray.ToByteGrayConverter();
        if (bl) {
            int n9 = n8;
            byteToBytePixelConverter.convert(this.clearBuffer, 0, 0, this.maskBuffer, n9, n3, n + 1, 1);
            n9 = n8 + n + 1;
            byteToBytePixelConverter.convert(this.clearBuffer, 0, 0, this.maskBuffer, n9, n3, 1, n2 + 1);
            n9 = n8 + n3;
            byteToBytePixelConverter.convert(this.clearBuffer, 0, 0, this.maskBuffer, n9, n3, 1, n2 + 1);
            n9 = n8 + (n2 + 1) * n3 + 1;
            byteToBytePixelConverter.convert(this.clearBuffer, 0, 0, this.maskBuffer, n9, n3, n + 1, 1);
            n8 += n3 + 1;
        }
        byteToBytePixelConverter.convert(maskData.getMaskBuffer(), 0, n, this.maskBuffer, n8, n3, n, n2);
        float f = this.maskTex.getPhysicalWidth();
        float f2 = this.maskTex.getPhysicalHeight();
        rectBounds.setMinX((float)(this.curMaskCol + n5) / f);
        rectBounds.setMinY((float)(this.curMaskRow + n5) / f2);
        rectBounds.setMaxX((float)(this.curMaskCol + n5 + n) / f);
        rectBounds.setMaxY((float)(this.curMaskRow + n5 + n2) / f2);
        this.curMaskCol += n6;
        if (this.highMaskCol < this.curMaskCol) {
            this.highMaskCol = this.curMaskCol;
        }
        if (this.nextMaskRow < this.curMaskRow + n7) {
            this.nextMaskRow = this.curMaskRow + n7;
        }
    }

    public int getRectTextureMaxSize() {
        if (this.checkDisposed()) {
            return 0;
        }
        if (this.rectTex == null) {
            this.createRectTexture();
        }
        return this.rectTexMax;
    }

    public Texture getRectTexture() {
        if (this.checkDisposed()) {
            return null;
        }
        if (this.rectTex == null) {
            this.createRectTexture();
        }
        this.rectTex.lock();
        return this.rectTex;
    }

    private void createRectTexture() {
        int n;
        if (this.checkDisposed()) {
            return;
        }
        int n2 = PrismSettings.primTextureSize;
        if (n2 < 0) {
            n2 = this.getResourceFactory().getMaximumTextureSize();
        }
        int n3 = 3;
        int n4 = 2;
        while (n3 + n4 + 1 <= n2) {
            this.rectTexMax = n4++;
            n3 += n4;
        }
        byte[] arrby = new byte[n3 * n3];
        int n5 = 1;
        for (int i = 1; i <= this.rectTexMax; ++i) {
            int n6 = 1;
            for (n = 1; n <= this.rectTexMax; ++n) {
                int n7 = n5 * n3 + n6;
                for (int j = 0; j < i; ++j) {
                    for (int k = 0; k < n; ++k) {
                        arrby[n7 + k] = -1;
                    }
                    n7 += n3;
                }
                n6 += n + 1;
            }
            n5 += i + 1;
        }
        if (PrismSettings.verbose) {
            System.out.println("max rectangle texture cell size = " + this.rectTexMax);
        }
        Texture texture = this.getResourceFactory().createMaskTexture(n3, n3, Texture.WrapMode.CLAMP_NOT_NEEDED);
        texture.contentsUseful();
        texture.makePermanent();
        PixelFormat pixelFormat = texture.getPixelFormat();
        n = n3 * pixelFormat.getBytesPerPixelUnit();
        texture.update(ByteBuffer.wrap(arrby), pixelFormat, 0, 0, 0, 0, n3, n3, n, false);
        this.rectTex = texture;
    }

    public Texture getWrapRectTexture() {
        if (this.checkDisposed()) {
            return null;
        }
        if (this.wrapRectTex == null) {
            int n;
            Texture texture = this.getResourceFactory().createMaskTexture(2, 2, Texture.WrapMode.CLAMP_TO_EDGE);
            texture.contentsUseful();
            texture.makePermanent();
            int n2 = texture.getPhysicalWidth();
            int n3 = texture.getPhysicalHeight();
            if (PrismSettings.verbose) {
                System.out.println("wrap rectangle texture = " + n2 + " x " + n3);
            }
            byte[] arrby = new byte[n2 * n3];
            int n4 = n2;
            for (int i = 1; i < n3; ++i) {
                for (n = 1; n < n3; ++n) {
                    arrby[n4 + n] = -1;
                }
                n4 += n2;
            }
            PixelFormat pixelFormat = texture.getPixelFormat();
            n = n2 * pixelFormat.getBytesPerPixelUnit();
            texture.update(ByteBuffer.wrap(arrby), pixelFormat, 0, 0, 0, 0, n2, n3, n, false);
            this.wrapRectTex = texture;
        }
        this.wrapRectTex.lock();
        return this.wrapRectTex;
    }

    public Texture getOvalTexture() {
        if (this.checkDisposed()) {
            return null;
        }
        if (this.ovalTex == null) {
            int n;
            int n2 = this.getRectTextureMaxSize();
            int n3 = n2 * (n2 + 1) / 2;
            byte[] arrby = new byte[(n3 += n2 + 1) * n3];
            int n4 = 1;
            for (int i = 1; i <= n2; ++i) {
                int n5 = 1;
                for (n = 1; n <= n2; ++n) {
                    int n6 = n4 * n3 + n5;
                    for (int j = 0; j < i; ++j) {
                        int n7;
                        int n8;
                        if (j * 2 >= i) {
                            int n9 = i - 1 - j;
                            n8 = n6 + (n9 - j) * n3;
                            for (n7 = 0; n7 < n; ++n7) {
                                arrby[n6 + n7] = arrby[n8 + n7];
                            }
                        } else {
                            float f = (float)j + 0.0625f;
                            for (n8 = 0; n8 < 8; ++n8) {
                                float f2 = f / (float)i - 0.5f;
                                f2 = (float)Math.sqrt(0.25f - f2 * f2);
                                int n10 = Math.round((float)n * 4.0f * (1.0f - f2 * 2.0f));
                                int n11 = n10 >> 3;
                                int n12 = n10 & 7;
                                int n13 = n6 + n11;
                                arrby[n13] = (byte)(arrby[n13] + (8 - n12));
                                int n14 = n6 + n11 + 1;
                                arrby[n14] = (byte)(arrby[n14] + n12);
                                f += 0.125f;
                            }
                            n8 = 0;
                            for (n7 = 0; n7 < n; ++n7) {
                                arrby[n6 + n7] = n7 * 2 >= n ? arrby[n6 + n - 1 - n7] : (byte)(((n8 += arrby[n6 + n7]) * 255 + 32) / 64);
                            }
                            arrby[n6 + n] = 0;
                        }
                        n6 += n3;
                    }
                    n5 += n + 1;
                }
                n4 += i + 1;
            }
            Texture texture = this.getResourceFactory().createMaskTexture(n3, n3, Texture.WrapMode.CLAMP_NOT_NEEDED);
            texture.contentsUseful();
            texture.makePermanent();
            PixelFormat pixelFormat = texture.getPixelFormat();
            n = n3 * pixelFormat.getBytesPerPixelUnit();
            texture.update(ByteBuffer.wrap(arrby), pixelFormat, 0, 0, 0, 0, n3, n3, n, false);
            this.ovalTex = texture;
        }
        this.ovalTex.lock();
        return this.ovalTex;
    }

    public Texture getGradientTexture(Gradient gradient, BaseTransform baseTransform, int n, int n2, MaskData maskData, float f, float f2, float f3, float f4) {
        int n3;
        if (this.checkDisposed()) {
            return null;
        }
        int n4 = n * n2;
        int n5 = n4 * 4;
        if (this.paintBuffer == null || this.paintBuffer.capacity() < n5) {
            this.paintPixels = new int[n4];
            this.paintBuffer = ByteBuffer.wrap(new byte[n5]);
        }
        if (this.paintTex != null) {
            this.paintTex.lock();
            if (this.paintTex.isSurfaceLost()) {
                this.paintTex = null;
            }
        }
        if (this.paintTex == null || this.paintTex.getContentWidth() < n || this.paintTex.getContentHeight() < n2) {
            int n6 = n;
            n3 = n2;
            if (this.paintTex != null) {
                n6 = Math.max(n, this.paintTex.getContentWidth());
                n3 = Math.max(n2, this.paintTex.getContentHeight());
                this.paintTex.dispose();
            }
            this.paintTex = this.getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_NOT_NEEDED, n6, n3);
        }
        PaintUtil.fillImageWithGradient(this.paintPixels, gradient, baseTransform, 0, 0, n, n2, f, f2, f3, f4);
        byte[] arrby = this.paintBuffer.array();
        if (maskData != null) {
            byte[] arrby2 = maskData.getMaskBuffer().array();
            int n7 = 0;
            for (int i = 0; i < n4; ++i) {
                int n8 = this.paintPixels[i];
                int n9 = arrby2[i] & 0xFF;
                arrby[n7++] = (byte)((n8 & 0xFF) * n9 / 255);
                arrby[n7++] = (byte)((n8 >> 8 & 0xFF) * n9 / 255);
                arrby[n7++] = (byte)((n8 >> 16 & 0xFF) * n9 / 255);
                arrby[n7++] = (byte)((n8 >>> 24) * n9 / 255);
            }
        } else {
            n3 = 0;
            for (int i = 0; i < n4; ++i) {
                int n10 = this.paintPixels[i];
                arrby[n3++] = (byte)(n10 & 0xFF);
                arrby[n3++] = (byte)(n10 >> 8 & 0xFF);
                arrby[n3++] = (byte)(n10 >> 16 & 0xFF);
                arrby[n3++] = (byte)(n10 >>> 24);
            }
        }
        this.paintTex.update(this.paintBuffer, PixelFormat.BYTE_BGRA_PRE, 0, 0, 0, 0, n, n2, n * 4, false);
        return this.paintTex;
    }

    public void dispose() {
        this.clearGlyphCaches();
        GlyphCache.disposeForContext(this);
        if (this.maskTex != null) {
            this.maskTex.dispose();
            this.maskTex = null;
        }
        if (this.paintTex != null) {
            this.paintTex.dispose();
            this.paintTex = null;
        }
        if (this.rectTex != null) {
            this.rectTex.dispose();
            this.rectTex = null;
        }
        if (this.wrapRectTex != null) {
            this.wrapRectTex.dispose();
            this.wrapRectTex = null;
        }
        if (this.ovalTex != null) {
            this.ovalTex.dispose();
            this.ovalTex = null;
        }
        this.disposed = true;
    }

    public final boolean isDisposed() {
        return this.disposed;
    }

    protected boolean checkDisposed() {
        if (PrismSettings.verbose && this.isDisposed()) {
            try {
                throw new IllegalStateException("attempt to use resource after context is disposed");
            }
            catch (RuntimeException runtimeException) {
                runtimeException.printStackTrace();
            }
        }
        return this.isDisposed();
    }
}

