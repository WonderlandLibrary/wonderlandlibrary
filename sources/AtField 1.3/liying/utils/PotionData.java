/*
 * Decompiled with CFR 0.152.
 */
package liying.utils;

import liying.utils.Translate;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;

public class PotionData {
    public final IPotion potion;
    public int maxTimer = 0;
    public final Translate translate;
    public float animationX = 0.0f;
    public final int level;

    public float getAnimationX() {
        return this.animationX;
    }

    public IPotion getPotion() {
        return this.potion;
    }

    public int getMaxTimer() {
        return this.maxTimer;
    }

    public PotionData(IPotion iPotion, Translate translate, int n) {
        this.potion = iPotion;
        this.translate = translate;
        this.level = n;
    }
}

