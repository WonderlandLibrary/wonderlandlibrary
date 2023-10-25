/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.potion;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionHealth
extends Potion {
    @Override
    public boolean isReady(int n, int n2) {
        return n >= 1;
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    public PotionHealth(int n, ResourceLocation resourceLocation, boolean bl, int n2) {
        super(n, resourceLocation, bl, n2);
    }
}

