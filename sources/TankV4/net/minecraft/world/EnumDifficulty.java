package net.minecraft.world;

public enum EnumDifficulty {
   HARD(3, "options.difficulty.hard"),
   EASY(1, "options.difficulty.easy");

   private static final EnumDifficulty[] ENUM$VALUES = new EnumDifficulty[]{PEACEFUL, EASY, NORMAL, HARD};
   PEACEFUL(0, "options.difficulty.peaceful");

   private final String difficultyResourceKey;
   private final int difficultyId;
   NORMAL(2, "options.difficulty.normal");

   private static final EnumDifficulty[] difficultyEnums = new EnumDifficulty[values().length];

   public static EnumDifficulty getDifficultyEnum(int var0) {
      return difficultyEnums[var0 % difficultyEnums.length];
   }

   public String getDifficultyResourceKey() {
      return this.difficultyResourceKey;
   }

   private EnumDifficulty(int var3, String var4) {
      this.difficultyId = var3;
      this.difficultyResourceKey = var4;
   }

   static {
      EnumDifficulty[] var3;
      int var2 = (var3 = values()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         EnumDifficulty var0 = var3[var1];
         difficultyEnums[var0.difficultyId] = var0;
      }

   }

   public int getDifficultyId() {
      return this.difficultyId;
   }
}
