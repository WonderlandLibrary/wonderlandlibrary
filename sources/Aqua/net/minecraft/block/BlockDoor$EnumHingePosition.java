package net.minecraft.block;

import net.minecraft.util.IStringSerializable;

public static enum BlockDoor.EnumHingePosition implements IStringSerializable
{
    LEFT,
    RIGHT;


    public String toString() {
        return this.getName();
    }

    public String getName() {
        return this == LEFT ? "left" : "right";
    }
}
