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

    public WorldBorderImpl(WorldBorder worldBorder) {
        this.wrapped = worldBorder;
    }

    @Override
    public boolean contains(WBlockPos wBlockPos) {
        WBlockPos wBlockPos2 = wBlockPos;
        WorldBorder worldBorder = this.wrapped;
        boolean bl = false;
        BlockPos blockPos = new BlockPos(wBlockPos2.getX(), wBlockPos2.getY(), wBlockPos2.getZ());
        return worldBorder.func_177746_a(blockPos);
    }

    public final WorldBorder getWrapped() {
        return this.wrapped;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof WorldBorderImpl && ((WorldBorderImpl)object).wrapped.equals(this.wrapped);
    }
}

