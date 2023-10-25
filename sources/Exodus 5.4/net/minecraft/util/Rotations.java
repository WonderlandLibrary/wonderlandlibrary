/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

public class Rotations {
    protected final float z;
    protected final float x;
    protected final float y;

    public boolean equals(Object object) {
        if (!(object instanceof Rotations)) {
            return false;
        }
        Rotations rotations = (Rotations)object;
        return this.x == rotations.x && this.y == rotations.y && this.z == rotations.z;
    }

    public float getY() {
        return this.y;
    }

    public float getX() {
        return this.x;
    }

    public Rotations(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
    }

    public Rotations(NBTTagList nBTTagList) {
        this.x = nBTTagList.getFloatAt(0);
        this.y = nBTTagList.getFloatAt(1);
        this.z = nBTTagList.getFloatAt(2);
    }

    public NBTTagList writeToNBT() {
        NBTTagList nBTTagList = new NBTTagList();
        nBTTagList.appendTag(new NBTTagFloat(this.x));
        nBTTagList.appendTag(new NBTTagFloat(this.y));
        nBTTagList.appendTag(new NBTTagFloat(this.z));
        return nBTTagList;
    }

    public float getZ() {
        return this.z;
    }
}

