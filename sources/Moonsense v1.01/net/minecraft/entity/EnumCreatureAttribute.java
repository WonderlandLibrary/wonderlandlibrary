// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

public enum EnumCreatureAttribute
{
    UNDEFINED("UNDEFINED", 0, "UNDEFINED", 0), 
    UNDEAD("UNDEAD", 1, "UNDEAD", 1), 
    ARTHROPOD("ARTHROPOD", 2, "ARTHROPOD", 2);
    
    private static final EnumCreatureAttribute[] $VALUES;
    private static final String __OBFID = "CL_00001553";
    
    static {
        $VALUES = new EnumCreatureAttribute[] { EnumCreatureAttribute.UNDEFINED, EnumCreatureAttribute.UNDEAD, EnumCreatureAttribute.ARTHROPOD };
    }
    
    private EnumCreatureAttribute(final String name, final int ordinal, final String p_i1597_1_, final int p_i1597_2_) {
    }
}
