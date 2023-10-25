package net.minecraft.world;

import net.minecraft.util.BlockPos;

public class Teleporter.PortalPosition
extends BlockPos {
    public long lastUpdateTime;

    public Teleporter.PortalPosition(BlockPos pos, long lastUpdate) {
        super(pos.getX(), pos.getY(), pos.getZ());
        this.lastUpdateTime = lastUpdate;
    }
}
