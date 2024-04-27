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
    public static final FoodStats unwrap(IFoodStats iFoodStats) {
        boolean bl = false;
        return ((FoodStatsImpl)iFoodStats).getWrapped();
    }

    public static final IFoodStats wrap(FoodStats foodStats) {
        boolean bl = false;
        return new FoodStatsImpl(foodStats);
    }
}

