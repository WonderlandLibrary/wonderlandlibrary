/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.extensions;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=2, d1={"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\f\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u0002\u001a\n\u0010\u0003\u001a\u00020\u0004*\u00020\u0002\u00a8\u0006\u0005"}, d2={"getBlock", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getVec", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "LiKingSense"})
public final class BlockExtensionKt {
    @Nullable
    public static final IBlock getBlock(@NotNull WBlockPos $this$getBlock) {
        Intrinsics.checkParameterIsNotNull((Object)$this$getBlock, (String)"$this$getBlock");
        return BlockUtils.getBlock($this$getBlock);
    }

    @NotNull
    public static final WVec3 getVec(@NotNull WBlockPos $this$getVec) {
        Intrinsics.checkParameterIsNotNull((Object)$this$getVec, (String)"$this$getVec");
        return new WVec3((double)$this$getVec.getX() + 0.5, (double)$this$getVec.getY() + 0.5, (double)$this$getVec.getZ() + 0.5);
    }
}

