/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.world.block.value;

import com.github.steveice10.mc.protocol.data.game.world.block.value.BlockValue;
import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class ChestValue
implements BlockValue {
    private int viewers;

    public ChestValue(int viewers) {
        this.viewers = viewers;
    }

    public int getViewers() {
        return this.viewers;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChestValue)) {
            return false;
        }
        ChestValue that = (ChestValue)o;
        return this.viewers == that.viewers;
    }

    public int hashCode() {
        return ObjectUtil.hashCode(this.viewers);
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

