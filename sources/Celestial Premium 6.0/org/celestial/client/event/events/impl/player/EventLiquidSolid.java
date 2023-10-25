/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.player;

import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.events.callables.EventCancellable;

public class EventLiquidSolid
extends EventCancellable {
    private final BlockLiquid blockLiquid;
    private final BlockPos pos;

    public EventLiquidSolid(BlockLiquid blockLiquid, BlockPos pos) {
        this.blockLiquid = blockLiquid;
        this.pos = pos;
    }

    public BlockLiquid getBlock() {
        return this.blockLiquid;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}

