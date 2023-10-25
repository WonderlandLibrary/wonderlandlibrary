package net.minecraft.world;

public enum EnumDifficulty {
  PEACEFUL(0, "options.difficulty.peaceful"),
  EASY(1, "options.difficulty.easy"),
  NORMAL(2, "options.difficulty.normal"),
  HARD(3, "options.difficulty.hard");
  
  private static final EnumDifficulty[] difficultyEnums;
  
  private final int difficultyId;
  
  private final String difficultyResourceKey;
  
  static {
    difficultyEnums = new EnumDifficulty[(values()).length];
    for (EnumDifficulty enumdifficulty : values())
      difficultyEnums[enumdifficulty.difficultyId] = enumdifficulty; 
  }
  
  EnumDifficulty(int difficultyIdIn, String difficultyResourceKeyIn) {
    this.difficultyId = difficultyIdIn;
    this.difficultyResourceKey = difficultyResourceKeyIn;
  }
  
  public int getDifficultyId() {
    return this.difficultyId;
  }
  
  public static EnumDifficulty getDifficultyEnum(int p_151523_0_) {
    return difficultyEnums[p_151523_0_ % difficultyEnums.length];
  }
  
  public String getDifficultyResourceKey() {
    return this.difficultyResourceKey;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\EnumDifficulty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */