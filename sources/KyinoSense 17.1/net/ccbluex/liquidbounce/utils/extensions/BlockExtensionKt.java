/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.extensions;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=2, xi=2, d1={"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\f\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u0002\u001a\n\u0010\u0003\u001a\u00020\u0004*\u00020\u0002\u00a8\u0006\u0005"}, d2={"getBlock", "Lnet/minecraft/block/Block;", "Lnet/minecraft/util/BlockPos;", "getVec", "Lnet/minecraft/util/Vec3;", "KyinoClient"})
public final class BlockExtensionKt {
    @Nullable
    public static final Block getBlock(@NotNull BlockPos $this$getBlock) {
        Intrinsics.checkParameterIsNotNull($this$getBlock, "$this$getBlock");
        return BlockUtils.getBlock($this$getBlock);
    }

    @NotNull
    public static final Vec3 getVec(@NotNull BlockPos $this$getVec) {
        Intrinsics.checkParameterIsNotNull($this$getVec, "$this$getVec");
        return new Vec3((double)$this$getVec.func_177958_n() + 0.5, (double)$this$getVec.func_177956_o() + 0.5, (double)$this$getVec.func_177952_p() + 0.5);
    }
}

