/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

public enum EnumDifficulty {
    PEACEFUL(0, "options.difficulty.peaceful"),
    EASY(1, "options.difficulty.easy"),
    NORMAL(2, "options.difficulty.normal"),
    HARD(3, "options.difficulty.hard");

    private static final EnumDifficulty[] difficultyEnums = new EnumDifficulty[EnumDifficulty.values().length];
    private final int difficultyId;
    private final String difficultyResourceKey;

    private EnumDifficulty(int n2, String string2) {
        this.difficultyId = n2;
        this.difficultyResourceKey = string2;
    }

    public int getDifficultyId() {
        return this.difficultyId;
    }

    public static EnumDifficulty getDifficultyEnum(int n) {
        return difficultyEnums[n % difficultyEnums.length];
    }

    public String getDifficultyResourceKey() {
        return this.difficultyResourceKey;
    }

    static {
        EnumDifficulty[] enumDifficultyArray = EnumDifficulty.values();
        int n = enumDifficultyArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumDifficulty enumDifficulty;
            EnumDifficulty.difficultyEnums[enumDifficulty.difficultyId] = enumDifficulty = enumDifficultyArray[n2];
            ++n2;
        }
    }
}

