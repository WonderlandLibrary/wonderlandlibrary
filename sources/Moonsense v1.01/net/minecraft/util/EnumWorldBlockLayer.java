// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public enum EnumWorldBlockLayer
{
    SOLID("SOLID", 0, "SOLID", 0, "Solid"), 
    CUTOUT_MIPPED("CUTOUT_MIPPED", 1, "CUTOUT_MIPPED", 1, "Mipped Cutout"), 
    CUTOUT("CUTOUT", 2, "CUTOUT", 2, "Cutout"), 
    TRANSLUCENT("TRANSLUCENT", 3, "TRANSLUCENT", 3, "Translucent");
    
    private final String field_180338_e;
    private static final EnumWorldBlockLayer[] $VALUES;
    private static final String __OBFID = "CL_00002152";
    
    static {
        $VALUES = new EnumWorldBlockLayer[] { EnumWorldBlockLayer.SOLID, EnumWorldBlockLayer.CUTOUT_MIPPED, EnumWorldBlockLayer.CUTOUT, EnumWorldBlockLayer.TRANSLUCENT };
    }
    
    private EnumWorldBlockLayer(final String name, final int ordinal, final String p_i45755_1_, final int p_i45755_2_, final String p_i45755_3_) {
        this.field_180338_e = p_i45755_3_;
    }
    
    @Override
    public String toString() {
        return this.field_180338_e;
    }
}
