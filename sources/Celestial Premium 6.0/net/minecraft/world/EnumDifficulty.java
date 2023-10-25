/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

public enum EnumDifficulty {
    PEACEFUL(0, "options.difficulty.peaceful"),
    EASY(1, "options.difficulty.easy"),
    NORMAL(2, "options.difficulty.normal"),
    HARD(3, "options.difficulty.hard");

    private static final EnumDifficulty[] ID_MAPPING;
    private final int difficultyId;
    private final String difficultyResourceKey;

    private EnumDifficulty(int difficultyIdIn, String difficultyResourceKeyIn) {
        this.difficultyId = difficultyIdIn;
        this.difficultyResourceKey = difficultyResourceKeyIn;
    }

    public int getDifficultyId() {
        return this.difficultyId;
    }

    public static EnumDifficulty getDifficultyEnum(int p_151523_0_) {
        return ID_MAPPING[p_151523_0_ % ID_MAPPING.length];
    }

    public String getDifficultyResourceKey() {
        return this.difficultyResourceKey;
    }

    static {
        ID_MAPPING = new EnumDifficulty[EnumDifficulty.values().length];
        EnumDifficulty[] arrenumDifficulty = EnumDifficulty.values();
        int n = arrenumDifficulty.length;
        for (int i = 0; i < n; ++i) {
            EnumDifficulty enumdifficulty;
            EnumDifficulty.ID_MAPPING[enumdifficulty.difficultyId] = enumdifficulty = arrenumDifficulty[i];
        }
    }
}

