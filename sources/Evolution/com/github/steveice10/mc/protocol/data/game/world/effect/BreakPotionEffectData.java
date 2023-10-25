/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.world.effect;

import com.github.steveice10.mc.protocol.data.game.world.effect.WorldEffectData;
import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class BreakPotionEffectData
implements WorldEffectData {
    private int potionId;

    public BreakPotionEffectData(int potionId) {
        this.potionId = potionId;
    }

    public int getPotionId() {
        return this.potionId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BreakPotionEffectData)) {
            return false;
        }
        BreakPotionEffectData that = (BreakPotionEffectData)o;
        return this.potionId == that.potionId;
    }

    public int hashCode() {
        return ObjectUtil.hashCode(this.potionId);
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

