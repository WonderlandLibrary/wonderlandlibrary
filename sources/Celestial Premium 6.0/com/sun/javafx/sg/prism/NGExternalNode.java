/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.Graphics;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import java.nio.Buffer;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class NGExternalNode
extends NGNode {
    private Texture dsttexture;
    private BufferData bufferData;
    private final AtomicReference<RenderData> renderData = new AtomicReference<Object>(null);
    private RenderData rd;
    private volatile ReentrantLock bufferLock;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void renderContent(Graphics graphics) {
        RenderData renderData = this.renderData.getAndSet(null);
        if (renderData != null) {
            this.rd = renderData;
        }
        if (this.rd == null) {
            return;
        }
        int n = this.rd.bdata.srcbounds.x;
        int n2 = this.rd.bdata.srcbounds.y;
        int n3 = this.rd.bdata.srcbounds.width;
        int n4 = this.rd.bdata.srcbounds.height;
        if (this.dsttexture != null) {
            this.dsttexture.lock();
            if (this.dsttexture.isSurfaceLost() || this.dsttexture.getContentWidth() != n3 || this.dsttexture.getContentHeight() != n4) {
                this.dsttexture.unlock();
                this.dsttexture.dispose();
                this.rd = this.rd.copyAddDirtyRect(0, 0, n3, n4);
                this.dsttexture = this.createTexture(graphics, this.rd);
            }
        } else {
            this.dsttexture = this.createTexture(graphics, this.rd);
        }
        if (this.dsttexture == null) {
            return;
        }
        try {
            if (renderData != null) {
                this.bufferLock.lock();
                try {
                    this.dsttexture.update(this.rd.bdata.srcbuffer, PixelFormat.INT_ARGB_PRE, this.rd.dirtyRect.x, this.rd.dirtyRect.y, n + this.rd.dirtyRect.x, n2 + this.rd.dirtyRect.y, this.rd.dirtyRect.width, this.rd.dirtyRect.height, this.rd.bdata.linestride * 4, false);
                }
                finally {
                    this.bufferLock.unlock();
                }
                if (this.rd.clearTarget) {
                    graphics.clearQuad(0.0f, 0.0f, this.rd.bdata.usrwidth, this.rd.bdata.usrheight);
                }
            }
            graphics.drawTexture(this.dsttexture, 0.0f, 0.0f, this.rd.bdata.usrwidth, this.rd.bdata.usrheight, 0.0f, 0.0f, n3, n4);
        }
        finally {
            this.dsttexture.unlock();
        }
    }

    private Texture createTexture(Graphics graphics, RenderData renderData) {
        ResourceFactory resourceFactory = graphics.getResourceFactory();
        if (resourceFactory.isDisposed()) {
            return null;
        }
        Texture texture = resourceFactory.createTexture(PixelFormat.INT_ARGB_PRE, Texture.Usage.DYNAMIC, Texture.WrapMode.CLAMP_NOT_NEEDED, renderData.bdata.srcbounds.width, renderData.bdata.srcbounds.height);
        if (texture != null) {
            texture.contentsUseful();
        } else {
            System.err.println("NGExternalNode: failed to create a texture");
        }
        return texture;
    }

    public void setLock(ReentrantLock reentrantLock) {
        this.bufferLock = reentrantLock;
    }

    public void setImageBuffer(Buffer buffer, int n, int n2, int n3, int n4, float f, float f2, int n5, double d, double d2) {
        this.bufferData = new BufferData(buffer, n5, n, n2, n3, n4, f, f2, d, d2);
        this.renderData.set(new RenderData(this.bufferData, n, n2, n3, n4, true));
    }

    public void setImageBounds(int n, int n2, int n3, int n4, float f, float f2) {
        boolean bl = (float)n3 < this.bufferData.usrwidth || (float)n4 < this.bufferData.usrheight;
        this.bufferData = this.bufferData.copyWithBounds(n, n2, n3, n4, f, f2);
        this.renderData.updateAndGet(renderData -> {
            boolean bl2 = renderData != null ? renderData.clearTarget : false;
            return new RenderData(this.bufferData, n, n2, n3, n4, bl2 | bl);
        });
    }

    public void repaintDirtyRegion(int n, int n2, int n3, int n4) {
        this.renderData.updateAndGet(renderData -> {
            if (renderData != null) {
                return renderData.copyAddDirtyRect(n, n2, n3, n4);
            }
            return new RenderData(this.bufferData, n, n2, n3, n4, false);
        });
    }

    public void markContentDirty() {
        this.visualsChanged();
    }

    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }

    private static class RenderData {
        final BufferData bdata;
        final Rectangle dirtyRect;
        final boolean clearTarget;

        RenderData(BufferData bufferData, int n, int n2, int n3, int n4, boolean bl) {
            this(bufferData, n, n2, n3, n4, bl, true);
        }

        RenderData(BufferData bufferData, int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
            this.bdata = bufferData;
            Rectangle rectangle = new Rectangle(n, n2, n3, n4);
            this.dirtyRect = bl2 ? bufferData.scale(rectangle) : rectangle;
            this.dirtyRect.intersectWith(bufferData.srcbounds);
            this.clearTarget = bl;
        }

        RenderData copyAddDirtyRect(int n, int n2, int n3, int n4) {
            Rectangle rectangle = this.bdata.scale(new Rectangle(n, n2, n3, n4));
            rectangle.add(this.dirtyRect);
            return new RenderData(this.bdata, rectangle.x, rectangle.y, rectangle.width, rectangle.height, this.clearTarget, false);
        }
    }

    private static class BufferData {
        final Buffer srcbuffer;
        final int linestride;
        final Rectangle srcbounds;
        final float usrwidth;
        final float usrheight;
        final double scaleX;
        final double scaleY;

        BufferData(Buffer buffer, int n, int n2, int n3, int n4, int n5, float f, float f2, double d, double d2) {
            this.srcbuffer = buffer;
            this.scaleX = d;
            this.scaleY = d2;
            this.linestride = n;
            this.srcbounds = this.scale(new Rectangle(n2, n3, n4, n5));
            this.usrwidth = f;
            this.usrheight = f2;
        }

        Rectangle scale(Rectangle rectangle) {
            int n = rectangle.x;
            rectangle.x = (int)Math.round((double)n * this.scaleX);
            int n2 = rectangle.y;
            rectangle.y = (int)Math.round((double)n2 * this.scaleY);
            rectangle.width = (int)Math.round((double)rectangle.width * this.scaleX);
            rectangle.height = (int)Math.round((double)rectangle.height * this.scaleY);
            return rectangle;
        }

        BufferData copyWithBounds(int n, int n2, int n3, int n4, float f, float f2) {
            return new BufferData(this.srcbuffer, this.linestride, n, n2, n3, n4, f, f2, this.scaleX, this.scaleY);
        }
    }
}

