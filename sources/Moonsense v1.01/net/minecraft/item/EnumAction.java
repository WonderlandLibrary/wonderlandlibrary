// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

public enum EnumAction
{
    NONE("NONE", 0, "NONE", 0), 
    EAT("EAT", 1, "EAT", 1), 
    DRINK("DRINK", 2, "DRINK", 2), 
    BLOCK("BLOCK", 3, "BLOCK", 3), 
    BOW("BOW", 4, "BOW", 4);
    
    private static final EnumAction[] $VALUES;
    private static final String __OBFID = "CL_00000073";
    
    static {
        $VALUES = new EnumAction[] { EnumAction.NONE, EnumAction.EAT, EnumAction.DRINK, EnumAction.BLOCK, EnumAction.BOW };
    }
    
    private EnumAction(final String name, final int ordinal, final String p_i1910_1_, final int p_i1910_2_) {
    }
}
