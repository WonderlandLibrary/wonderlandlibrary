/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.border.WorldBorder
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.world.border.IWorldBorder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0013\u0010\u000b\u001a\u00020\b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0096\u0002R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/WorldBorderImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/world/border/IWorldBorder;", "wrapped", "Lnet/minecraft/world/border/WorldBorder;", "(Lnet/minecraft/world/border/WorldBorder;)V", "getWrapped", "()Lnet/minecraft/world/border/WorldBorder;", "contains", "", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "equals", "other", "", "LiKingSense"})
public final class WorldBorderImpl
implements IWorldBorder {
    @NotNull
    private final WorldBorder wrapped;

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean contains(@NotNull WBlockPos blockPos) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)blockPos, (String)"blockPos");
        WBlockPos wBlockPos = blockPos;
        WorldBorder worldBorder = this.wrapped;
        boolean $i$f$unwrap = false;
        BlockPos blockPos2 = new BlockPos($this$unwrap$iv.getX(), $this$unwrap$iv.getY(), $this$unwrap$iv.getZ());
        return worldBorder.func_177746_a(blockPos2);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof WorldBorderImpl && Intrinsics.areEqual((Object)((WorldBorderImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final WorldBorder getWrapped() {
        return this.wrapped;
    }

    public WorldBorderImpl(@NotNull WorldBorder wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

