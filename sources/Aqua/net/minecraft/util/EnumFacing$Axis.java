package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

public static enum EnumFacing.Axis implements Predicate<EnumFacing>,
IStringSerializable
{
    X("x", EnumFacing.Plane.HORIZONTAL),
    Y("y", EnumFacing.Plane.VERTICAL),
    Z("z", EnumFacing.Plane.HORIZONTAL);

    private static final Map<String, EnumFacing.Axis> NAME_LOOKUP;
    private final String name;
    private final EnumFacing.Plane plane;

    private EnumFacing.Axis(String name, EnumFacing.Plane plane) {
        this.name = name;
        this.plane = plane;
    }

    public static EnumFacing.Axis byName(String name) {
        return name == null ? null : (EnumFacing.Axis)((Object)NAME_LOOKUP.get((Object)name.toLowerCase()));
    }

    public String getName2() {
        return this.name;
    }

    public boolean isVertical() {
        return this.plane == EnumFacing.Plane.VERTICAL;
    }

    public boolean isHorizontal() {
        return this.plane == EnumFacing.Plane.HORIZONTAL;
    }

    public String toString() {
        return this.name;
    }

    public boolean apply(EnumFacing p_apply_1_) {
        return p_apply_1_ != null && p_apply_1_.getAxis() == this;
    }

    public EnumFacing.Plane getPlane() {
        return this.plane;
    }

    public String getName() {
        return this.name;
    }

    static {
        NAME_LOOKUP = Maps.newHashMap();
        for (EnumFacing.Axis enumfacing$axis : EnumFacing.Axis.values()) {
            NAME_LOOKUP.put((Object)enumfacing$axis.getName2().toLowerCase(), (Object)enumfacing$axis);
        }
    }
}
