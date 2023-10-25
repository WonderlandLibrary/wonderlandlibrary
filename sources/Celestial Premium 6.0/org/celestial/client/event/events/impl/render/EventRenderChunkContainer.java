/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.render;

import net.minecraft.client.renderer.chunk.RenderChunk;
import org.celestial.client.event.events.Event;

public class EventRenderChunkContainer
implements Event {
    public RenderChunk RenderChunk;

    public EventRenderChunkContainer(RenderChunk renderChunk) {
        this.RenderChunk = renderChunk;
    }
}

