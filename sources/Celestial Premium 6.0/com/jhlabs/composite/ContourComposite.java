/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.composite;

import com.jhlabs.composite.ContourCompositeContext;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;

public final class ContourComposite
implements Composite {
    private int offset;

    public ContourComposite(int offset) {
        this.offset = offset;
    }

    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return new ContourCompositeContext(this.offset, srcColorModel, dstColorModel);
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object o) {
        return o instanceof ContourComposite;
    }
}

