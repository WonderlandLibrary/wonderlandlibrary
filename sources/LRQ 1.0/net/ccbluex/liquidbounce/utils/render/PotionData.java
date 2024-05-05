/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.utils.render.Translate;

public class PotionData {
    public final IPotion potion;
    public int maxTimer = 0;
    public float animationX = 0.0f;
    public final Translate translate;
    public final int level;

    public PotionData(IPotion potion, Translate translate, int level) {
        this.potion = potion;
        this.translate = translate;
        this.level = level;
    }

    public float getAnimationX() {
        return this.animationX;
    }

    public IPotion getPotion() {
        return this.potion;
    }

    public int getMaxTimer() {
        return this.maxTimer;
    }
}

