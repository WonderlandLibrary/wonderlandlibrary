package dev.rise.event.impl.other;

import dev.rise.event.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;

@Getter
@Setter
@AllArgsConstructor
public final class BlockCollideEvent extends Event {
    private AxisAlignedBB collisionBoundingBox;
    private Block block;
    private int x, y, z;
}
