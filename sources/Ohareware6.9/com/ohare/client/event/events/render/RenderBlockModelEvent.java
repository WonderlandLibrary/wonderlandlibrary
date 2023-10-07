package com.ohare.client.event.events.render;

import com.ohare.client.event.Event;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

/**
 * made by Xen for OhareWare
 *
 * @since 6/15/2019
 **/
public class RenderBlockModelEvent extends Event {
    private final Block block;

    private final BlockPos pos;

    public RenderBlockModelEvent(Block block, BlockPos pos) {
        this.block = block;
        this.pos = pos;
    }

    public Block getBlock() {
        return block;
    }

    public BlockPos getPos() {
        return pos;
    }
}