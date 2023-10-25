package net.minecraft.util;

public static enum EnumFacing.AxisDirection {
    POSITIVE(1, "Towards positive"),
    NEGATIVE(-1, "Towards negative");

    private final int offset;
    private final String description;

    private EnumFacing.AxisDirection(int offset, String description) {
        this.offset = offset;
        this.description = description;
    }

    public int getOffset() {
        return this.offset;
    }

    public String toString() {
        return this.description;
    }
}
