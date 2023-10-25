/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class BiomeGenSnow
extends BiomeGenBase {
    private boolean field_150615_aC;
    private WorldGenIcePath field_150617_aE;
    private WorldGenIceSpike field_150616_aD = new WorldGenIceSpike();

    @Override
    protected BiomeGenBase createMutatedBiome(int n) {
        BiomeGenBase biomeGenBase = new BiomeGenSnow(n, true).func_150557_a(0xD2FFFF, true).setBiomeName(String.valueOf(this.biomeName) + " Spikes").setEnableSnow().setTemperatureRainfall(0.0f, 0.5f).setHeight(new BiomeGenBase.Height(this.minHeight + 0.1f, this.maxHeight + 0.1f));
        biomeGenBase.minHeight = this.minHeight + 0.3f;
        biomeGenBase.maxHeight = this.maxHeight + 0.4f;
        return biomeGenBase;
    }

    @Override
    public void decorate(World world, Random random, BlockPos blockPos) {
        if (this.field_150615_aC) {
            int n;
            int n2;
            int n3 = 0;
            while (n3 < 3) {
                n2 = random.nextInt(16) + 8;
                n = random.nextInt(16) + 8;
                this.field_150616_aD.generate(world, random, world.getHeight(blockPos.add(n2, 0, n)));
                ++n3;
            }
            n3 = 0;
            while (n3 < 2) {
                n2 = random.nextInt(16) + 8;
                n = random.nextInt(16) + 8;
                this.field_150617_aE.generate(world, random, world.getHeight(blockPos.add(n2, 0, n)));
                ++n3;
            }
        }
        super.decorate(world, random, blockPos);
    }

    public BiomeGenSnow(int n, boolean bl) {
        super(n);
        this.field_150617_aE = new WorldGenIcePath(4);
        this.field_150615_aC = bl;
        if (bl) {
            this.topBlock = Blocks.snow.getDefaultState();
        }
        this.spawnableCreatureList.clear();
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random random) {
        return new WorldGenTaiga2(false);
    }
}

