// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

public enum EnumDifficulty
{
    PEACEFUL("PEACEFUL", 0, 0, "options.difficulty.peaceful"), 
    EASY("EASY", 1, 1, "options.difficulty.easy"), 
    NORMAL("NORMAL", 2, 2, "options.difficulty.normal"), 
    HARD("HARD", 3, 3, "options.difficulty.hard");
    
    private static final EnumDifficulty[] difficultyEnums;
    private final int difficultyId;
    private final String difficultyResourceKey;
    private static final EnumDifficulty[] $VALUES;
    private static final String __OBFID = "CL_00001510";
    
    private EnumDifficulty(final String p_i45312_1_, final int p_i45312_2_, final int p_i45312_3_, final String p_i45312_4_) {
        this.difficultyId = p_i45312_3_;
        this.difficultyResourceKey = p_i45312_4_;
    }
    
    public int getDifficultyId() {
        return this.difficultyId;
    }
    
    public static EnumDifficulty getDifficultyEnum(final int p_151523_0_) {
        return EnumDifficulty.difficultyEnums[p_151523_0_ % EnumDifficulty.difficultyEnums.length];
    }
    
    public String getDifficultyResourceKey() {
        return this.difficultyResourceKey;
    }
    
    static {
        difficultyEnums = new EnumDifficulty[values().length];
        $VALUES = new EnumDifficulty[] { EnumDifficulty.PEACEFUL, EnumDifficulty.EASY, EnumDifficulty.NORMAL, EnumDifficulty.HARD };
        for (final EnumDifficulty var4 : values()) {
            EnumDifficulty.difficultyEnums[var4.difficultyId] = var4;
        }
    }
}
