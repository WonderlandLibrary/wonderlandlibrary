package org.luaj.vm2.customs;

import net.minecraft.util.math.AxisAlignedBB;

public class BoundingHook {

    private AxisAlignedBB bb;

    public BoundingHook(AxisAlignedBB bb) {
        this.bb = bb;
    }

    public double minX() {
        return bb.minX;
    }

    public double maxX() {
        return bb.maxX;
    }

    public double minY() {
        return bb.minY;
    }

    public double maxY() {
        return bb.maxY;
    }

    public double minZ() {
        return bb.minZ;
    }

    public double maxZ() {
        return bb.maxZ;
    }
}