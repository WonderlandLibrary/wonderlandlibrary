/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Maps
 */
package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
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

    private final int horizontalIndex;
    private static final EnumFacing[] VALUES = new EnumFacing[6];
    private final int index;
    private final String name;
    private final int opposite;
    private final Vec3i directionVec;
    private static final EnumFacing[] HORIZONTALS;
    private static final Map<String, EnumFacing> NAME_LOOKUP;
    private final AxisDirection axisDirection;
    private final Axis axis;

    public EnumFacing rotateAround(Axis axis) {
        switch (axis) {
            case X: {
                if (this != WEST && this != EAST) {
                    return this.rotateX();
                }
                return this;
            }
            case Y: {
                if (this != UP && this != DOWN) {
                    return this.rotateY();
                }
                return this;
            }
            case Z: {
                if (this != NORTH && this != SOUTH) {
                    return this.rotateZ();
                }
                return this;
            }
        }
        throw new IllegalStateException("Unable to get CW facing for axis " + axis);
    }

    private EnumFacing rotateZ() {
        switch (this) {
            case EAST: {
                return DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
            }
            case WEST: {
                return UP;
            }
            case UP: {
                return EAST;
            }
            case DOWN: 
        }
        return WEST;
    }

    public String getName2() {
        return this.name;
    }

    private EnumFacing rotateX() {
        switch (this) {
            case NORTH: {
                return DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get X-rotated facing of " + this);
            }
            case SOUTH: {
                return UP;
            }
            case UP: {
                return NORTH;
            }
            case DOWN: 
        }
        return SOUTH;
    }

    public static EnumFacing byName(String string) {
        return string == null ? null : NAME_LOOKUP.get(string.toLowerCase());
    }

    public EnumFacing rotateY() {
        switch (this) {
            case NORTH: {
                return EAST;
            }
            case EAST: {
                return SOUTH;
            }
            case SOUTH: {
                return WEST;
            }
            case WEST: {
                return NORTH;
            }
        }
        throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
    }

    public int getFrontOffsetY() {
        return this.axis == Axis.Y ? this.axisDirection.getOffset() : 0;
    }

    public static EnumFacing getFront(int n) {
        return VALUES[MathHelper.abs_int(n % VALUES.length)];
    }

    public int getFrontOffsetZ() {
        return this.axis == Axis.Z ? this.axisDirection.getOffset() : 0;
    }

    public static EnumFacing random(Random random) {
        return EnumFacing.values()[random.nextInt(EnumFacing.values().length)];
    }

    public Vec3i getDirectionVec() {
        return this.directionVec;
    }

    public AxisDirection getAxisDirection() {
        return this.axisDirection;
    }

    public int getIndex() {
        return this.index;
    }

    public EnumFacing getOpposite() {
        return EnumFacing.getFront(this.opposite);
    }

    public static EnumFacing func_181076_a(AxisDirection axisDirection, Axis axis) {
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumFacing = enumFacingArray[n2];
            if (enumFacing.getAxisDirection() == axisDirection && enumFacing.getAxis() == axis) {
                return enumFacing;
            }
            ++n2;
        }
        throw new IllegalArgumentException("No such direction: " + (Object)((Object)axisDirection) + " " + axis);
    }

    public int getHorizontalIndex() {
        return this.horizontalIndex;
    }

    public String toString() {
        return this.name;
    }

    static {
        HORIZONTALS = new EnumFacing[4];
        NAME_LOOKUP = Maps.newHashMap();
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumFacing;
            EnumFacing.VALUES[enumFacing.index] = enumFacing = enumFacingArray[n2];
            if (enumFacing.getAxis().isHorizontal()) {
                EnumFacing.HORIZONTALS[enumFacing.horizontalIndex] = enumFacing;
            }
            NAME_LOOKUP.put(enumFacing.getName2().toLowerCase(), enumFacing);
            ++n2;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    public static EnumFacing getHorizontal(int n) {
        return HORIZONTALS[MathHelper.abs_int(n % HORIZONTALS.length)];
    }

    private EnumFacing(int n2, int n3, int n4, String string2, AxisDirection axisDirection, Axis axis, Vec3i vec3i) {
        this.index = n2;
        this.horizontalIndex = n4;
        this.opposite = n3;
        this.name = string2;
        this.axis = axis;
        this.axisDirection = axisDirection;
        this.directionVec = vec3i;
    }

    public Axis getAxis() {
        return this.axis;
    }

    public EnumFacing rotateYCCW() {
        switch (this) {
            case NORTH: {
                return WEST;
            }
            case EAST: {
                return NORTH;
            }
            case SOUTH: {
                return EAST;
            }
            case WEST: {
                return SOUTH;
            }
        }
        throw new IllegalStateException("Unable to get CCW facing of " + this);
    }

    public int getFrontOffsetX() {
        return this.axis == Axis.X ? this.axisDirection.getOffset() : 0;
    }

    public static EnumFacing getFacingFromVector(float f, float f2, float f3) {
        EnumFacing enumFacing = NORTH;
        float f4 = Float.MIN_VALUE;
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumFacing2 = enumFacingArray[n2];
            float f5 = f * (float)enumFacing2.directionVec.getX() + f2 * (float)enumFacing2.directionVec.getY() + f3 * (float)enumFacing2.directionVec.getZ();
            if (f5 > f4) {
                f4 = f5;
                enumFacing = enumFacing2;
            }
            ++n2;
        }
        return enumFacing;
    }

    public static EnumFacing fromAngle(double d) {
        return EnumFacing.getHorizontal(MathHelper.floor_double(d / 90.0 + 0.5) & 3);
    }

    public static enum AxisDirection {
        POSITIVE(1, "Towards positive"),
        NEGATIVE(-1, "Towards negative");

        private final int offset;
        private final String description;

        public String toString() {
            return this.description;
        }

        public int getOffset() {
            return this.offset;
        }

        private AxisDirection(int n2, String string2) {
            this.offset = n2;
            this.description = string2;
        }
    }

    public static enum Plane implements Predicate<EnumFacing>,
    Iterable<EnumFacing>
    {
        HORIZONTAL,
        VERTICAL;


        public EnumFacing[] facings() {
            switch (this) {
                case HORIZONTAL: {
                    return new EnumFacing[]{NORTH, EAST, SOUTH, WEST};
                }
                case VERTICAL: {
                    return new EnumFacing[]{UP, DOWN};
                }
            }
            throw new Error("Someone's been tampering with the universe!");
        }

        @Override
        public Iterator<EnumFacing> iterator() {
            return Iterators.forArray((Object[])this.facings());
        }

        public EnumFacing random(Random random) {
            EnumFacing[] enumFacingArray = this.facings();
            return enumFacingArray[random.nextInt(enumFacingArray.length)];
        }

        public boolean apply(EnumFacing enumFacing) {
            return enumFacing != null && enumFacing.getAxis().getPlane() == this;
        }
    }

    public static enum Axis implements Predicate<EnumFacing>,
    IStringSerializable
    {
        X("x", Plane.HORIZONTAL),
        Y("y", Plane.VERTICAL),
        Z("z", Plane.HORIZONTAL);

        private final String name;
        private final Plane plane;
        private static final Map<String, Axis> NAME_LOOKUP;

        public boolean apply(EnumFacing enumFacing) {
            return enumFacing != null && enumFacing.getAxis() == this;
        }

        public static Axis byName(String string) {
            return string == null ? null : NAME_LOOKUP.get(string.toLowerCase());
        }

        static {
            NAME_LOOKUP = Maps.newHashMap();
            Axis[] axisArray = Axis.values();
            int n = axisArray.length;
            int n2 = 0;
            while (n2 < n) {
                Axis axis = axisArray[n2];
                NAME_LOOKUP.put(axis.getName2().toLowerCase(), axis);
                ++n2;
            }
        }

        public Plane getPlane() {
            return this.plane;
        }

        public String toString() {
            return this.name;
        }

        public boolean isVertical() {
            return this.plane == Plane.VERTICAL;
        }

        private Axis(String string2, Plane plane) {
            this.name = string2;
            this.plane = plane;
        }

        public boolean isHorizontal() {
            return this.plane == Plane.HORIZONTAL;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public String getName2() {
            return this.name;
        }
    }
}

