// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import java.util.Arrays;
import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import java.util.Map;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityWitch;
import com.google.common.collect.Lists;
import net.minecraft.world.biome.Biome;
import java.util.List;

public class MapGenScatteredFeature extends MapGenStructure
{
    private static final List<Biome> BIOMELIST;
    private final List<Biome.SpawnListEntry> monsters;
    private int maxDistanceBetweenScatteredFeatures;
    private final int minDistanceBetweenScatteredFeatures;
    
    public MapGenScatteredFeature() {
        this.monsters = (List<Biome.SpawnListEntry>)Lists.newArrayList();
        this.maxDistanceBetweenScatteredFeatures = 32;
        this.minDistanceBetweenScatteredFeatures = 8;
        this.monsters.add(new Biome.SpawnListEntry(EntityWitch.class, 1, 1, 1));
    }
    
    public MapGenScatteredFeature(final Map<String, String> p_i2061_1_) {
        this();
        for (final Map.Entry<String, String> entry : p_i2061_1_.entrySet()) {
            if (entry.getKey().equals("distance")) {
                this.maxDistanceBetweenScatteredFeatures = MathHelper.getInt(entry.getValue(), this.maxDistanceBetweenScatteredFeatures, 9);
            }
        }
    }
    
    @Override
    public String getStructureName() {
        return "Temple";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        final int i = chunkX;
        final int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.maxDistanceBetweenScatteredFeatures - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.maxDistanceBetweenScatteredFeatures - 1;
        }
        int k = chunkX / this.maxDistanceBetweenScatteredFeatures;
        int l = chunkZ / this.maxDistanceBetweenScatteredFeatures;
        final Random random = this.world.setRandomSeed(k, l, 14357617);
        k *= this.maxDistanceBetweenScatteredFeatures;
        l *= this.maxDistanceBetweenScatteredFeatures;
        k += random.nextInt(this.maxDistanceBetweenScatteredFeatures - 8);
        l += random.nextInt(this.maxDistanceBetweenScatteredFeatures - 8);
        if (i == k && j == l) {
            final Biome biome = this.world.getBiomeProvider().getBiome(new BlockPos(i * 16 + 8, 0, j * 16 + 8));
            if (biome == null) {
                return false;
            }
            for (final Biome biome2 : MapGenScatteredFeature.BIOMELIST) {
                if (biome == biome2) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final BlockPos pos, final boolean findUnexplored) {
        this.world = worldIn;
        return MapGenStructure.findNearestStructurePosBySpacing(worldIn, this, pos, this.maxDistanceBetweenScatteredFeatures, 8, 14357617, false, 100, findUnexplored);
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new Start(this.world, this.rand, chunkX, chunkZ);
    }
    
    public boolean isSwampHut(final BlockPos pos) {
        final StructureStart structurestart = this.getStructureAt(pos);
        if (structurestart != null && structurestart instanceof Start && !structurestart.components.isEmpty()) {
            final StructureComponent structurecomponent = structurestart.components.get(0);
            return structurecomponent instanceof ComponentScatteredFeaturePieces.SwampHut;
        }
        return false;
    }
    
    public List<Biome.SpawnListEntry> getMonsters() {
        return this.monsters;
    }
    
    static {
        BIOMELIST = Arrays.asList(Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.SWAMPLAND, Biomes.ICE_PLAINS, Biomes.COLD_TAIGA);
    }
    
    public static class Start extends StructureStart
    {
        public Start() {
        }
        
        public Start(final World worldIn, final Random random, final int chunkX, final int chunkZ) {
            this(worldIn, random, chunkX, chunkZ, worldIn.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8)));
        }
        
        public Start(final World worldIn, final Random random, final int chunkX, final int chunkZ, final Biome biomeIn) {
            super(chunkX, chunkZ);
            if (biomeIn != Biomes.JUNGLE && biomeIn != Biomes.JUNGLE_HILLS) {
                if (biomeIn == Biomes.SWAMPLAND) {
                    final ComponentScatteredFeaturePieces.SwampHut componentscatteredfeaturepieces$swamphut = new ComponentScatteredFeaturePieces.SwampHut(random, chunkX * 16, chunkZ * 16);
                    this.components.add(componentscatteredfeaturepieces$swamphut);
                }
                else if (biomeIn != Biomes.DESERT && biomeIn != Biomes.DESERT_HILLS) {
                    if (biomeIn == Biomes.ICE_PLAINS || biomeIn == Biomes.COLD_TAIGA) {
                        final ComponentScatteredFeaturePieces.Igloo componentscatteredfeaturepieces$igloo = new ComponentScatteredFeaturePieces.Igloo(random, chunkX * 16, chunkZ * 16);
                        this.components.add(componentscatteredfeaturepieces$igloo);
                    }
                }
                else {
                    final ComponentScatteredFeaturePieces.DesertPyramid componentscatteredfeaturepieces$desertpyramid = new ComponentScatteredFeaturePieces.DesertPyramid(random, chunkX * 16, chunkZ * 16);
                    this.components.add(componentscatteredfeaturepieces$desertpyramid);
                }
            }
            else {
                final ComponentScatteredFeaturePieces.JunglePyramid componentscatteredfeaturepieces$junglepyramid = new ComponentScatteredFeaturePieces.JunglePyramid(random, chunkX * 16, chunkZ * 16);
                this.components.add(componentscatteredfeaturepieces$junglepyramid);
            }
            this.updateBoundingBox();
        }
    }
}
