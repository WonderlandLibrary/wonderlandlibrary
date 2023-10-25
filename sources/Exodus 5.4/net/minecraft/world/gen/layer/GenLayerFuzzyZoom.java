/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerZoom;

public class GenLayerFuzzyZoom
extends GenLayerZoom {
    public GenLayerFuzzyZoom(long l, GenLayer genLayer) {
        super(l, genLayer);
    }

    @Override
    protected int selectModeOrRandom(int n, int n2, int n3, int n4) {
        return this.selectRandom(n, n2, n3, n4);
    }
}

