package wtf.diablo.events.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import wtf.diablo.events.Event;

@Getter@Setter
public class CollideEvent extends Event
{
    public final Block block;
    public AxisAlignedBB boundingBox;
    public final double x;
    public final double y;
    public final double z;

    public CollideEvent(final AxisAlignedBB bb, final Block block, final double x, final double y, final double z) {
        this.block = block;
        this.boundingBox = bb;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBoundingBox(final AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Block getBlock() {
        return this.block;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}

