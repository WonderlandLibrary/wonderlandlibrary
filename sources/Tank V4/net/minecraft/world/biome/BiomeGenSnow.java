package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class BiomeGenSnow extends BiomeGenBase {
   private WorldGenIceSpike field_150616_aD = new WorldGenIceSpike();
   private boolean field_150615_aC;
   private WorldGenIcePath field_150617_aE = new WorldGenIcePath(4);

   public BiomeGenSnow(int var1, boolean var2) {
      super(var1);
      this.field_150615_aC = var2;
      if (var2) {
         this.topBlock = Blocks.snow.getDefaultState();
      }

      this.spawnableCreatureList.clear();
   }

   public void decorate(World var1, Random var2, BlockPos var3) {
      if (this.field_150615_aC) {
         int var4;
         int var5;
         int var6;
         for(var4 = 0; var4 < 3; ++var4) {
            var5 = var2.nextInt(16) + 8;
            var6 = var2.nextInt(16) + 8;
            this.field_150616_aD.generate(var1, var2, var1.getHeight(var3.add(var5, 0, var6)));
         }

         for(var4 = 0; var4 < 2; ++var4) {
            var5 = var2.nextInt(16) + 8;
            var6 = var2.nextInt(16) + 8;
            this.field_150617_aE.generate(var1, var2, var1.getHeight(var3.add(var5, 0, var6)));
         }
      }

      super.decorate(var1, var2, var3);
   }

   protected BiomeGenBase createMutatedBiome(int var1) {
      BiomeGenBase var2 = (new BiomeGenSnow(var1, true)).func_150557_a(13828095, true).setBiomeName(this.biomeName + " Spikes").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F).setHeight(new BiomeGenBase.Height(this.minHeight + 0.1F, this.maxHeight + 0.1F));
      var2.minHeight = this.minHeight + 0.3F;
      var2.maxHeight = this.maxHeight + 0.4F;
      return var2;
   }

   public WorldGenAbstractTree genBigTreeChance(Random var1) {
      return new WorldGenTaiga2(false);
   }
}
