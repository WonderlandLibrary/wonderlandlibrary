/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.FoodStats
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IFoodStats;
import net.ccbluex.liquidbounce.injection.backend.FoodStatsImpl;
import net.minecraft.util.FoodStats;

public final class FoodStatsImplKt {
    public static final FoodStats unwrap(IFoodStats $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((FoodStatsImpl)$this$unwrap).getWrapped();
    }

    public static final IFoodStats wrap(FoodStats $this$wrap) {
        int $i$f$wrap = 0;
        return new FoodStatsImpl($this$wrap);
    }
}

