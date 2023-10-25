/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.world.effect;

import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.data.game.world.effect.WorldEffectData;
import com.github.steveice10.mc.protocol.util.ObjectUtil;
import java.util.Objects;

public class BreakBlockEffectData
implements WorldEffectData {
    private BlockState blockState;

    public BreakBlockEffectData(BlockState blockState) {
        this.blockState = blockState;
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BreakBlockEffectData)) {
            return false;
        }
        BreakBlockEffectData that = (BreakBlockEffectData)o;
        return Objects.equals(this.blockState, that.blockState);
    }

    public int hashCode() {
        return ObjectUtil.hashCode(this.blockState);
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

