package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\r\b\u00002\b0\u00000B\b¢j\bj\bj\bj\bj\bj\b\bj\b\tj\b\nj\bj\b\fj\b\r¨"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/EnchantmentType;", "", "(Ljava/lang/String;I)V", "SHARPNESS", "POWER", "PROTECTION", "FEATHER_FALLING", "PROJECTILE_PROTECTION", "THORNS", "FIRE_PROTECTION", "RESPIRATION", "AQUA_AFFINITY", "BLAST_PROTECTION", "UNBREAKING", "Pride"})
public final class EnchantmentType
extends Enum<EnchantmentType> {
    public static final EnchantmentType SHARPNESS;
    public static final EnchantmentType POWER;
    public static final EnchantmentType PROTECTION;
    public static final EnchantmentType FEATHER_FALLING;
    public static final EnchantmentType PROJECTILE_PROTECTION;
    public static final EnchantmentType THORNS;
    public static final EnchantmentType FIRE_PROTECTION;
    public static final EnchantmentType RESPIRATION;
    public static final EnchantmentType AQUA_AFFINITY;
    public static final EnchantmentType BLAST_PROTECTION;
    public static final EnchantmentType UNBREAKING;
    private static final EnchantmentType[] $VALUES;

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
