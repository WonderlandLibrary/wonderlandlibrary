/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.minecraft.potion;

public final class PotionType
extends Enum {
    public static final /* enum */ PotionType WITHER;
    public static final /* enum */ PotionType NIGHT_VISION;
    public static final /* enum */ PotionType BLINDNESS;
    public static final /* enum */ PotionType HEAL;
    public static final /* enum */ PotionType CONFUSION;
    public static final /* enum */ PotionType MOVE_SPEED;
    public static final /* enum */ PotionType GLOWING;
    public static final /* enum */ PotionType HUNGER;
    public static final /* enum */ PotionType MOVE_SLOWDOWN;
    public static final /* enum */ PotionType HARM;
    public static final /* enum */ PotionType REGENERATION;
    private static final PotionType[] $VALUES;
    public static final /* enum */ PotionType DIG_SLOWDOWN;
    public static final /* enum */ PotionType JUMP;
    public static final /* enum */ PotionType WEAKNESS;
    public static final /* enum */ PotionType POISON;

    public static PotionType valueOf(String string) {
        return Enum.valueOf(PotionType.class, string);
    }

    static {
        PotionType[] potionTypeArray = new PotionType[15];
        PotionType[] potionTypeArray2 = potionTypeArray;
        potionTypeArray[0] = HEAL = new PotionType("HEAL", 0);
        potionTypeArray[1] = REGENERATION = new PotionType("REGENERATION", 1);
        potionTypeArray[2] = BLINDNESS = new PotionType("BLINDNESS", 2);
        potionTypeArray[3] = MOVE_SPEED = new PotionType("MOVE_SPEED", 3);
        potionTypeArray[4] = HUNGER = new PotionType("HUNGER", 4);
        potionTypeArray[5] = DIG_SLOWDOWN = new PotionType("DIG_SLOWDOWN", 5);
        potionTypeArray[6] = CONFUSION = new PotionType("CONFUSION", 6);
        potionTypeArray[7] = WEAKNESS = new PotionType("WEAKNESS", 7);
        potionTypeArray[8] = MOVE_SLOWDOWN = new PotionType("MOVE_SLOWDOWN", 8);
        potionTypeArray[9] = HARM = new PotionType("HARM", 9);
        potionTypeArray[10] = WITHER = new PotionType("WITHER", 10);
        potionTypeArray[11] = POISON = new PotionType("POISON", 11);
        potionTypeArray[12] = NIGHT_VISION = new PotionType("NIGHT_VISION", 12);
        potionTypeArray[13] = JUMP = new PotionType("JUMP", 13);
        potionTypeArray[14] = GLOWING = new PotionType("GLOWING", 14);
        $VALUES = potionTypeArray;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private PotionType() {
        void var2_-1;
        void var1_-1;
    }

    public static PotionType[] values() {
        return (PotionType[])$VALUES.clone();
    }
}

