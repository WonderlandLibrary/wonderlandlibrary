package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class RenderBlockEvent extends Event {
    public int x;
    public int y;
    public int z;
    public Block block;
    public BlockPos pos;

    public RenderBlockEvent(int x, int y, int z, Block block , BlockPos pos) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.pos = pos;
    }
}
