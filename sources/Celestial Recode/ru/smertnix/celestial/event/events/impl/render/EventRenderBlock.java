package ru.smertnix.celestial.event.events.impl.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import ru.smertnix.celestial.event.events.Event;
import ru.smertnix.celestial.event.events.callables.EventCancellable;

import java.awt.*;

public class EventRenderBlock implements Event {

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
        return state;
    }

    public BufferBuilder getBufferBuilder() {
        return bufferBuilder;
    }

    public BlockPos getPos() {
        return pos;
    }

    public IBlockAccess getAccess() {
        return access;
    }
}