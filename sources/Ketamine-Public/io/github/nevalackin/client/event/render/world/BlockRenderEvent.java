package io.github.nevalackin.client.event.render.world;

import io.github.nevalackin.client.event.CancellableEvent;
import net.minecraft.block.Block;

public final class BlockRenderEvent extends CancellableEvent {

    private final Block block;

    public BlockRenderEvent(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
