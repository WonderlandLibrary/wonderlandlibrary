package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

public enum EnumFacing implements IStringSerializable
{
    DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)),
    UP(1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)),
    NORTH(2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)),
    SOUTH(3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)),
    WEST(4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)),
    EAST(5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0));

    private final int index;
    private final int opposite;
    private final int horizontalIndex;
    private final String name;
    private final Axis axis;
    private final AxisDirection axisDirection;
    private final Vec3i directionVec;
    public static final EnumFacing[] VALUES;
    private static final EnumFacing[] HORIZONTALS;
    private static final Map<String, EnumFacing> NAME_LOOKUP;

    private EnumFacing(int indexIn, int oppositeIn, int horizontalIndexIn, String nameIn, AxisDirection axisDirectionIn, Axis axisIn, Vec3i directionVecIn) {
        this.index = indexIn;
        this.horizontalIndex = horizontalIndexIn;
        this.opposite = oppositeIn;
        this.name = nameIn;
        this.axis = axisIn;
        this.axisDirection = axisDirectionIn;
        this.directionVec = directionVecIn;
    }

    public int getIndex() {
        return this.index;
    }

    public int getHorizontalIndex() {
        return this.horizontalIndex;
    }

    public AxisDirection getAxisDirection() {
        return this.axisDirection;
    }

    public EnumFacing getOpposite() {
        return VALUES[this.opposite];
    }

    public EnumFacing rotateAround(Axis axis) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing$Axis[axis.ordinal()]) {
            case 1: {
                if (this != WEST && this != EAST) {
                    return this.rotateX();
                }
                return this;
            }
            case 2: {
                if (this != UP && this != DOWN) {
                    return this.rotateY();
                }
                return this;
            }
            case 3: {
                if (this != NORTH && this != SOUTH) {
                    return this.rotateZ();
                }
                return this;
            }
        }
        throw new IllegalStateException("Unable to get CW facing for axis " + axis);
    }

    public EnumFacing rotateY() {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[this.ordinal()]) {
            case 1: {
                return EAST;
            }
            case 2: {
                return SOUTH;
            }
            case 3: {
                return WEST;
            }
            case 4: {
                return NORTH;
            }
        }
        throw new IllegalStateException("Unable to get Y-rotated facing of " + (Object)((Object)this));
    }

    private EnumFacing rotateX() {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[this.ordinal()]) {
            case 1: {
                return DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get X-rotated facing of " + (Object)((Object)this));
            }
            case 3: {
                return UP;
            }
            case 5: {
                return NORTH;
            }
            case 6: 
        }
        return SOUTH;
    }

    private EnumFacing rotateZ() {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[this.ordinal()]) {
            case 2: {
                return DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get Z-rotated facing of " + (Object)((Object)this));
            }
            case 4: {
                return UP;
            }
            case 5: {
                return EAST;
            }
            case 6: 
        }
        return WEST;
    }

    public EnumFacing rotateYCCW() {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[this.ordinal()]) {
            case 1: {
                return WEST;
            }
            case 2: {
                return NORTH;
            }
            case 3: {
                return EAST;
            }
            case 4: {
                return SOUTH;
            }
        }
        throw new IllegalStateException("Unable to get CCW facing of " + (Object)((Object)this));
    }

    public int getFrontOffsetX() {
        return this.axis == Axis.X ? this.axisDirection.getOffset() : 0;
    }

    public int getFrontOffsetY() {
        return this.axis == Axis.Y ? this.axisDirection.getOffset() : 0;
    }

    public int getFrontOffsetZ() {
        return this.axis == Axis.Z ? this.axisDirection.getOffset() : 0;
    }

    public String getName2() {
        return this.name;
    }

    public Axis getAxis() {
        return this.axis;
    }

    public static EnumFacing byName(String name) {
        return name == null ? null : (EnumFacing)((Object)NAME_LOOKUP.get((Object)name.toLowerCase()));
    }

    public static EnumFacing getFront(int index) {
        return VALUES[MathHelper.abs_int((int)(index % VALUES.length))];
    }

    public static EnumFacing getHorizontal(int p_176731_0_) {
        return HORIZONTALS[MathHelper.abs_int((int)(p_176731_0_ % HORIZONTALS.length))];
    }

    public static EnumFacing fromAngle(double angle) {
        return EnumFacing.getHorizontal(MathHelper.floor_double((double)(angle / 90.0 + 0.5)) & 3);
    }

    public static EnumFacing random(Random rand) {
        return EnumFacing.values()[rand.nextInt(EnumFacing.values().length)];
    }

    public static EnumFacing getFacingFromVector(float p_176737_0_, float p_176737_1_, float p_176737_2_) {
        EnumFacing enumfacing = NORTH;
        float f = Float.MIN_VALUE;
        for (EnumFacing enumfacing1 : EnumFacing.values()) {
            float f1 = p_176737_0_ * (float)enumfacing1.directionVec.getX() + p_176737_1_ * (float)enumfacing1.directionVec.getY() + p_176737_2_ * (float)enumfacing1.directionVec.getZ();
            if (!(f1 > f)) continue;
            f = f1;
            enumfacing = enumfacing1;
        }
        return enumfacing;
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public static EnumFacing getFacingFromAxis(AxisDirection p_181076_0_, Axis p_181076_1_) {
        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (enumfacing.getAxisDirection() != p_181076_0_ || enumfacing.getAxis() != p_181076_1_) continue;
            return enumfacing;
        }
        throw new IllegalArgumentException("No such direction: " + p_181076_0_ + " " + p_181076_1_);
    }

    public Vec3i getDirectionVec() {
        return this.directionVec;
    }

    static {
        VALUES = new EnumFacing[6];
        HORIZONTALS = new EnumFacing[4];
        NAME_LOOKUP = Maps.newHashMap();
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        for (int i = 0; i < n; ++i) {
            EnumFacing enumfacing;
            EnumFacing.VALUES[enumfacing.index] = enumfacing = enumFacingArray[i];
            if (enumfacing.getAxis().isHorizontal()) {
                EnumFacing.HORIZONTALS[enumfacing.horizontalIndex] = enumfacing;
            }
            NAME_LOOKUP.put((Object)enumfacing.getName2().toLowerCase(), (Object)enumfacing);
        }
    }
}
