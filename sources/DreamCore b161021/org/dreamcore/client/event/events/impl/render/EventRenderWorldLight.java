package org.dreamcore.client.event.events.impl.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import org.dreamcore.client.event.events.callables.EventCancellable;

public class EventRenderWorldLight extends EventCancellable {

    private final EnumSkyBlock enumSkyBlock;
    private final BlockPos pos;

    public EventRenderWorldLight(EnumSkyBlock enumSkyBlock, BlockPos pos) {
        this.enumSkyBlock = enumSkyBlock;
        this.pos = pos;
    }

    public EnumSkyBlock getEnumSkyBlock() {
        return enumSkyBlock;
    }

    public BlockPos getPos() {
        return pos;
    }
}