package org.dreamcore.client.event.events.impl.render;

import net.minecraft.client.renderer.chunk.RenderChunk;
import org.dreamcore.client.event.events.callables.EventCancellable;

public class EventRenderChunkContainer extends EventCancellable {

    public RenderChunk renderChunk;

    public EventRenderChunkContainer(RenderChunk renderChunk) {
        this.renderChunk = renderChunk;
    }

    public RenderChunk getRenderChunk() {
        return renderChunk;
    }

    public void setRenderChunk(RenderChunk renderChunk) {
        this.renderChunk = renderChunk;
    }
}
