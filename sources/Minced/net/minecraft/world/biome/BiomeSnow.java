// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import java.util.Iterator;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;

public class BiomeSnow extends Biome
{
    private final boolean superIcy;
    private final WorldGenIceSpike iceSpike;
    private final WorldGenIcePath icePatch;
    
    public BiomeSnow(final boolean superIcyIn, final BiomeProperties properties) {
        super(properties);
        this.iceSpike = new WorldGenIceSpike();
        this.icePatch = new WorldGenIcePath(4);
        this.superIcy = superIcyIn;
        if (superIcyIn) {
            this.topBlock = Blocks.SNOW.getDefaultState();
        }
        this.spawnableCreatureList.clear();
        this.spawnableCreatureList.add(new SpawnListEntry(EntityRabbit.class, 10, 2, 3));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityPolarBear.class, 1, 1, 2));
        final Iterator<SpawnListEntry> iterator = this.spawnableMonsterList.iterator();
        while (iterator.hasNext()) {
            final SpawnListEntry biome$spawnlistentry = iterator.next();
            if (biome$spawnlistentry.entityClass == EntitySkeleton.class) {
                iterator.remove();
            }
        }
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 20, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityStray.class, 80, 4, 4));
    }
    
    @Override
    public float getSpawningChance() {
        return 0.07f;
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        if (this.superIcy) {
            for (int i = 0; i < 3; ++i) {
                final int j = rand.nextInt(16) + 8;
                final int k = rand.nextInt(16) + 8;
                this.iceSpike.generate(worldIn, rand, worldIn.getHeight(pos.add(j, 0, k)));
            }
            for (int l = 0; l < 2; ++l) {
                final int i2 = rand.nextInt(16) + 8;
                final int j2 = rand.nextInt(16) + 8;
                this.icePatch.generate(worldIn, rand, worldIn.getHeight(pos.add(i2, 0, j2)));
            }
        }
        super.decorate(worldIn, rand, pos);
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(final Random rand) {
        return new WorldGenTaiga2(false);
    }
}
