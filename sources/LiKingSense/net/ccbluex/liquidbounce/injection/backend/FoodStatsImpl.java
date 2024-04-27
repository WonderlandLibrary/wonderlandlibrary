/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.FoodStats
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.IFoodStats;
import net.minecraft.util.FoodStats;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/FoodStatsImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IFoodStats;", "wrapped", "Lnet/minecraft/util/FoodStats;", "(Lnet/minecraft/util/FoodStats;)V", "foodLevel", "", "getFoodLevel", "()I", "getWrapped", "()Lnet/minecraft/util/FoodStats;", "equals", "", "other", "", "LiKingSense"})
public final class FoodStatsImpl
implements IFoodStats {
    @NotNull
    private final FoodStats wrapped;

    @Override
    public int getFoodLevel() {
        return this.wrapped.func_75116_a();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof FoodStatsImpl && Intrinsics.areEqual((Object)((FoodStatsImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final FoodStats getWrapped() {
        return this.wrapped;
    }

    public FoodStatsImpl(@NotNull FoodStats wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

