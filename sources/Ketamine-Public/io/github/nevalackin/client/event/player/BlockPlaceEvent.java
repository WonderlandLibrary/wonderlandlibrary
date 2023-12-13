package io.github.nevalackin.client.event.player;

import io.github.nevalackin.client.event.CancellableEvent;
import io.github.nevalackin.client.event.Event;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public final class BlockPlaceEvent extends CancellableEvent {

    private final BlockPos blockPos;
    private final EnumFacing side;
    private final Vec3 hitVec;

    public BlockPlaceEvent(BlockPos blockPos, EnumFacing side, Vec3 hitVec) {
        this.blockPos = blockPos;
        this.side = side;
        this.hitVec = hitVec;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public EnumFacing getSide() {
        return side;
    }

    public Vec3 getHitVec() {
        return hitVec;
    }
}
