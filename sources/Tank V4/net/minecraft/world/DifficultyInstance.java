package net.minecraft.world;

import net.minecraft.util.MathHelper;

public class DifficultyInstance {
   private final EnumDifficulty worldDifficulty;
   private final float additionalDifficulty;

   public DifficultyInstance(EnumDifficulty var1, long var2, long var4, float var6) {
      this.worldDifficulty = var1;
      this.additionalDifficulty = this.calculateAdditionalDifficulty(var1, var2, var4, var6);
   }

   public float getAdditionalDifficulty() {
      return this.additionalDifficulty;
   }

   private float calculateAdditionalDifficulty(EnumDifficulty var1, long var2, long var4, float var6) {
      if (var1 == EnumDifficulty.PEACEFUL) {
         return 0.0F;
      } else {
         boolean var7 = var1 == EnumDifficulty.HARD;
         float var8 = 0.75F;
         float var9 = MathHelper.clamp_float(((float)var2 + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
         var8 += var9;
         float var10 = 0.0F;
         var10 += MathHelper.clamp_float((float)var4 / 3600000.0F, 0.0F, 1.0F) * (var7 ? 1.0F : 0.75F);
         var10 += MathHelper.clamp_float(var6 * 0.25F, 0.0F, var9);
         if (var1 == EnumDifficulty.EASY) {
            var10 *= 0.5F;
         }

         var8 += var10;
         return (float)var1.getDifficultyId() * var8;
      }
   }

   public float getClampedAdditionalDifficulty() {
      return this.additionalDifficulty < 2.0F ? 0.0F : (this.additionalDifficulty > 4.0F ? 1.0F : (this.additionalDifficulty - 2.0F) / 2.0F);
   }
}
