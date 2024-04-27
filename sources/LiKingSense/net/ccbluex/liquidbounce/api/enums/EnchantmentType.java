/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\r\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\r\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/EnchantmentType;", "", "(Ljava/lang/String;I)V", "SHARPNESS", "POWER", "PROTECTION", "FEATHER_FALLING", "PROJECTILE_PROTECTION", "THORNS", "FIRE_PROTECTION", "RESPIRATION", "AQUA_AFFINITY", "BLAST_PROTECTION", "UNBREAKING", "LiKingSense"})
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

