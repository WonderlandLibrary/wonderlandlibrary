/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.world.notify;

import com.github.steveice10.mc.protocol.data.game.world.notify.ClientNotificationValue;
import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class RainStrengthValue
implements ClientNotificationValue {
    private float strength;

    public RainStrengthValue(float strength) {
        if (strength > 1.0f) {
            strength = 1.0f;
        }
        if (strength < 0.0f) {
            strength = 0.0f;
        }
        this.strength = strength;
    }

    public float getStrength() {
        return this.strength;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RainStrengthValue)) {
            return false;
        }
        RainStrengthValue that = (RainStrengthValue)o;
        return Float.compare(this.strength, that.strength) == 0;
    }

    public int hashCode() {
        return ObjectUtil.hashCode(Float.valueOf(this.strength));
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

