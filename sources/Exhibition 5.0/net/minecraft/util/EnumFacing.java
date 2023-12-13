// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Random;
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
    private static final Map NAME_LOOKUP;
    private static final String __OBFID = "CL_00001201";
    private static final EnumFacing[] $VALUES;
    
    private EnumFacing(final String p_i46016_1_, final int p_i46016_2_, final int indexIn, final int oppositeIn, final int horizontalIndexIn, final String nameIn, final AxisDirection axisDirectionIn, final Axis axisIn, final Vec3i directionVecIn) {
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
        return getFront(this.opposite);
    }
    
    public EnumFacing rotateAround(final Axis axis) {
        switch (SwitchPlane.AXIS_LOOKUP[axis.ordinal()]) {
            case 1: {
                if (this != EnumFacing.WEST && this != EnumFacing.EAST) {
                    return this.rotateX();
                }
                return this;
            }
            case 2: {
                if (this != EnumFacing.UP && this != EnumFacing.DOWN) {
                    return this.rotateY();
                }
                return this;
            }
            case 3: {
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
        switch (SwitchPlane.FACING_LOOKUP[this.ordinal()]) {
            case 1: {
                return EnumFacing.EAST;
            }
            case 2: {
                return EnumFacing.SOUTH;
            }
            case 3: {
                return EnumFacing.WEST;
            }
            case 4: {
                return EnumFacing.NORTH;
            }
            default: {
                throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
            }
        }
    }
    
    private EnumFacing rotateX() {
        switch (SwitchPlane.FACING_LOOKUP[this.ordinal()]) {
            case 1: {
                return EnumFacing.DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get X-rotated facing of " + this);
            }
            case 3: {
                return EnumFacing.UP;
            }
            case 5: {
                return EnumFacing.NORTH;
            }
            case 6: {
                return EnumFacing.SOUTH;
            }
        }
    }
    
    private EnumFacing rotateZ() {
        switch (SwitchPlane.FACING_LOOKUP[this.ordinal()]) {
            case 2: {
                return EnumFacing.DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
            }
            case 4: {
                return EnumFacing.UP;
            }
            case 5: {
                return EnumFacing.EAST;
            }
            case 6: {
                return EnumFacing.WEST;
            }
        }
    }
    
    public EnumFacing rotateYCCW() {
        switch (SwitchPlane.FACING_LOOKUP[this.ordinal()]) {
            case 1: {
                return EnumFacing.WEST;
            }
            case 2: {
                return EnumFacing.NORTH;
            }
            case 3: {
                return EnumFacing.EAST;
            }
            case 4: {
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
    
    public static EnumFacing func_176737_a(final float x, final float y, final float z) {
        EnumFacing face = EnumFacing.NORTH;
        float min = Float.MIN_VALUE;
        for (final EnumFacing getFace : values()) {
            final float dist = x * getFace.directionVec.getX() + y * getFace.directionVec.getY() + z * getFace.directionVec.getZ();
            if (dist > min) {
                min = dist;
                face = getFace;
            }
        }
        return face;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public Vec3i getDirectionVec() {
        return this.directionVec;
    }
    
    static {
        VALUES = new EnumFacing[6];
        HORIZONTALS = new EnumFacing[4];
        NAME_LOOKUP = Maps.newHashMap();
        $VALUES = new EnumFacing[] { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST };
        for (final EnumFacing var4 : values()) {
            EnumFacing.VALUES[var4.index] = var4;
            if (var4.getAxis().isHorizontal()) {
                EnumFacing.HORIZONTALS[var4.horizontalIndex] = var4;
            }
            EnumFacing.NAME_LOOKUP.put(var4.getName2().toLowerCase(), var4);
        }
    }
    
    public enum Axis implements Predicate, IStringSerializable
    {
        X("X", 0, "X", 0, "x", Plane.HORIZONTAL), 
        Y("Y", 1, "Y", 1, "y", Plane.VERTICAL), 
        Z("Z", 2, "Z", 2, "z", Plane.HORIZONTAL);
        
        private static final Map NAME_LOOKUP;
        private final String name;
        private final Plane plane;
        private static final Axis[] $VALUES;
        private static final String __OBFID = "CL_00002321";
        private static final Axis[] $VALUES$;
        
        private Axis(final String p_i46398_1_, final int p_i46398_2_, final String p_i46015_1_, final int p_i46015_2_, final String name, final Plane plane) {
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
        
        public boolean apply(final EnumFacing facing) {
            return facing != null && facing.getAxis() == this;
        }
        
        public Plane getPlane() {
            return this.plane;
        }
        
        public String getName() {
            return this.name;
        }
        
        public boolean apply(final Object p_apply_1_) {
            return this.apply((EnumFacing)p_apply_1_);
        }
        
        static {
            NAME_LOOKUP = Maps.newHashMap();
            $VALUES = new Axis[] { Axis.X, Axis.Y, Axis.Z };
            $VALUES$ = new Axis[] { Axis.X, Axis.Y, Axis.Z };
            for (final Axis var4 : values()) {
                Axis.NAME_LOOKUP.put(var4.getName2().toLowerCase(), var4);
            }
        }
    }
    
    public enum AxisDirection
    {
        POSITIVE("POSITIVE", 0, "POSITIVE", 0, 1, "Towards positive"), 
        NEGATIVE("NEGATIVE", 1, "NEGATIVE", 1, -1, "Towards negative");
        
        private final int offset;
        private final String description;
        private static final AxisDirection[] $VALUES;
        private static final String __OBFID = "CL_00002320";
        private static final AxisDirection[] $VALUES$;
        
        private AxisDirection(final String p_i46399_1_, final int p_i46399_2_, final String p_i46014_1_, final int p_i46014_2_, final int offset, final String description) {
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
        
        static {
            $VALUES = new AxisDirection[] { AxisDirection.POSITIVE, AxisDirection.NEGATIVE };
            $VALUES$ = new AxisDirection[] { AxisDirection.POSITIVE, AxisDirection.NEGATIVE };
        }
    }
    
    public enum Plane implements Predicate, Iterable
    {
        HORIZONTAL("HORIZONTAL", 0, "HORIZONTAL", 0), 
        VERTICAL("VERTICAL", 1, "VERTICAL", 1);
        
        private static final Plane[] $VALUES;
        private static final String __OBFID = "CL_00002319";
        private static final Plane[] $VALUES$;
        
        private Plane(final String p_i46400_1_, final int p_i46400_2_, final String p_i46013_1_, final int p_i46013_2_) {
        }
        
        public EnumFacing[] facings() {
            switch (SwitchPlane.PLANE_LOOKUP[this.ordinal()]) {
                case 1: {
                    return new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST };
                }
                case 2: {
                    return new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN };
                }
                default: {
                    throw new Error("Someone's been tampering with the universe!");
                }
            }
        }
        
        public EnumFacing random(final Random rand) {
            final EnumFacing[] var2 = this.facings();
            return var2[rand.nextInt(var2.length)];
        }
        
        public boolean apply(final EnumFacing facing) {
            return facing != null && facing.getAxis().getPlane() == this;
        }
        
        public Iterator iterator() {
            return (Iterator)Iterators.forArray((Object[])this.facings());
        }
        
        public boolean apply(final Object p_apply_1_) {
            return this.apply((EnumFacing)p_apply_1_);
        }
        
        static {
            $VALUES = new Plane[] { Plane.HORIZONTAL, Plane.VERTICAL };
            $VALUES$ = new Plane[] { Plane.HORIZONTAL, Plane.VERTICAL };
        }
    }
    
    static final class SwitchPlane
    {
        static final int[] AXIS_LOOKUP;
        static final int[] FACING_LOOKUP;
        static final int[] PLANE_LOOKUP;
        private static final String __OBFID = "CL_00002322";
        
        static {
            PLANE_LOOKUP = new int[Plane.values().length];
            try {
                SwitchPlane.PLANE_LOOKUP[Plane.HORIZONTAL.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchPlane.PLANE_LOOKUP[Plane.VERTICAL.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            FACING_LOOKUP = new int[EnumFacing.values().length];
            try {
                SwitchPlane.FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchPlane.FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchPlane.FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchPlane.FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                SwitchPlane.FACING_LOOKUP[EnumFacing.UP.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                SwitchPlane.FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
            AXIS_LOOKUP = new int[Axis.values().length];
            try {
                SwitchPlane.AXIS_LOOKUP[Axis.X.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError9) {}
            try {
                SwitchPlane.AXIS_LOOKUP[Axis.Y.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError10) {}
            try {
                SwitchPlane.AXIS_LOOKUP[Axis.Z.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError11) {}
        }
    }
}
