/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.minecraft.potion;

public final class PotionType
extends Enum<PotionType> {
    public static final /* enum */ PotionType DIG_SPEED;
    public static final /* enum */ PotionType HEAL;
    public static final /* enum */ PotionType REGENERATION;
    public static final /* enum */ PotionType BLINDNESS;
    public static final /* enum */ PotionType MOVE_SPEED;
    public static final /* enum */ PotionType HUNGER;
    public static final /* enum */ PotionType DIG_SLOWDOWN;
    public static final /* enum */ PotionType CONFUSION;
    public static final /* enum */ PotionType WEAKNESS;
    public static final /* enum */ PotionType MOVE_SLOWDOWN;
    public static final /* enum */ PotionType HARM;
    public static final /* enum */ PotionType WITHER;
    public static final /* enum */ PotionType POISON;
    public static final /* enum */ PotionType NIGHT_VISION;
    public static final /* enum */ PotionType JUMP;
    private static final /* synthetic */ PotionType[] $VALUES;

    static {
        PotionType[] potionTypeArray = new PotionType[15];
        PotionType[] potionTypeArray2 = potionTypeArray;
        potionTypeArray[0] = DIG_SPEED = new PotionType();
        potionTypeArray[1] = HEAL = new PotionType();
        potionTypeArray[2] = REGENERATION = new PotionType();
        potionTypeArray[3] = BLINDNESS = new PotionType();
        potionTypeArray[4] = MOVE_SPEED = new PotionType();
        potionTypeArray[5] = HUNGER = new PotionType();
        potionTypeArray[6] = DIG_SLOWDOWN = new PotionType();
        potionTypeArray[7] = CONFUSION = new PotionType();
        potionTypeArray[8] = WEAKNESS = new PotionType();
        potionTypeArray[9] = MOVE_SLOWDOWN = new PotionType();
        potionTypeArray[10] = HARM = new PotionType();
        potionTypeArray[11] = WITHER = new PotionType();
        potionTypeArray[12] = POISON = new PotionType();
        potionTypeArray[13] = NIGHT_VISION = new PotionType();
        potionTypeArray[14] = JUMP = new PotionType();
        $VALUES = potionTypeArray;
    }

    public static PotionType[] values() {
        return (PotionType[])$VALUES.clone();
    }

    public static PotionType valueOf(String string) {
        return Enum.valueOf(PotionType.class, string);
    }
}

