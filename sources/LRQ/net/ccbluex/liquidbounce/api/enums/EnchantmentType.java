/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class EnchantmentType
extends Enum<EnchantmentType> {
    public static final /* enum */ EnchantmentType SHARPNESS;
    public static final /* enum */ EnchantmentType POWER;
    public static final /* enum */ EnchantmentType PROTECTION;
    public static final /* enum */ EnchantmentType FEATHER_FALLING;
    public static final /* enum */ EnchantmentType PROJECTILE_PROTECTION;
    public static final /* enum */ EnchantmentType THORNS;
    public static final /* enum */ EnchantmentType FIRE_PROTECTION;
    public static final /* enum */ EnchantmentType RESPIRATION;
    public static final /* enum */ EnchantmentType AQUA_AFFINITY;
    public static final /* enum */ EnchantmentType BLAST_PROTECTION;
    public static final /* enum */ EnchantmentType UNBREAKING;
    private static final /* synthetic */ EnchantmentType[] $VALUES;

    static {
        EnchantmentType[] enchantmentTypeArray = new EnchantmentType[11];
        EnchantmentType[] enchantmentTypeArray2 = enchantmentTypeArray;
        enchantmentTypeArray[0] = SHARPNESS = new EnchantmentType();
        enchantmentTypeArray[1] = POWER = new EnchantmentType();
        enchantmentTypeArray[2] = PROTECTION = new EnchantmentType();
        enchantmentTypeArray[3] = FEATHER_FALLING = new EnchantmentType();
        enchantmentTypeArray[4] = PROJECTILE_PROTECTION = new EnchantmentType();
        enchantmentTypeArray[5] = THORNS = new EnchantmentType();
        enchantmentTypeArray[6] = FIRE_PROTECTION = new EnchantmentType();
        enchantmentTypeArray[7] = RESPIRATION = new EnchantmentType();
        enchantmentTypeArray[8] = AQUA_AFFINITY = new EnchantmentType();
        enchantmentTypeArray[9] = BLAST_PROTECTION = new EnchantmentType();
        enchantmentTypeArray[10] = UNBREAKING = new EnchantmentType();
        $VALUES = enchantmentTypeArray;
    }

    public static EnchantmentType[] values() {
        return (EnchantmentType[])$VALUES.clone();
    }

    public static EnchantmentType valueOf(String string) {
        return Enum.valueOf(EnchantmentType.class, string);
    }
}

