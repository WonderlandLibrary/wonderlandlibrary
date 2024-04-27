/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.border.WorldBorder
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.world.border.IWorldBorder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.Nullable;

public final class WorldBorderImpl
implements IWorldBorder {
    private final WorldBorder wrapped;

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean contains(WBlockPos blockPos) {
        void $this$unwrap$iv;
        WBlockPos wBlockPos = blockPos;
        WorldBorder worldBorder = this.wrapped;
        boolean $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos($this$unwrap$iv.getX(), $this$unwrap$iv.getY(), $this$unwrap$iv.getZ());
        return worldBorder.func_177746_a(blockPos2);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof WorldBorderImpl && ((WorldBorderImpl)other).wrapped.equals(this.wrapped);
    }

    public final WorldBorder getWrapped() {
        return this.wrapped;
    }

    public WorldBorderImpl(WorldBorder wrapped) {
        this.wrapped = wrapped;
    }
}

