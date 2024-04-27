package net.ccbluex.liquidbounce.api.minecraft.potion;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\bj\bj\bj\b\bj\b\tj\b\nj\bj\b\fj\b\rj\bj\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/potion/PotionType;", "", "(Ljava/lang/String;I)V", "JUMP", "HEAL", "REGENERATION", "BLINDNESS", "MOVE_SPEED", "HUNGER", "DIG_SLOWDOWN", "CONFUSION", "WEAKNESS", "DIG_SPEED", "MOVE_SLOWDOWN", "HARM", "WITHER", "POISON", "NIGHT_VISION", "Pride"})
public final class PotionType
extends Enum<PotionType> {
    public static final PotionType JUMP;
    public static final PotionType HEAL;
    public static final PotionType REGENERATION;
    public static final PotionType BLINDNESS;
    public static final PotionType MOVE_SPEED;
    public static final PotionType HUNGER;
    public static final PotionType DIG_SLOWDOWN;
    public static final PotionType CONFUSION;
    public static final PotionType WEAKNESS;
    public static final PotionType DIG_SPEED;
    public static final PotionType MOVE_SLOWDOWN;
    public static final PotionType HARM;
    public static final PotionType WITHER;
    public static final PotionType POISON;
    public static final PotionType NIGHT_VISION;
    private static final PotionType[] $VALUES;

    static {
        PotionType[] potionTypeArray = new PotionType[15];
        PotionType[] potionTypeArray2 = potionTypeArray;
        potionTypeArray[0] = JUMP = new PotionType();
        potionTypeArray[1] = HEAL = new PotionType();
        potionTypeArray[2] = REGENERATION = new PotionType();
        potionTypeArray[3] = BLINDNESS = new PotionType();
        potionTypeArray[4] = MOVE_SPEED = new PotionType();
        potionTypeArray[5] = HUNGER = new PotionType();
        potionTypeArray[6] = DIG_SLOWDOWN = new PotionType();
        potionTypeArray[7] = CONFUSION = new PotionType();
        potionTypeArray[8] = WEAKNESS = new PotionType();
        potionTypeArray[9] = DIG_SPEED = new PotionType();
        potionTypeArray[10] = MOVE_SLOWDOWN = new PotionType();
        potionTypeArray[11] = HARM = new PotionType();
        potionTypeArray[12] = WITHER = new PotionType();
        potionTypeArray[13] = POISON = new PotionType();
        potionTypeArray[14] = NIGHT_VISION = new PotionType();
        $VALUES = potionTypeArray;
    }

    public static PotionType[] values() {
        return (PotionType[])$VALUES.clone();
    }

    public static PotionType valueOf(String string) {
        return Enum.valueOf(PotionType.class, string);
    }
}
