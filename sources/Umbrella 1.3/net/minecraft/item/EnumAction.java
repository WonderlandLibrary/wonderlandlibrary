/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

public enum EnumAction {
    NONE("NONE", 0),
    EAT("EAT", 1),
    DRINK("DRINK", 2),
    BLOCK("BLOCK", 3),
    BOW("BOW", 4);

    private static final EnumAction[] $VALUES;
    private static final String __OBFID = "CL_00000073";

    static {
        $VALUES = new EnumAction[]{NONE, EAT, DRINK, BLOCK, BOW};
    }

    private EnumAction(String p_i1910_1_, int p_i1910_2_) {
    }
}

