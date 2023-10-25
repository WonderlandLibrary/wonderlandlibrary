/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Graphics;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.QueuedPixelSource;
import com.sun.prism.j2d.J2DRTTexture;
import com.sun.prism.j2d.J2DResourceFactory;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public abstract class J2DPresentable
implements Presentable {
    J2DResourceFactory factory;
    boolean needsResize;
    BufferedImage buffer;
    IntBuffer ib;
    J2DRTTexture readbackBuffer;

    static J2DPresentable create(PresentableState presentableState, J2DResourceFactory j2DResourceFactory) {
        return new Glass(presentableState, j2DResourceFactory);
    }

    static J2DPresentable create(BufferedImage bufferedImage, J2DResourceFactory j2DResourceFactory) {
        return new Bimg(bufferedImage, j2DResourceFactory);
    }

    J2DPresentable(BufferedImage bufferedImage, J2DResourceFactory j2DResourceFactory) {
        this.buffer = bufferedImage;
        this.factory = j2DResourceFactory;
    }

    ResourceFactory getResourceFactory() {
        return this.factory;
    }

    public abstract BufferedImage createBuffer(int var1, int var2);

    @Override
    public Graphics createGraphics() {
        if (this.needsResize) {
            int n = this.getContentWidth();
            int n2 = this.getContentHeight();
            this.buffer = null;
            this.readbackBuffer = null;
            this.buffer = this.createBuffer(n, n2);
            WritableRaster writableRaster = this.buffer.getRaster();
            DataBuffer dataBuffer = writableRaster.getDataBuffer();
            SinglePixelPackedSampleModel singlePixelPackedSampleModel = (SinglePixelPackedSampleModel)writableRaster.getSampleModel();
            int[] arrn = ((DataBufferInt)dataBuffer).getData();
            this.ib = IntBuffer.wrap(arrn, dataBuffer.getOffset(), dataBuffer.getSize());
            this.needsResize = false;
        }
        Graphics2D graphics2D = this.buffer.createGraphics();
        return this.factory.createJ2DPrismGraphics(this, graphics2D);
    }

    J2DRTTexture getReadbackBuffer() {
        if (this.readbackBuffer == null) {
            this.readbackBuffer = (J2DRTTexture)this.factory.createRTTexture(this.getContentWidth(), this.getContentHeight(), Texture.WrapMode.CLAMP_NOT_NEEDED);
            this.readbackBuffer.makePermanent();
        }
        return this.readbackBuffer;
    }

    BufferedImage getBackBuffer() {
        return this.buffer;
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.factory.getScreen();
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
    public float getPixelScaleFactorX() {
        return 1.0f;
    }

    @Override
    public float getPixelScaleFactorY() {
        return 1.0f;
    }

    @Override
    public int getPhysicalWidth() {
        return this.buffer == null ? this.getContentWidth() : this.buffer.getWidth();
    }

    @Override
    public int getPhysicalHeight() {
        return this.buffer == null ? this.getContentHeight() : this.buffer.getHeight();
    }

    @Override
    public boolean isMSAA() {
        return false;
    }

    private static class Glass
    extends J2DPresentable {
        private final PresentableState pState;
        private final int theFormat;
        private Pixels pixels;
        private QueuedPixelSource pixelSource = new QueuedPixelSource(false);
        private boolean opaque;

        Glass(PresentableState presentableState, J2DResourceFactory j2DResourceFactory) {
            super(null, j2DResourceFactory);
            this.pState = presentableState;
            this.theFormat = presentableState.getPixelFormat();
            this.needsResize = true;
        }

        @Override
        public BufferedImage createBuffer(int n, int n2) {
            if (PrismSettings.verbose) {
                System.out.println("Glass native format: " + this.theFormat);
            }
            ByteOrder byteOrder = ByteOrder.nativeOrder();
            switch (this.theFormat) {
                case 1: {
                    if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                        return new BufferedImage(n, n2, 3);
                    }
                    throw new UnsupportedOperationException("BYTE_BGRA_PRE pixel format on BIG_ENDIAN");
                }
                case 2: {
                    if (byteOrder == ByteOrder.BIG_ENDIAN) {
                        return new BufferedImage(n, n2, 2);
                    }
                    throw new UnsupportedOperationException("BYTE_ARGB pixel format on LITTLE_ENDIAN");
                }
            }
            throw new UnsupportedOperationException("unrecognized pixel format: " + this.theFormat);
        }

        @Override
        public boolean lockResources(PresentableState presentableState) {
            if (this.pState != presentableState || this.theFormat != presentableState.getPixelFormat()) {
                return true;
            }
            this.needsResize = this.buffer == null || this.buffer.getWidth() != presentableState.getWidth() || this.buffer.getHeight() != presentableState.getHeight();
            return false;
        }

        @Override
        public boolean prepare(Rectangle rectangle) {
            if (!this.pState.isViewClosed()) {
                int n = this.getPhysicalWidth();
                int n2 = this.getPhysicalHeight();
                this.pixels = this.pixelSource.getUnusedPixels(n, n2, 1.0f, 1.0f);
                IntBuffer intBuffer = (IntBuffer)this.pixels.getPixels();
                assert (this.ib.hasArray());
                System.arraycopy(this.ib.array(), 0, intBuffer.array(), 0, n * n2);
                return true;
            }
            return false;
        }

        @Override
        public boolean present() {
            this.pixelSource.enqueuePixels(this.pixels);
            this.pState.uploadPixels(this.pixelSource);
            return true;
        }

        @Override
        public int getContentWidth() {
            return this.pState.getWidth();
        }

        @Override
        public int getContentHeight() {
            return this.pState.getHeight();
        }

        @Override
        public void setOpaque(boolean bl) {
            this.opaque = bl;
        }

        @Override
        public boolean isOpaque() {
            return this.opaque;
        }
    }

    private static class Bimg
    extends J2DPresentable {
        private boolean opaque;

        public Bimg(BufferedImage bufferedImage, J2DResourceFactory j2DResourceFactory) {
            super(bufferedImage, j2DResourceFactory);
        }

        @Override
        public BufferedImage createBuffer(int n, int n2) {
            throw new UnsupportedOperationException("cannot create new buffers for image");
        }

        @Override
        public boolean lockResources(PresentableState presentableState) {
            return false;
        }

        @Override
        public boolean prepare(Rectangle rectangle) {
            throw new UnsupportedOperationException("cannot prepare/present on image");
        }

        @Override
        public boolean present() {
            throw new UnsupportedOperationException("cannot prepare/present on image");
        }

        @Override
        public int getContentWidth() {
            return this.buffer.getWidth();
        }

        @Override
        public int getContentHeight() {
            return this.buffer.getHeight();
        }

        @Override
        public void setOpaque(boolean bl) {
            this.opaque = bl;
        }

        @Override
        public boolean isOpaque() {
            return this.opaque;
        }
    }
}

