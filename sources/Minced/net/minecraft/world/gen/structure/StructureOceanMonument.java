// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import com.google.common.collect.Sets;
import net.minecraft.util.math.ChunkPos;
import java.util.Set;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityGuardian;
import com.google.common.collect.Lists;
import java.util.Arrays;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import java.util.Map;
import net.minecraft.world.biome.Biome;
import java.util.List;

public class StructureOceanMonument extends MapGenStructure
{
    private int spacing;
    private int separation;
    public static final List<Biome> WATER_BIOMES;
    public static final List<Biome> SPAWN_BIOMES;
    private static final List<Biome.SpawnListEntry> MONUMENT_ENEMIES;
    
    public StructureOceanMonument() {
        this.spacing = 32;
        this.separation = 5;
    }
    
    public StructureOceanMonument(final Map<String, String> p_i45608_1_) {
        this();
        for (final Map.Entry<String, String> entry : p_i45608_1_.entrySet()) {
            if (entry.getKey().equals("spacing")) {
                this.spacing = MathHelper.getInt(entry.getValue(), this.spacing, 1);
            }
            else {
                if (!entry.getKey().equals("separation")) {
                    continue;
                }
                this.separation = MathHelper.getInt(entry.getValue(), this.separation, 1);
            }
        }
    }
    
    @Override
    public String getStructureName() {
        return "Monument";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        final int i = chunkX;
        final int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.spacing - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.spacing - 1;
        }
        int k = chunkX / this.spacing;
        int l = chunkZ / this.spacing;
        final Random random = this.world.setRandomSeed(k, l, 10387313);
        k *= this.spacing;
        l *= this.spacing;
        k += (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        l += (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        if (i == k && j == l) {
            if (!this.world.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 16, StructureOceanMonument.SPAWN_BIOMES)) {
                return false;
            }
            final boolean flag = this.world.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 29, StructureOceanMonument.WATER_BIOMES);
            if (flag) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final BlockPos pos, final boolean findUnexplored) {
        this.world = worldIn;
        return MapGenStructure.findNearestStructurePosBySpacing(worldIn, this, pos, this.spacing, this.separation, 10387313, true, 100, findUnexplored);
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        return new StartMonument(this.world, this.rand, chunkX, chunkZ);
    }
    
    public List<Biome.SpawnListEntry> getMonsters() {
        return StructureOceanMonument.MONUMENT_ENEMIES;
    }
    
    static {
        WATER_BIOMES = Arrays.asList(Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.RIVER, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER);
        SPAWN_BIOMES = Arrays.asList(Biomes.DEEP_OCEAN);
        (MONUMENT_ENEMIES = Lists.newArrayList()).add(new Biome.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
    }
    
    public static class StartMonument extends StructureStart
    {
        private final Set<ChunkPos> processed;
        private boolean wasCreated;
        
        public StartMonument() {
            this.processed = (Set<ChunkPos>)Sets.newHashSet();
        }
        
        public StartMonument(final World worldIn, final Random random, final int chunkX, final int chunkZ) {
            super(chunkX, chunkZ);
            this.processed = (Set<ChunkPos>)Sets.newHashSet();
            this.create(worldIn, random, chunkX, chunkZ);
        }
        
        private void create(final World worldIn, final Random random, final int chunkX, final int chunkZ) {
            random.setSeed(worldIn.getSeed());
            final long i = random.nextLong();
            final long j = random.nextLong();
            final long k = chunkX * i;
            final long l = chunkZ * j;
            random.setSeed(k ^ l ^ worldIn.getSeed());
            final int i2 = chunkX * 16 + 8 - 29;
            final int j2 = chunkZ * 16 + 8 - 29;
            final EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(random);
            this.components.add(new StructureOceanMonumentPieces.MonumentBuilding(random, i2, j2, enumfacing));
            this.updateBoundingBox();
            this.wasCreated = true;
        }
        
        @Override
        public void generateStructure(final World worldIn, final Random rand, final StructureBoundingBox structurebb) {
            if (!this.wasCreated) {
                this.components.clear();
                this.create(worldIn, rand, this.getChunkPosX(), this.getChunkPosZ());
            }
            super.generateStructure(worldIn, rand, structurebb);
        }
        
        @Override
        public boolean isValidForPostProcess(final ChunkPos pair) {
            return !this.processed.contains(pair) && super.isValidForPostProcess(pair);
        }
        
        @Override
        public void notifyPostProcessAt(final ChunkPos pair) {
            super.notifyPostProcessAt(pair);
            this.processed.add(pair);
        }
        
        @Override
        public void writeToNBT(final NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            final NBTTagList nbttaglist = new NBTTagList();
            for (final ChunkPos chunkpos : this.processed) {
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setInteger("X", chunkpos.x);
                nbttagcompound.setInteger("Z", chunkpos.z);
                nbttaglist.appendTag(nbttagcompound);
            }
            tagCompound.setTag("Processed", nbttaglist);
        }
        
        @Override
        public void readFromNBT(final NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            if (tagCompound.hasKey("Processed", 9)) {
                final NBTTagList nbttaglist = tagCompound.getTagList("Processed", 10);
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                    this.processed.add(new ChunkPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Z")));
                }
            }
        }
    }
}
