/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.pisces.JavaSurface;
import com.sun.pisces.PiscesRenderer;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.sw.SWArgbPreTexture;
import com.sun.prism.sw.SWGraphics;
import com.sun.prism.sw.SWResourceFactory;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class SWRTTexture
extends SWArgbPreTexture
implements RTTexture {
    private PiscesRenderer pr;
    private JavaSurface surface;
    private final Rectangle dimensions = new Rectangle();
    private boolean isOpaque;

    SWRTTexture(SWResourceFactory sWResourceFactory, int n, int n2) {
        super(sWResourceFactory, Texture.WrapMode.CLAMP_TO_ZERO, n, n2);
        this.allocate();
        this.surface = new JavaSurface(this.getDataNoClone(), 1, n, n2);
        this.dimensions.setBounds(0, 0, n, n2);
    }

    JavaSurface getSurface() {
        return this.surface;
    }

    @Override
    public int[] getPixels() {
        if (this.contentWidth == this.physicalWidth) {
            return this.getDataNoClone();
        }
        return null;
    }

    @Override
    public boolean readPixels(Buffer buffer, int n, int n2, int n3, int n4) {
        if (n != this.getContentX() || n2 != this.getContentY() || n3 != this.getContentWidth() || n4 != this.getContentHeight()) {
            throw new IllegalArgumentException("reading subtexture not yet supported!");
        }
        return this.readPixels(buffer);
    }

    @Override
    public boolean readPixels(Buffer buffer) {
        if (PrismSettings.debug) {
            System.out.println("+ SWRTT.readPixels: this: " + this);
        }
        int[] arrn = this.getDataNoClone();
        buffer.clear();
        if (buffer instanceof IntBuffer) {
            IntBuffer intBuffer = (IntBuffer)buffer;
            for (int i = 0; i < this.contentHeight; ++i) {
                intBuffer.put(arrn, i * this.physicalWidth, this.contentWidth);
            }
        } else if (buffer instanceof ByteBuffer) {
            ByteBuffer byteBuffer = (ByteBuffer)buffer;
            for (int i = 0; i < this.contentHeight; ++i) {
                for (int j = 0; j < this.contentWidth; ++j) {
                    int n = arrn[i * this.physicalWidth + j];
                    byte by = (byte)(n >> 24);
                    byte by2 = (byte)(n >> 16);
                    byte by3 = (byte)(n >> 8);
                    byte by4 = (byte)n;
                    byteBuffer.put(by4).put(by3).put(by2).put(by);
                }
            }
        } else {
            return false;
        }
        buffer.rewind();
        return true;
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.getResourceFactory().getScreen();
    }

    @Override
    public Graphics createGraphics() {
        if (this.pr == null) {
            this.pr = new PiscesRenderer(this.surface);
        }
        return new SWGraphics(this, this.getResourceFactory().getContext(), this.pr);
    }

    @Override
    public boolean isOpaque() {
        return this.isOpaque;
    }

    @Override
    public void setOpaque(boolean bl) {
        this.isOpaque = bl;
    }

    Rectangle getDimensions() {
        return this.dimensions;
    }

    @Override
    public boolean isVolatile() {
        return false;
    }

    @Override
    public boolean isMSAA() {
        return false;
    }
}

