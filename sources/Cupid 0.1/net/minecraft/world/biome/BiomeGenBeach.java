package net.minecraft.world.biome;

import net.minecraft.init.Blocks;

public class BiomeGenBeach extends BiomeGenBase {
  public BiomeGenBeach(int p_i1969_1_) {
    super(p_i1969_1_);
    this.spawnableCreatureList.clear();
    this.topBlock = Blocks.sand.getDefaultState();
    this.fillerBlock = Blocks.sand.getDefaultState();
    this.theBiomeDecorator.treesPerChunk = -999;
    this.theBiomeDecorator.deadBushPerChunk = 0;
    this.theBiomeDecorator.reedsPerChunk = 0;
    this.theBiomeDecorator.cactiPerChunk = 0;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\biome\BiomeGenBeach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */