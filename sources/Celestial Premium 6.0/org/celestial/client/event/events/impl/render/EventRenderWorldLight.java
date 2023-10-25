/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import org.celestial.client.event.events.callables.EventCancellable;

public class EventRenderWorldLight
extends EventCancellable {
    private final EnumSkyBlock enumSkyBlock;
    private final BlockPos pos;

    public EventRenderWorldLight(EnumSkyBlock enumSkyBlock, BlockPos pos) {
        this.enumSkyBlock = enumSkyBlock;
        this.pos = pos;
    }

    public EnumSkyBlock getEnumSkyBlock() {
        return this.enumSkyBlock;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}

