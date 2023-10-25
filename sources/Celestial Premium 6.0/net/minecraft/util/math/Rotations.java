/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.math;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

public class Rotations {
    protected final float x;
    protected final float y;
    protected final float z;

    public Rotations(float x, float y, float z) {
        this.x = !Float.isInfinite(x) && !Float.isNaN(x) ? x % 360.0f : 0.0f;
        this.y = !Float.isInfinite(y) && !Float.isNaN(y) ? y % 360.0f : 0.0f;
        this.z = !Float.isInfinite(z) && !Float.isNaN(z) ? z % 360.0f : 0.0f;
    }

    public Rotations(NBTTagList nbt) {
        this(nbt.getFloatAt(0), nbt.getFloatAt(1), nbt.getFloatAt(2));
    }

    public NBTTagList writeToNBT() {
        NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.appendTag(new NBTTagFloat(this.x));
        nbttaglist.appendTag(new NBTTagFloat(this.y));
        nbttaglist.appendTag(new NBTTagFloat(this.z));
        return nbttaglist;
    }

    public boolean equals(Object p_equals_1_) {
        if (!(p_equals_1_ instanceof Rotations)) {
            return false;
        }
        Rotations rotations = (Rotations)p_equals_1_;
        return this.x == rotations.x && this.y == rotations.y && this.z == rotations.z;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }
}

