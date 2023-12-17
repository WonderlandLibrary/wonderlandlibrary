// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

public enum EnumDifficulty
{
    PEACEFUL(0, "options.difficulty.peaceful"), 
    EASY(1, "options.difficulty.easy"), 
    NORMAL(2, "options.difficulty.normal"), 
    HARD(3, "options.difficulty.hard");
    
    private static final EnumDifficulty[] ID_MAPPING;
    private final int id;
    private final String translationKey;
    
    private EnumDifficulty(final int difficultyIdIn, final String difficultyResourceKeyIn) {
        this.id = difficultyIdIn;
        this.translationKey = difficultyResourceKeyIn;
    }
    
    public int getId() {
        return this.id;
    }
    
    public static EnumDifficulty byId(final int id) {
        return EnumDifficulty.ID_MAPPING[id % EnumDifficulty.ID_MAPPING.length];
    }
    
    public String getTranslationKey() {
        return this.translationKey;
    }
    
    static {
        ID_MAPPING = new EnumDifficulty[values().length];
        for (final EnumDifficulty enumdifficulty : values()) {
            EnumDifficulty.ID_MAPPING[enumdifficulty.id] = enumdifficulty;
        }
    }
}
