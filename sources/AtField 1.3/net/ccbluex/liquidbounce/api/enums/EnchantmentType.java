/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class EnchantmentType
extends Enum {
    public static final /* enum */ EnchantmentType PROTECTION;
    public static final /* enum */ EnchantmentType PROJECTILE_PROTECTION;
    private static final EnchantmentType[] $VALUES;
    public static final /* enum */ EnchantmentType SHARPNESS;
    public static final /* enum */ EnchantmentType THORNS;
    public static final /* enum */ EnchantmentType RESPIRATION;
    public static final /* enum */ EnchantmentType FIRE_PROTECTION;
    public static final /* enum */ EnchantmentType AQUA_AFFINITY;
    public static final /* enum */ EnchantmentType POWER;
    public static final /* enum */ EnchantmentType FEATHER_FALLING;
    public static final /* enum */ EnchantmentType UNBREAKING;
    public static final /* enum */ EnchantmentType BLAST_PROTECTION;

    public static EnchantmentType valueOf(String string) {
        return Enum.valueOf(EnchantmentType.class, string);
    }

    public static EnchantmentType[] values() {
        return (EnchantmentType[])$VALUES.clone();
    }

    static {
        EnchantmentType[] enchantmentTypeArray = new EnchantmentType[11];
        EnchantmentType[] enchantmentTypeArray2 = enchantmentTypeArray;
        enchantmentTypeArray[0] = SHARPNESS = new EnchantmentType("SHARPNESS", 0);
        enchantmentTypeArray[1] = POWER = new EnchantmentType("POWER", 1);
        enchantmentTypeArray[2] = PROTECTION = new EnchantmentType("PROTECTION", 2);
        enchantmentTypeArray[3] = FEATHER_FALLING = new EnchantmentType("FEATHER_FALLING", 3);
        enchantmentTypeArray[4] = PROJECTILE_PROTECTION = new EnchantmentType("PROJECTILE_PROTECTION", 4);
        enchantmentTypeArray[5] = THORNS = new EnchantmentType("THORNS", 5);
        enchantmentTypeArray[6] = FIRE_PROTECTION = new EnchantmentType("FIRE_PROTECTION", 6);
        enchantmentTypeArray[7] = RESPIRATION = new EnchantmentType("RESPIRATION", 7);
        enchantmentTypeArray[8] = AQUA_AFFINITY = new EnchantmentType("AQUA_AFFINITY", 8);
        enchantmentTypeArray[9] = BLAST_PROTECTION = new EnchantmentType("BLAST_PROTECTION", 9);
        enchantmentTypeArray[10] = UNBREAKING = new EnchantmentType("UNBREAKING", 10);
        $VALUES = enchantmentTypeArray;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private EnchantmentType() {
        void var2_-1;
        void var1_-1;
    }
}

