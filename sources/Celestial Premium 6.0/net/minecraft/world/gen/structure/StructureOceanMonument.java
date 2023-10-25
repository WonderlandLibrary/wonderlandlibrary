/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;
import net.minecraft.world.gen.structure.StructureStart;

public class StructureOceanMonument
extends MapGenStructure {
    private int spacing = 32;
    private int separation = 5;
    public static final List<Biome> WATER_BIOMES = Arrays.asList(Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.RIVER, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER);
    public static final List<Biome> SPAWN_BIOMES = Arrays.asList(Biomes.DEEP_OCEAN);
    private static final List<Biome.SpawnListEntry> MONUMENT_ENEMIES = Lists.newArrayList();

    public StructureOceanMonument() {
    }

    public StructureOceanMonument(Map<String, String> p_i45608_1_) {
        this();
        for (Map.Entry<String, String> entry : p_i45608_1_.entrySet()) {
            if (entry.getKey().equals("spacing")) {
                this.spacing = MathHelper.getInt(entry.getValue(), this.spacing, 1);
                continue;
            }
            if (!entry.getKey().equals("separation")) continue;
            this.separation = MathHelper.getInt(entry.getValue(), this.separation, 1);
        }
    }

    @Override
    public String getStructureName() {
        return "Monument";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        int i = chunkX;
        int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.spacing - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.spacing - 1;
        }
        int k = chunkX / this.spacing;
        int l = chunkZ / this.spacing;
        Random random = this.worldObj.setRandomSeed(k, l, 10387313);
        k *= this.spacing;
        l *= this.spacing;
        if (i == (k += (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2) && j == (l += (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2)) {
            if (!this.worldObj.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 16, SPAWN_BIOMES)) {
                return false;
            }
            boolean flag = this.worldObj.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 29, WATER_BIOMES);
            if (flag) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean p_180706_3_) {
        this.worldObj = worldIn;
        return StructureOceanMonument.func_191069_a(worldIn, this, pos, this.spacing, this.separation, 10387313, true, 100, p_180706_3_);
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new StartMonument(this.worldObj, this.rand, chunkX, chunkZ);
    }

    public List<Biome.SpawnListEntry> getScatteredFeatureSpawnList() {
        return MONUMENT_ENEMIES;
    }

    static {
        MONUMENT_ENEMIES.add(new Biome.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
    }

    public static class StartMonument
    extends StructureStart {
        private final Set<ChunkPos> processed = Sets.newHashSet();
        private boolean wasCreated;

        public StartMonument() {
        }

        public StartMonument(World worldIn, Random random, int chunkX, int chunkZ) {
            super(chunkX, chunkZ);
            this.create(worldIn, random, chunkX, chunkZ);
        }

        private void create(World worldIn, Random random, int chunkX, int chunkZ) {
            random.setSeed(worldIn.getSeed());
            long i = random.nextLong();
            long j = random.nextLong();
            long k = (long)chunkX * i;
            long l = (long)chunkZ * j;
            random.setSeed(k ^ l ^ worldIn.getSeed());
            int i1 = chunkX * 16 + 8 - 29;
            int j1 = chunkZ * 16 + 8 - 29;
            EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(random);
            this.components.add(new StructureOceanMonumentPieces.MonumentBuilding(random, i1, j1, enumfacing));
            this.updateBoundingBox();
            this.wasCreated = true;
        }

        @Override
        public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb) {
            if (!this.wasCreated) {
                this.components.clear();
                this.create(worldIn, rand, this.getChunkPosX(), this.getChunkPosZ());
            }
            super.generateStructure(worldIn, rand, structurebb);
        }

        @Override
        public boolean isValidForPostProcess(ChunkPos pair) {
            return this.processed.contains(pair) ? false : super.isValidForPostProcess(pair);
        }

        @Override
        public void notifyPostProcessAt(ChunkPos pair) {
            super.notifyPostProcessAt(pair);
            this.processed.add(pair);
        }

        @Override
        public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            NBTTagList nbttaglist = new NBTTagList();
            for (ChunkPos chunkpos : this.processed) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setInteger("X", chunkpos.x);
                nbttagcompound.setInteger("Z", chunkpos.z);
                nbttaglist.appendTag(nbttagcompound);
            }
            tagCompound.setTag("Processed", nbttaglist);
        }

        @Override
        public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            if (tagCompound.hasKey("Processed", 9)) {
                NBTTagList nbttaglist = tagCompound.getTagList("Processed", 10);
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                    this.processed.add(new ChunkPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Z")));
                }
            }
        }
    }
}

