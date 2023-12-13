// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

public enum EnumAction
{
    NONE("NONE", 0), 
    EAT("EAT", 1), 
    DRINK("DRINK", 2), 
    BLOCK("BLOCK", 3), 
    BOW("BOW", 4);
    
    private static final EnumAction[] $VALUES;
    private static final String __OBFID = "CL_00000073";
    
    private EnumAction(final String p_i1910_1_, final int p_i1910_2_) {
    }
    
    static {
        $VALUES = new EnumAction[] { EnumAction.NONE, EnumAction.EAT, EnumAction.DRINK, EnumAction.BLOCK, EnumAction.BOW };
    }
}
