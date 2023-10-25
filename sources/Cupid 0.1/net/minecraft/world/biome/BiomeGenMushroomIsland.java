package net.minecraft.world.biome;

import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Blocks;

public class BiomeGenMushroomIsland extends BiomeGenBase {
  public BiomeGenMushroomIsland(int p_i1984_1_) {
    super(p_i1984_1_);
    this.theBiomeDecorator.treesPerChunk = -100;
    this.theBiomeDecorator.flowersPerChunk = -100;
    this.theBiomeDecorator.grassPerChunk = -100;
    this.theBiomeDecorator.mushroomsPerChunk = 1;
    this.theBiomeDecorator.bigMushroomsPerChunk = 1;
    this.topBlock = Blocks.mycelium.getDefaultState();
    this.spawnableMonsterList.clear();
    this.spawnableCreatureList.clear();
    this.spawnableWaterCreatureList.clear();
    this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry((Class)EntityMooshroom.class, 8, 4, 8));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\biome\BiomeGenMushroomIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */