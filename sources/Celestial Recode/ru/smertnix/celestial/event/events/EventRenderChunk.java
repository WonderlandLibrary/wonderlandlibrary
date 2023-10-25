package ru.smertnix.celestial.event.events;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.event.events.callables.EventCancellable;

public class EventRenderChunk extends EventCancellable {

    public BlockPos blockPos;
    public RenderChunk renderChunk;

    public EventRenderChunk(RenderChunk renderChunk, BlockPos blockPos) {
        this.blockPos = blockPos;
        this.renderChunk = renderChunk;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public RenderChunk getRenderChunk() {
        return renderChunk;
    }

    public void setRenderChunk(RenderChunk renderChunk) {
        this.renderChunk = renderChunk;
    }
}
