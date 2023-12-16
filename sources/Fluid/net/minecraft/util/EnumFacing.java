// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import com.google.common.base.Predicate;
import java.util.Random;
import com.google.common.collect.Maps;
import java.util.Map;

public enum EnumFacing implements IStringSerializable
{
    DOWN("DOWN", 0, 0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)), 
    UP("UP", 1, 1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)), 
    NORTH("NORTH", 2, 2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)), 
    SOUTH("SOUTH", 3, 3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)), 
    WEST("WEST", 4, 4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)), 
    EAST("EAST", 5, 5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0));
    
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
    
    static {
        VALUES = new EnumFacing[6];
        HORIZONTALS = new EnumFacing[4];
        NAME_LOOKUP = Maps.newHashMap();
        EnumFacing[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            EnumFacing.VALUES[enumfacing.index] = enumfacing;
            if (enumfacing.getAxis().isHorizontal()) {
                EnumFacing.HORIZONTALS[enumfacing.horizontalIndex] = enumfacing;
            }
            EnumFacing.NAME_LOOKUP.put(enumfacing.getName2().toLowerCase(), enumfacing);
        }
    }
    
    private EnumFacing(final String s, final int n, final int indexIn, final int oppositeIn, final int horizontalIndexIn, final String nameIn, final AxisDirection axisDirectionIn, final Axis axisIn, final Vec3i directionVecIn) {
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
        return EnumFacing.VALUES[this.opposite];
    }
    
    public EnumFacing rotateAround(final Axis axis) {
        switch (axis) {
            case X: {
                if (this != EnumFacing.WEST && this != EnumFacing.EAST) {
                    return this.rotateX();
                }
                return this;
            }
            case Y: {
                if (this != EnumFacing.UP && this != EnumFacing.DOWN) {
                    return this.rotateY();
                }
                return this;
            }
            case Z: {
                if (this != EnumFacing.NORTH && this != EnumFacing.SOUTH) {
                    return this.rotateZ();
                }
                return this;
            }
            default: {
                throw new IllegalStateException("Unable to get CW facing for axis " + axis);
            }
        }
    }
    
    public EnumFacing rotateY() {
        switch (this) {
            case NORTH: {
                return EnumFacing.EAST;
            }
            case EAST: {
                return EnumFacing.SOUTH;
            }
            case SOUTH: {
                return EnumFacing.WEST;
            }
            case WEST: {
                return EnumFacing.NORTH;
            }
            default: {
                throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
            }
        }
    }
    
    private EnumFacing rotateX() {
        switch (this) {
            case NORTH: {
                return EnumFacing.DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get X-rotated facing of " + this);
            }
            case SOUTH: {
                return EnumFacing.UP;
            }
            case UP: {
                return EnumFacing.NORTH;
            }
            case DOWN: {
                return EnumFacing.SOUTH;
            }
        }
    }
    
    private EnumFacing rotateZ() {
        switch (this) {
            case EAST: {
                return EnumFacing.DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
            }
            case WEST: {
                return EnumFacing.UP;
            }
            case UP: {
                return EnumFacing.EAST;
            }
            case DOWN: {
                return EnumFacing.WEST;
            }
        }
    }
    
    public EnumFacing rotateYCCW() {
        switch (this) {
            case NORTH: {
                return EnumFacing.WEST;
            }
            case EAST: {
                return EnumFacing.NORTH;
            }
            case SOUTH: {
                return EnumFacing.EAST;
            }
            case WEST: {
                return EnumFacing.SOUTH;
            }
            default: {
                throw new IllegalStateException("Unable to get CCW facing of " + this);
            }
        }
    }
    
    public int getFrontOffsetX() {
        return (this.axis == Axis.X) ? this.axisDirection.getOffset() : 0;
    }
    
    public int getFrontOffsetY() {
        return (this.axis == Axis.Y) ? this.axisDirection.getOffset() : 0;
    }
    
    public int getFrontOffsetZ() {
        return (this.axis == Axis.Z) ? this.axisDirection.getOffset() : 0;
    }
    
    public String getName2() {
        return this.name;
    }
    
    public Axis getAxis() {
        return this.axis;
    }
    
    public static EnumFacing byName(final String name) {
        return (name == null) ? null : EnumFacing.NAME_LOOKUP.get(name.toLowerCase());
    }
    
    public static EnumFacing getFront(final int index) {
        return EnumFacing.VALUES[MathHelper.abs_int(index % EnumFacing.VALUES.length)];
    }
    
    public static EnumFacing getHorizontal(final int p_176731_0_) {
        return EnumFacing.HORIZONTALS[MathHelper.abs_int(p_176731_0_ % EnumFacing.HORIZONTALS.length)];
    }
    
    public static EnumFacing fromAngle(final double angle) {
        return getHorizontal(MathHelper.floor_double(angle / 90.0 + 0.5) & 0x3);
    }
    
    public static EnumFacing random(final Random rand) {
        return values()[rand.nextInt(values().length)];
    }
    
    public static EnumFacing getFacingFromVector(final float p_176737_0_, final float p_176737_1_, final float p_176737_2_) {
        EnumFacing enumfacing = EnumFacing.NORTH;
        float f = Float.MIN_VALUE;
        EnumFacing[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing2 = values[i];
            final float f2 = p_176737_0_ * enumfacing2.directionVec.getX() + p_176737_1_ * enumfacing2.directionVec.getY() + p_176737_2_ * enumfacing2.directionVec.getZ();
            if (f2 > f) {
                f = f2;
                enumfacing = enumfacing2;
            }
        }
        return enumfacing;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public static EnumFacing getFacingFromAxis(final AxisDirection p_181076_0_, final Axis p_181076_1_) {
        EnumFacing[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            if (enumfacing.getAxisDirection() == p_181076_0_ && enumfacing.getAxis() == p_181076_1_) {
                return enumfacing;
            }
        }
        throw new IllegalArgumentException("No such direction: " + p_181076_0_ + " " + p_181076_1_);
    }
    
    public Vec3i getDirectionVec() {
        return this.directionVec;
    }
    
    public enum Axis implements Predicate<EnumFacing>, IStringSerializable
    {
        X("X", 0, "x", Plane.HORIZONTAL), 
        Y("Y", 1, "y", Plane.VERTICAL), 
        Z("Z", 2, "z", Plane.HORIZONTAL);
        
        private static final Map<String, Axis> NAME_LOOKUP;
        private final String name;
        private final Plane plane;
        
        static {
            NAME_LOOKUP = Maps.newHashMap();
            Axis[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Axis enumfacing$axis = values[i];
                Axis.NAME_LOOKUP.put(enumfacing$axis.getName2().toLowerCase(), enumfacing$axis);
            }
        }
        
        private Axis(final String s, final int n, final String name, final Plane plane) {
            this.name = name;
            this.plane = plane;
        }
        
        public static Axis byName(final String name) {
            return (name == null) ? null : Axis.NAME_LOOKUP.get(name.toLowerCase());
        }
        
        public String getName2() {
            return this.name;
        }
        
        public boolean isVertical() {
            return this.plane == Plane.VERTICAL;
        }
        
        public boolean isHorizontal() {
            return this.plane == Plane.HORIZONTAL;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public boolean apply(final EnumFacing p_apply_1_) {
            return p_apply_1_ != null && p_apply_1_.getAxis() == this;
        }
        
        public Plane getPlane() {
            return this.plane;
        }
        
        public String getName() {
            return this.name;
        }
    }
    
    public enum Plane implements Predicate<EnumFacing>, Iterable<EnumFacing>
    {
        HORIZONTAL("HORIZONTAL", 0), 
        VERTICAL("VERTICAL", 1);
        
        private Plane(final String s, final int n) {
        }
        
        public EnumFacing[] facings() {
            switch (this) {
                case HORIZONTAL: {
                    return new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST };
                }
                case VERTICAL: {
                    return new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN };
                }
                default: {
                    throw new Error("Someone's been tampering with the universe!");
                }
            }
        }
        
        public EnumFacing random(final Random rand) {
            final EnumFacing[] aenumfacing = this.facings();
            return aenumfacing[rand.nextInt(aenumfacing.length)];
        }
        
        public boolean apply(final EnumFacing p_apply_1_) {
            return p_apply_1_ != null && p_apply_1_.getAxis().getPlane() == this;
        }
        
        public Iterator<EnumFacing> iterator() {
            return (Iterator<EnumFacing>)Iterators.forArray((Object[])this.facings());
        }
    }
    
    public enum AxisDirection
    {
        POSITIVE("POSITIVE", 0, 1, "Towards positive"), 
        NEGATIVE("NEGATIVE", 1, -1, "Towards negative");
        
        private final int offset;
        private final String description;
        
        private AxisDirection(final String s, final int n, final int offset, final String description) {
            this.offset = offset;
            this.description = description;
        }
        
        public int getOffset() {
            return this.offset;
        }
        
        @Override
        public String toString() {
            return this.description;
        }
    }
}
