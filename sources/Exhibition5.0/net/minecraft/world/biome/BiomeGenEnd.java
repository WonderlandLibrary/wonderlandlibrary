// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import net.minecraft.init.Blocks;
import net.minecraft.entity.monster.EntityEnderman;

public class BiomeGenEnd extends BiomeGenBase
{
    private static final String __OBFID = "CL_00000187";
    
    public BiomeGenEnd(final int p_i1990_1_) {
        super(p_i1990_1_);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 10, 4, 4));
        this.topBlock = Blocks.dirt.getDefaultState();
        this.fillerBlock = Blocks.dirt.getDefaultState();
        this.theBiomeDecorator = new BiomeEndDecorator();
    }
    
    @Override
    public int getSkyColorByTemp(final float p_76731_1_) {
        return 0;
    }
}
