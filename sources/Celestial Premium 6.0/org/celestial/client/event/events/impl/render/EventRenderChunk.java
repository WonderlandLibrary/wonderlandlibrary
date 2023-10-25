/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.render;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.events.Event;

public class EventRenderChunk
implements Event {
    public BlockPos BlockPos;
    public RenderChunk RenderChunk;

    public EventRenderChunk(RenderChunk renderChunk, BlockPos blockPos) {
        this.BlockPos = blockPos;
        this.RenderChunk = renderChunk;
    }
}

