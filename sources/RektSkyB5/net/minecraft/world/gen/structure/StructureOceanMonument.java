/*
 * Decompiled with CFR 0.152.
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;
import net.minecraft.world.gen.structure.StructureStart;

public class StructureOceanMonument
extends MapGenStructure {
    private int field_175800_f = 32;
    private int field_175801_g = 5;
    public static final List<BiomeGenBase> field_175802_d = Arrays.asList(BiomeGenBase.ocean, BiomeGenBase.deepOcean, BiomeGenBase.river, BiomeGenBase.frozenOcean, BiomeGenBase.frozenRiver);
    private static final List<BiomeGenBase.SpawnListEntry> field_175803_h = Lists.newArrayList();

    public StructureOceanMonument() {
    }

    public StructureOceanMonument(Map<String, String> p_i45608_1_) {
        this();
        for (Map.Entry<String, String> entry : p_i45608_1_.entrySet()) {
            if (entry.getKey().equals("spacing")) {
                this.field_175800_f = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.field_175800_f, 1);
                continue;
            }
            if (!entry.getKey().equals("separation")) continue;
            this.field_175801_g = MathHelper.parseIntWithDefaultAndMax(entry.getValue(), this.field_175801_g, 1);
        }
    }

    @Override
    public String getStructureName() {
        return "Monument";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        int i2 = chunkX;
        int j2 = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.field_175800_f - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.field_175800_f - 1;
        }
        int k2 = chunkX / this.field_175800_f;
        int l2 = chunkZ / this.field_175800_f;
        Random random = this.worldObj.setRandomSeed(k2, l2, 10387313);
        k2 *= this.field_175800_f;
        l2 *= this.field_175800_f;
        if (i2 == (k2 += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2) && j2 == (l2 += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2)) {
            if (this.worldObj.getWorldChunkManager().getBiomeGenerator(new BlockPos(i2 * 16 + 8, 64, j2 * 16 + 8), null) != BiomeGenBase.deepOcean) {
                return false;
            }
            boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(i2 * 16 + 8, j2 * 16 + 8, 29, field_175802_d);
            if (flag) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new StartMonument(this.worldObj, this.rand, chunkX, chunkZ);
    }

    public List<BiomeGenBase.SpawnListEntry> func_175799_b() {
        return field_175803_h;
    }

    static {
        field_175803_h.add(new BiomeGenBase.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
    }

    public static class StartMonument
    extends StructureStart {
        private Set<ChunkCoordIntPair> field_175791_c = Sets.newHashSet();
        private boolean field_175790_d;

        public StartMonument() {
        }

        public StartMonument(World worldIn, Random p_i45607_2_, int p_i45607_3_, int p_i45607_4_) {
            super(p_i45607_3_, p_i45607_4_);
            this.func_175789_b(worldIn, p_i45607_2_, p_i45607_3_, p_i45607_4_);
        }

        private void func_175789_b(World worldIn, Random p_175789_2_, int p_175789_3_, int p_175789_4_) {
            p_175789_2_.setSeed(worldIn.getSeed());
            long i2 = p_175789_2_.nextLong();
            long j2 = p_175789_2_.nextLong();
            long k2 = (long)p_175789_3_ * i2;
            long l2 = (long)p_175789_4_ * j2;
            p_175789_2_.setSeed(k2 ^ l2 ^ worldIn.getSeed());
            int i1 = p_175789_3_ * 16 + 8 - 29;
            int j1 = p_175789_4_ * 16 + 8 - 29;
            EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(p_175789_2_);
            this.components.add(new StructureOceanMonumentPieces.MonumentBuilding(p_175789_2_, i1, j1, enumfacing));
            this.updateBoundingBox();
            this.field_175790_d = true;
        }

        @Override
        public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb) {
            if (!this.field_175790_d) {
                this.components.clear();
                this.func_175789_b(worldIn, rand, this.getChunkPosX(), this.getChunkPosZ());
            }
            super.generateStructure(worldIn, rand, structurebb);
        }

        @Override
        public boolean func_175788_a(ChunkCoordIntPair pair) {
            return this.field_175791_c.contains(pair) ? false : super.func_175788_a(pair);
        }

        @Override
        public void func_175787_b(ChunkCoordIntPair pair) {
            super.func_175787_b(pair);
            this.field_175791_c.add(pair);
        }

        @Override
        public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            NBTTagList nbttaglist = new NBTTagList();
            for (ChunkCoordIntPair chunkcoordintpair : this.field_175791_c) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setInteger("X", chunkcoordintpair.chunkXPos);
                nbttagcompound.setInteger("Z", chunkcoordintpair.chunkZPos);
                nbttaglist.appendTag(nbttagcompound);
            }
            tagCompound.setTag("Processed", nbttaglist);
        }

        @Override
        public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            if (tagCompound.hasKey("Processed", 9)) {
                NBTTagList nbttaglist = tagCompound.getTagList("Processed", 10);
                for (int i2 = 0; i2 < nbttaglist.tagCount(); ++i2) {
                    NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i2);
                    this.field_175791_c.add(new ChunkCoordIntPair(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Z")));
                }
            }
        }
    }
}

