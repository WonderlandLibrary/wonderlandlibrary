/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RenderTextFilter
extends AbstractBufferedImageOp {
    private String text;
    private Font font;
    private Paint paint;
    private Composite composite;
    private AffineTransform transform;

    public RenderTextFilter() {
    }

    public RenderTextFilter(String text, Font font, Paint paint, Composite composite, AffineTransform transform) {
        this.text = text;
        this.font = font;
        this.composite = composite;
        this.paint = paint;
        this.transform = transform;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    public Composite getComposite() {
        return this.composite;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
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
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (this.font != null) {
            g.setFont(this.font);
        }
        if (this.transform != null) {
            g.setTransform(this.transform);
        }
        if (this.composite != null) {
            g.setComposite(this.composite);
        }
        if (this.paint != null) {
            g.setPaint(this.paint);
        }
        if (this.text != null) {
            g.drawString(this.text, 10, 100);
        }
        g.dispose();
        return dst;
    }
}

