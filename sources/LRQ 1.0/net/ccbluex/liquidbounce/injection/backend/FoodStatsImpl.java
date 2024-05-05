/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.FoodStats
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IFoodStats;
import net.minecraft.util.FoodStats;
import org.jetbrains.annotations.Nullable;

public final class FoodStatsImpl
implements IFoodStats {
    private final FoodStats wrapped;

    @Override
    public int getFoodLevel() {
        return this.wrapped.func_75116_a();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof FoodStatsImpl && ((FoodStatsImpl)other).wrapped.equals(this.wrapped);
    }

    public final FoodStats getWrapped() {
        return this.wrapped;
    }

    public FoodStatsImpl(FoodStats wrapped) {
        this.wrapped = wrapped;
    }
}

