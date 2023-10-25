/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.animation.impl.utils;

public class Progression {
    double value;

    public Progression() {
        this.value = 0.0;
    }

    public Progression(double value) {
        this.value = value;
    }

    public final double getValue() {
        return this.value;
    }

    public final void setValue(double value) {
        this.value = value;
    }

    public final void reset() {
        this.value = 0.0;
    }
}

