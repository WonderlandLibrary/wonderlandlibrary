/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl;

import com.sun.scenario.effect.Filterable;

public interface HeapImage
extends Filterable {
    public int getScanlineStride();

    public int[] getPixelArray();
}

