/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

public class CompoundFilter
extends AbstractBufferedImageOp {
    private BufferedImageOp filter1;
    private BufferedImageOp filter2;

    public CompoundFilter(BufferedImageOp filter1, BufferedImageOp filter2) {
        this.filter1 = filter1;
        this.filter2 = filter2;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        BufferedImage image = this.filter1.filter(src, dst);
        image = this.filter2.filter(image, dst);
        return image;
    }
}

