/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.PresentableState;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.j2d.J2DPresentable;
import com.sun.prism.j2d.J2DPrismGraphics;
import com.sun.prism.j2d.J2DResourceFactory;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public final class PrismPrintGraphics
extends J2DPrismGraphics
implements PrinterGraphics {
    private AffineTransform origTx2D;

    @Override
    protected void setTransformG2D(AffineTransform affineTransform) {
        this.g2d.setTransform(this.origTx2D);
        this.g2d.transform(affineTransform);
    }

    @Override
    protected void captureTransform(Graphics2D graphics2D) {
        this.origTx2D = graphics2D.getTransform();
    }

    public PrismPrintGraphics(Graphics2D graphics2D, int n, int n2) {
        super(new PagePresentable(n, n2), graphics2D);
        this.setClipRect(new Rectangle(0, 0, n, n2));
    }

    PrismPrintGraphics(J2DPresentable j2DPresentable, Graphics2D graphics2D) {
        super(j2DPresentable, graphics2D);
    }

    static class PagePresentable
    extends J2DPresentable {
        private int width;
        private int height;
        static J2DResourceFactory factory = new PrintResourceFactory();
        private boolean opaque;

        PagePresentable(int n, int n2) {
            super(null, factory);
            this.width = n;
            this.height = n2;
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
            throw new UnsupportedOperationException("Cannot prepare an image");
        }

        @Override
        public boolean present() {
            throw new UnsupportedOperationException("Cannot present on image");
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
        public void setOpaque(boolean bl) {
            this.opaque = bl;
        }

        @Override
        public boolean isOpaque() {
            return this.opaque;
        }
    }

    static class PrintResourceFactory
    extends J2DResourceFactory {
        PrintResourceFactory() {
            super(null);
        }

        @Override
        J2DPrismGraphics createJ2DPrismGraphics(J2DPresentable j2DPresentable, Graphics2D graphics2D) {
            PrismPrintGraphics prismPrintGraphics = new PrismPrintGraphics(j2DPresentable, graphics2D);
            Rectangle rectangle = new Rectangle(0, 0, j2DPresentable.getContentWidth(), j2DPresentable.getContentHeight());
            prismPrintGraphics.setClipRect(rectangle);
            return prismPrintGraphics;
        }
    }
}

