// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.enums;

import net.minecraft.util.EnumFacing;

public final class SwitchEnumFacing
{
    public static final int[] field_178907_a;
    
    static {
        field_178907_a = new int[EnumFacing.values().length];
        try {
            SwitchEnumFacing.field_178907_a[EnumFacing.NORTH.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            SwitchEnumFacing.field_178907_a[EnumFacing.SOUTH.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError2) {}
        try {
            SwitchEnumFacing.field_178907_a[EnumFacing.WEST.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError3) {}
        try {
            SwitchEnumFacing.field_178907_a[EnumFacing.EAST.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError4) {}
    }
}
