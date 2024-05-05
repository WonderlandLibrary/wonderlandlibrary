package net.minecraft.world.gen.structure;

import com.google.common.collect.Sets;
import java.util.Random;
import java.util.Set;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;
import net.minecraft.world.gen.structure.StructureStart;

public static class StructureOceanMonument.StartMonument
extends StructureStart {
    private Set<ChunkCoordIntPair> field_175791_c = Sets.newHashSet();
    private boolean field_175790_d;

    public StructureOceanMonument.StartMonument() {
    }

    public StructureOceanMonument.StartMonument(World worldIn, Random p_i45607_2_, int p_i45607_3_, int p_i45607_4_) {
        super(p_i45607_3_, p_i45607_4_);
        this.func_175789_b(worldIn, p_i45607_2_, p_i45607_3_, p_i45607_4_);
    }

    private void func_175789_b(World worldIn, Random p_175789_2_, int p_175789_3_, int p_175789_4_) {
        p_175789_2_.setSeed(worldIn.getSeed());
        long i = p_175789_2_.nextLong();
        long j = p_175789_2_.nextLong();
        long k = (long)p_175789_3_ * i;
        long l = (long)p_175789_4_ * j;
        p_175789_2_.setSeed(k ^ l ^ worldIn.getSeed());
        int i1 = p_175789_3_ * 16 + 8 - 29;
        int j1 = p_175789_4_ * 16 + 8 - 29;
        EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(p_175789_2_);
        this.components.add((Object)new StructureOceanMonumentPieces.MonumentBuilding(p_175789_2_, i1, j1, enumfacing));
        this.updateBoundingBox();
        this.field_175790_d = true;
    }

    public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb) {
        if (!this.field_175790_d) {
            this.components.clear();
            this.func_175789_b(worldIn, rand, this.getChunkPosX(), this.getChunkPosZ());
        }
        super.generateStructure(worldIn, rand, structurebb);
    }

    public boolean func_175788_a(ChunkCoordIntPair pair) {
        return this.field_175791_c.contains((Object)pair) ? false : super.func_175788_a(pair);
    }

    public void func_175787_b(ChunkCoordIntPair pair) {
        super.func_175787_b(pair);
        this.field_175791_c.add((Object)pair);
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagList nbttaglist = new NBTTagList();
        for (ChunkCoordIntPair chunkcoordintpair : this.field_175791_c) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", chunkcoordintpair.chunkXPos);
            nbttagcompound.setInteger("Z", chunkcoordintpair.chunkZPos);
            nbttaglist.appendTag((NBTBase)nbttagcompound);
        }
        tagCompound.setTag("Processed", (NBTBase)nbttaglist);
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (tagCompound.hasKey("Processed", 9)) {
            NBTTagList nbttaglist = tagCompound.getTagList("Processed", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                this.field_175791_c.add((Object)new ChunkCoordIntPair(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Z")));
            }
        }
    }
}
