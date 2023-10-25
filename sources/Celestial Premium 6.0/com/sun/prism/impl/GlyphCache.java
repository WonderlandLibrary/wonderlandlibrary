/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PulseLogger
 */
package com.sun.prism.impl;

import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseContext;
import com.sun.prism.impl.BufferUtil;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.packrect.RectanglePacker;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.paint.Color;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.WeakHashMap;

public class GlyphCache {
    private static final int WIDTH = PrismSettings.glyphCacheWidth;
    private static final int HEIGHT = PrismSettings.glyphCacheHeight;
    private static ByteBuffer emptyMask;
    private final BaseContext context;
    private final FontStrike strike;
    private static final int SEGSHIFT = 5;
    private static final int SEGSIZE = 32;
    private static final int SEGMASK = 31;
    HashMap<Integer, GlyphData[]> glyphDataMap = new HashMap();
    private static final int SUBPIXEL_SHIFT = 27;
    private RectanglePacker packer;
    private boolean isLCDCache;
    static WeakHashMap<BaseContext, RectanglePacker> greyPackerMap;
    static WeakHashMap<BaseContext, RectanglePacker> lcdPackerMap;

    public GlyphCache(BaseContext baseContext, FontStrike fontStrike) {
        this.context = baseContext;
        this.strike = fontStrike;
        this.isLCDCache = fontStrike.getAAMode() == 1;
        WeakHashMap<BaseContext, RectanglePacker> weakHashMap = this.isLCDCache ? lcdPackerMap : greyPackerMap;
        this.packer = weakHashMap.get(baseContext);
        if (this.packer == null) {
            ResourceFactory resourceFactory = baseContext.getResourceFactory();
            Texture texture = resourceFactory.createMaskTexture(WIDTH, HEIGHT, Texture.WrapMode.CLAMP_NOT_NEEDED);
            texture.contentsUseful();
            texture.makePermanent();
            if (!this.isLCDCache) {
                resourceFactory.setGlyphTexture(texture);
            }
            texture.setLinearFiltering(false);
            this.packer = new RectanglePacker(texture, WIDTH, HEIGHT);
            weakHashMap.put(baseContext, this.packer);
        }
    }

    public void render(BaseContext baseContext, GlyphList glyphList, float f, float f2, int n, int n2, Color color, Color color2, BaseTransform baseTransform, BaseBounds baseBounds) {
        int n3;
        int n4;
        if (this.isLCDCache) {
            n4 = baseContext.getLCDBuffer().getPhysicalWidth();
            n3 = baseContext.getLCDBuffer().getPhysicalHeight();
        } else {
            n4 = 1;
            n3 = 1;
        }
        Texture texture = this.getBackingStore();
        VertexBuffer vertexBuffer = baseContext.getVertexBuffer();
        int n5 = glyphList.getGlyphCount();
        Color color3 = null;
        Point2D point2D = new Point2D();
        for (int i = 0; i < n5; ++i) {
            int n6 = glyphList.getGlyphCode(i);
            if ((n6 & 0xFFFFFF) == 65535) continue;
            point2D.setLocation(f + glyphList.getPosX(i), f2 + glyphList.getPosY(i));
            baseTransform.transform(point2D, point2D);
            int n7 = this.strike.getQuantizedPosition(point2D);
            GlyphData glyphData = this.getCachedGlyph(n6, n7);
            if (glyphData == null) continue;
            if (baseBounds != null) {
                if (f + glyphList.getPosX(i) > baseBounds.getMaxX()) break;
                if (f + glyphList.getPosX(i + 1) < baseBounds.getMinX()) continue;
            }
            if (color != null && color2 != null) {
                int n8 = glyphList.getCharOffset(i);
                if (n <= n8 && n8 < n2) {
                    if (color != color3) {
                        vertexBuffer.setPerVertexColor(color, 1.0f);
                        color3 = color;
                    }
                } else if (color2 != color3) {
                    vertexBuffer.setPerVertexColor(color2, 1.0f);
                    color3 = color2;
                }
            }
            this.addDataToQuad(glyphData, vertexBuffer, texture, point2D.x, point2D.y, n4, n3);
        }
    }

    private void addDataToQuad(GlyphData glyphData, VertexBuffer vertexBuffer, Texture texture, float f, float f2, float f3, float f4) {
        f2 = Math.round(f2);
        Rectangle rectangle = glyphData.getRect();
        if (rectangle == null) {
            return;
        }
        int n = glyphData.getBlankBoundary();
        float f5 = rectangle.width - n * 2;
        float f6 = rectangle.height - n * 2;
        float f7 = (float)glyphData.getOriginX() + f;
        float f8 = (float)glyphData.getOriginY() + f2;
        float f9 = f8 + f6;
        float f10 = texture.getPhysicalWidth();
        float f11 = texture.getPhysicalHeight();
        float f12 = (float)(rectangle.x + n) / f10;
        float f13 = (float)(rectangle.y + n) / f11;
        float f14 = f12 + f5 / f10;
        float f15 = f13 + f6 / f11;
        if (this.isLCDCache) {
            f7 = (float)Math.round(f7 * 3.0f) / 3.0f;
            float f16 = f7 + f5 / 3.0f;
            float f17 = f7 / f3;
            float f18 = f16 / f3;
            float f19 = f8 / f4;
            float f20 = f9 / f4;
            vertexBuffer.addQuad(f7, f8, f16, f9, f12, f13, f14, f15, f17, f19, f18, f20);
        } else {
            f7 = Math.round(f7);
            float f21 = f7 + f5;
            if (this.context.isSuperShaderEnabled()) {
                vertexBuffer.addSuperQuad(f7, f8, f21, f9, f12, f13, f14, f15, true);
            } else {
                vertexBuffer.addQuad(f7, f8, f21, f9, f12, f13, f14, f15);
            }
        }
    }

    public Texture getBackingStore() {
        return this.packer.getBackingStore();
    }

    public void clear() {
        this.glyphDataMap.clear();
    }

    private void clearAll() {
        this.context.flushVertexBuffer();
        this.context.clearGlyphCaches();
        this.packer.clear();
    }

    private GlyphData getCachedGlyph(int n, int n2) {
        int n3 = n >>> 5;
        int n4 = n & 0x1F;
        GlyphData[] arrglyphData = this.glyphDataMap.get(n3 |= n2 << 27);
        if (arrglyphData != null) {
            if (arrglyphData[n4] != null) {
                return arrglyphData[n4];
            }
        } else {
            arrglyphData = new GlyphData[32];
            this.glyphDataMap.put(n3, arrglyphData);
        }
        GlyphData glyphData = null;
        Glyph glyph = this.strike.getGlyph(n);
        if (glyph != null) {
            byte[] arrby = glyph.getPixelData(n2);
            if (arrby == null || arrby.length == 0) {
                glyphData = new GlyphData(0, 0, 0, glyph.getPixelXAdvance(), glyph.getPixelYAdvance(), null);
            } else {
                MaskData maskData = MaskData.create(arrby, glyph.getOriginX(), glyph.getOriginY(), glyph.getWidth(), glyph.getHeight());
                int n5 = 1;
                int n6 = maskData.getWidth() + 2 * n5;
                int n7 = maskData.getHeight() + 2 * n5;
                int n8 = maskData.getOriginX();
                int n9 = maskData.getOriginY();
                Rectangle rectangle = new Rectangle(0, 0, n6, n7);
                glyphData = new GlyphData(n8, n9, n5, glyph.getPixelXAdvance(), glyph.getPixelYAdvance(), rectangle);
                if (!this.packer.add(rectangle)) {
                    if (PulseLogger.PULSE_LOGGING_ENABLED) {
                        PulseLogger.incrementCounter((String)"Font Glyph Cache Cleared");
                    }
                    this.clearAll();
                    if (!this.packer.add(rectangle)) {
                        if (PrismSettings.verbose) {
                            System.out.println(rectangle + " won't fit in GlyphCache");
                        }
                        return null;
                    }
                }
                boolean bl = true;
                Texture texture = this.getBackingStore();
                int n10 = rectangle.width;
                int n11 = rectangle.height;
                int n12 = texture.getPixelFormat().getBytesPerPixelUnit();
                int n13 = n10 * n12;
                int n14 = n13 * n11;
                if (emptyMask == null || n14 > emptyMask.capacity()) {
                    emptyMask = BufferUtil.newByteBuffer(n14);
                }
                try {
                    texture.update(emptyMask, texture.getPixelFormat(), rectangle.x, rectangle.y, 0, 0, n10, n11, n13, bl);
                }
                catch (Exception exception) {
                    if (PrismSettings.verbose) {
                        exception.printStackTrace();
                    }
                    return null;
                }
                maskData.uploadToTexture(texture, n5 + rectangle.x, n5 + rectangle.y, bl);
            }
            arrglyphData[n4] = glyphData;
        }
        return glyphData;
    }

    private static void disposePackerForContext(BaseContext baseContext, WeakHashMap<BaseContext, RectanglePacker> weakHashMap) {
        RectanglePacker rectanglePacker = weakHashMap.remove(baseContext);
        if (rectanglePacker != null) {
            rectanglePacker.dispose();
        }
    }

    public static void disposeForContext(BaseContext baseContext) {
        GlyphCache.disposePackerForContext(baseContext, greyPackerMap);
        GlyphCache.disposePackerForContext(baseContext, lcdPackerMap);
    }

    static {
        greyPackerMap = new WeakHashMap();
        lcdPackerMap = new WeakHashMap();
    }

    static class GlyphData {
        private final int originX;
        private final int originY;
        private final int blankBoundary;
        private final float xAdvance;
        private final float yAdvance;
        private final Rectangle rect;

        GlyphData(int n, int n2, int n3, float f, float f2, Rectangle rectangle) {
            this.originX = n;
            this.originY = n2;
            this.blankBoundary = n3;
            this.xAdvance = f;
            this.yAdvance = f2;
            this.rect = rectangle;
        }

        int getOriginX() {
            return this.originX;
        }

        int getOriginY() {
            return this.originY;
        }

        int getBlankBoundary() {
            return this.blankBoundary;
        }

        float getXAdvance() {
            return this.xAdvance;
        }

        float getYAdvance() {
            return this.yAdvance;
        }

        Rectangle getRect() {
            return this.rect;
        }
    }
}

