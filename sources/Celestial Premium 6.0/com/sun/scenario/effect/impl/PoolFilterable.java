/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl;

import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.impl.ImagePool;

public interface PoolFilterable
extends Filterable {
    public void setImagePool(ImagePool var1);

    public ImagePool getImagePool();
}

