// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenFossils;
import net.minecraft.world.gen.feature.WorldGenDesertWells;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import java.util.Iterator;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;

public class BiomeDesert extends Biome
{
    public BiomeDesert(final BiomeProperties properties) {
        super(properties);
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();
        this.decorator.treesPerChunk = -999;
        this.decorator.deadBushPerChunk = 2;
        this.decorator.reedsPerChunk = 50;
        this.decorator.cactiPerChunk = 10;
        this.spawnableCreatureList.clear();
        this.spawnableCreatureList.add(new SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        final Iterator<SpawnListEntry> iterator = this.spawnableMonsterList.iterator();
        while (iterator.hasNext()) {
            final SpawnListEntry biome$spawnlistentry = iterator.next();
            if (biome$spawnlistentry.entityClass == EntityZombie.class || biome$spawnlistentry.entityClass == EntityZombieVillager.class) {
                iterator.remove();
            }
        }
        this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 19, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityZombieVillager.class, 1, 1, 1));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityHusk.class, 80, 4, 4));
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        if (rand.nextInt(1000) == 0) {
            final int i = rand.nextInt(16) + 8;
            final int j = rand.nextInt(16) + 8;
            final BlockPos blockpos = worldIn.getHeight(pos.add(i, 0, j)).up();
            new WorldGenDesertWells().generate(worldIn, rand, blockpos);
        }
        if (rand.nextInt(64) == 0) {
            new WorldGenFossils().generate(worldIn, rand, pos);
        }
    }
}
