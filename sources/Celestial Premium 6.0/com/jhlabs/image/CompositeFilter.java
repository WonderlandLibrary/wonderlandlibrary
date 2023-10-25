/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class CompositeFilter
extends AbstractBufferedImageOp {
    private Composite composite;
    private AffineTransform transform;

    public CompositeFilter() {
    }

    public CompositeFilter(Composite composite) {
        this.composite = composite;
    }

    public CompositeFilter(Composite composite, AffineTransform transform) {
        this.composite = composite;
        this.transform = transform;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    public Composite getComposite() {
        return this.composite;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }

    public AffineTransform getTransform() {
        return this.transform;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        Graphics2D g = dst.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setComposite(this.composite);
        g.drawRenderedImage(src, this.transform);
        g.dispose();
        return dst;
    }

    public String toString() {
        return "Composite";
    }
}

