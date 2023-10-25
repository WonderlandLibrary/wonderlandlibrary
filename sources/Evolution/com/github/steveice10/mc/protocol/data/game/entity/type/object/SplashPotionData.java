/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.entity.type.object;

import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectData;
import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class SplashPotionData
implements ObjectData {
    private int potionData;

    public SplashPotionData(int potionData) {
        this.potionData = potionData;
    }

    public int getPotionData() {
        return this.potionData;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SplashPotionData)) {
            return false;
        }
        SplashPotionData that = (SplashPotionData)o;
        return this.potionData == that.potionData;
    }

    public int hashCode() {
        return ObjectUtil.hashCode(this.potionData);
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

