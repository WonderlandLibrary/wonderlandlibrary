/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.celestial.client.event.events.callables.EventCancellable;

public class EventRenderBlock
extends EventCancellable {
    private final IBlockState state;
    private final BlockPos pos;
    private final IBlockAccess access;
    private final BufferBuilder bufferBuilder;

    public EventRenderBlock(IBlockState state, BlockPos pos, IBlockAccess access, BufferBuilder bufferBuilder) {
        this.state = state;
        this.pos = pos;
        this.access = access;
        this.bufferBuilder = bufferBuilder;
    }

    public IBlockState getState() {
        return this.state;
    }

    public BufferBuilder getBufferBuilder() {
        return this.bufferBuilder;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public IBlockAccess getAccess() {
        return this.access;
    }
}

