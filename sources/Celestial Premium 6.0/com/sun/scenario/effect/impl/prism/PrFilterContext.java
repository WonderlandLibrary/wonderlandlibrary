/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism;

import com.sun.glass.ui.Screen;
import com.sun.scenario.effect.FilterContext;
import java.util.Map;
import java.util.WeakHashMap;

public class PrFilterContext
extends FilterContext {
    private static Screen defaultScreen;
    private static final Map<Screen, PrFilterContext> ctxMap;
    private static PrFilterContext printerFilterContext;
    private PrFilterContext swinstance;
    private boolean forceSW;

    public static PrFilterContext getPrinterContext(Object object) {
        if (printerFilterContext == null) {
            printerFilterContext = new PrFilterContext(object);
        }
        return printerFilterContext;
    }

    private PrFilterContext(Object object) {
        super(object);
    }

    public static PrFilterContext getInstance(Screen screen) {
        if (screen == null) {
            throw new IllegalArgumentException("Screen must be non-null");
        }
        PrFilterContext prFilterContext = ctxMap.get(screen);
        if (prFilterContext == null) {
            prFilterContext = new PrFilterContext(screen);
            ctxMap.put(screen, prFilterContext);
        }
        return prFilterContext;
    }

    public static PrFilterContext getDefaultInstance() {
        if (defaultScreen == null) {
            defaultScreen = Screen.getMainScreen();
        }
        return PrFilterContext.getInstance(defaultScreen);
    }

    public PrFilterContext getSoftwareInstance() {
        if (this.swinstance == null) {
            if (this.forceSW) {
                this.swinstance = this;
            } else {
                this.swinstance = new PrFilterContext(this.getReferent());
                this.swinstance.forceSW = true;
            }
        }
        return this.swinstance;
    }

    public boolean isForceSoftware() {
        return this.forceSW;
    }

    private static int hashCode(boolean bl) {
        return bl ? 1231 : 1237;
    }

    @Override
    public int hashCode() {
        return this.getReferent().hashCode() ^ PrFilterContext.hashCode(this.forceSW);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PrFilterContext)) {
            return false;
        }
        PrFilterContext prFilterContext = (PrFilterContext)object;
        return this.getReferent().equals(prFilterContext.getReferent()) && this.forceSW == prFilterContext.forceSW;
    }

    static {
        ctxMap = new WeakHashMap<Screen, PrFilterContext>();
        printerFilterContext = null;
    }
}

