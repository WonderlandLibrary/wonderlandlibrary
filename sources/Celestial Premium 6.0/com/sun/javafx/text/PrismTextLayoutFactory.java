/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.text;

import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.text.PrismTextLayout;

public class PrismTextLayoutFactory
implements TextLayoutFactory {
    private static final PrismTextLayout reusableTL = new PrismTextLayout();
    private static boolean inUse;
    private static final PrismTextLayoutFactory factory;

    private PrismTextLayoutFactory() {
    }

    @Override
    public TextLayout createLayout() {
        return new PrismTextLayout();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public TextLayout getLayout() {
        if (inUse) {
            return new PrismTextLayout();
        }
        Class<PrismTextLayoutFactory> class_ = PrismTextLayoutFactory.class;
        synchronized (PrismTextLayoutFactory.class) {
            if (inUse) {
                // ** MonitorExit[var1_1] (shouldn't be in output)
                return new PrismTextLayout();
            }
            inUse = true;
            reusableTL.setAlignment(0);
            reusableTL.setWrapWidth(0.0f);
            reusableTL.setDirection(0);
            reusableTL.setContent(null);
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return reusableTL;
        }
    }

    @Override
    public void disposeLayout(TextLayout textLayout) {
        if (textLayout == reusableTL) {
            inUse = false;
        }
    }

    public static PrismTextLayoutFactory getFactory() {
        return factory;
    }

    static {
        factory = new PrismTextLayoutFactory();
    }
}

