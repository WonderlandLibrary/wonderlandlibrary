package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\b\n\b\n\b\u000020B00\b0¢\bR0¢\b\n\u0000\b\t\nR0X¢\n\u0000\b\f\"\b\rR0¢\b\n\u0000\bR0¢\b\n\u0000\bR0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/BlockBBEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "blockPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "block", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "boundingBox", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;)V", "getBlock", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "getBoundingBox", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;", "setBoundingBox", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/IAxisAlignedBB;)V", "x", "", "getX", "()I", "y", "getY", "z", "getZ", "Pride"})
public final class BlockBBEvent
extends Event {
    private final int x;
    private final int y;
    private final int z;
    @NotNull
    private final IBlock block;
    @Nullable
    private IAxisAlignedBB boundingBox;

    public final int getX() {
        return this.x;
    }

    public final int getY() {
        return this.y;
    }

    public final int getZ() {
        return this.z;
    }

    @NotNull
    public final IBlock getBlock() {
        return this.block;
    }

    @Nullable
    public final IAxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public final void setBoundingBox(@Nullable IAxisAlignedBB iAxisAlignedBB) {
        this.boundingBox = iAxisAlignedBB;
    }

    public BlockBBEvent(@NotNull WBlockPos blockPos, @NotNull IBlock block, @Nullable IAxisAlignedBB boundingBox) {
        Intrinsics.checkParameterIsNotNull(blockPos, "blockPos");
        Intrinsics.checkParameterIsNotNull(block, "block");
        this.block = block;
        this.boundingBox = boundingBox;
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
    }
}
