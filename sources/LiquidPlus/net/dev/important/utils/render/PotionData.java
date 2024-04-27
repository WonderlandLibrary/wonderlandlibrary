/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.potion.Potion
 */
package net.dev.important.utils.render;

import net.dev.important.utils.render.Translate;
import net.minecraft.potion.Potion;

public class PotionData {
    public final Potion potion;
    public int maxTimer = 0;
    public float animationX = 0.0f;
    public final Translate translate;
    public final int level;

    public PotionData(Potion potion, Translate translate, int level) {
        this.potion = potion;
        this.translate = translate;
        this.level = level;
    }

    public float getAnimationX() {
        return this.animationX;
    }

    public Potion getPotion() {
        return this.potion;
    }

    public int getMaxTimer() {
        return this.maxTimer;
    }
}

