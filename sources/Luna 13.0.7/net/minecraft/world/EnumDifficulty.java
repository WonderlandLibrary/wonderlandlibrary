package net.minecraft.world;

public enum EnumDifficulty
{
  private static final EnumDifficulty[] difficultyEnums;
  private final int difficultyId;
  private final String difficultyResourceKey;
  private static final EnumDifficulty[] $VALUES;
  private static final String __OBFID = "CL_00001510";
  
  private EnumDifficulty(String p_i45312_1_, int p_i45312_2_, int p_i45312_3_, String p_i45312_4_)
  {
    this.difficultyId = p_i45312_3_;
    this.difficultyResourceKey = p_i45312_4_;
  }
  
  public int getDifficultyId()
  {
    return this.difficultyId;
  }
  
  public static EnumDifficulty getDifficultyEnum(int p_151523_0_)
  {
    return difficultyEnums[(p_151523_0_ % difficultyEnums.length)];
  }
  
  public String getDifficultyResourceKey()
  {
    return this.difficultyResourceKey;
  }
  
  static
  {
    difficultyEnums = new EnumDifficulty[values().length];
    
    $VALUES = new EnumDifficulty[] { PEACEFUL, EASY, NORMAL, HARD };
    
    EnumDifficulty[] var0 = values();
    int var1 = var0.length;
    for (int var2 = 0; var2 < var1; var2++)
    {
      EnumDifficulty var3 = var0[var2];
      difficultyEnums[var3.difficultyId] = var3;
    }
  }
}
