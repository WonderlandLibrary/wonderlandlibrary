package today.flux.addon.api.utils;

import com.soterdev.SoterObfuscator;
import net.minecraft.util.EnumFacing;

public enum Facing {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static Facing getFacing(EnumFacing facing) {
        if (facing == EnumFacing.UP)
            return UP;
        else if (facing == EnumFacing.DOWN)
            return DOWN;
        else if (facing == EnumFacing.NORTH)
            return NORTH;
        else if (facing == EnumFacing.SOUTH)
            return SOUTH;
        else if (facing == EnumFacing.WEST)
            return WEST;
        else
            return EAST;
    }

    
    public EnumFacing getFacing() {
        if (this == Facing.UP)
            return EnumFacing.UP;
        else if (this == Facing.DOWN)
            return EnumFacing.DOWN;
        else if (this == Facing.NORTH)
            return EnumFacing.NORTH;
        else if (this == Facing.SOUTH)
            return EnumFacing.SOUTH;
        else if (this == Facing.WEST)
            return EnumFacing.WEST;
        else
            return EnumFacing.EAST;
    }
}
