// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.entity.monster.EntityGuardian;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Random;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import java.util.Map;
import java.util.List;

public class StructureOceanMonument extends MapGenStructure
{
    private int field_175800_f;
    private int field_175801_g;
    public static final List field_175802_d;
    private static final List field_175803_h;
    private static final String __OBFID = "CL_00001996";
    
    public StructureOceanMonument() {
        this.field_175800_f = 32;
        this.field_175801_g = 5;
    }
    
    public StructureOceanMonument(final Map p_i45608_1_) {
        this();
        for (final Map.Entry var3 : p_i45608_1_.entrySet()) {
            if (var3.getKey().equals("spacing")) {
                this.field_175800_f = MathHelper.parseIntWithDefaultAndMax(var3.getValue(), this.field_175800_f, 1);
            }
            else {
                if (!var3.getKey().equals("separation")) {
                    continue;
                }
                this.field_175801_g = MathHelper.parseIntWithDefaultAndMax(var3.getValue(), this.field_175801_g, 1);
            }
        }
    }
    
    @Override
    public String getStructureName() {
        return "Monument";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
        final int var3 = p_75047_1_;
        final int var4 = p_75047_2_;
        if (p_75047_1_ < 0) {
            p_75047_1_ -= this.field_175800_f - 1;
        }
        if (p_75047_2_ < 0) {
            p_75047_2_ -= this.field_175800_f - 1;
        }
        int var5 = p_75047_1_ / this.field_175800_f;
        int var6 = p_75047_2_ / this.field_175800_f;
        final Random var7 = this.worldObj.setRandomSeed(var5, var6, 10387313);
        var5 *= this.field_175800_f;
        var6 *= this.field_175800_f;
        var5 += (var7.nextInt(this.field_175800_f - this.field_175801_g) + var7.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
        var6 += (var7.nextInt(this.field_175800_f - this.field_175801_g) + var7.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
        if (var3 == var5 && var4 == var6) {
            if (this.worldObj.getWorldChunkManager().func_180300_a(new BlockPos(var3 * 16 + 8, 64, var4 * 16 + 8), null) != BiomeGenBase.deepOcean) {
                return false;
            }
            final boolean var8 = this.worldObj.getWorldChunkManager().areBiomesViable(var3 * 16 + 8, var4 * 16 + 8, 29, StructureOceanMonument.field_175802_d);
            if (var8) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected StructureStart getStructureStart(final int p_75049_1_, final int p_75049_2_) {
        return new StartMonument(this.worldObj, this.rand, p_75049_1_, p_75049_2_);
    }
    
    public List func_175799_b() {
        return StructureOceanMonument.field_175803_h;
    }
    
    static {
        field_175802_d = Arrays.asList(BiomeGenBase.ocean, BiomeGenBase.deepOcean, BiomeGenBase.river, BiomeGenBase.frozenOcean, BiomeGenBase.frozenRiver);
        (field_175803_h = Lists.newArrayList()).add(new BiomeGenBase.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
    }
    
    public static class StartMonument extends StructureStart
    {
        private Set field_175791_c;
        private boolean field_175790_d;
        private static final String __OBFID = "CL_00001995";
        
        public StartMonument() {
            this.field_175791_c = Sets.newHashSet();
        }
        
        public StartMonument(final World worldIn, final Random p_i45607_2_, final int p_i45607_3_, final int p_i45607_4_) {
            super(p_i45607_3_, p_i45607_4_);
            this.field_175791_c = Sets.newHashSet();
            this.func_175789_b(worldIn, p_i45607_2_, p_i45607_3_, p_i45607_4_);
        }
        
        private void func_175789_b(final World worldIn, final Random p_175789_2_, final int p_175789_3_, final int p_175789_4_) {
            p_175789_2_.setSeed(worldIn.getSeed());
            final long var5 = p_175789_2_.nextLong();
            final long var6 = p_175789_2_.nextLong();
            final long var7 = p_175789_3_ * var5;
            final long var8 = p_175789_4_ * var6;
            p_175789_2_.setSeed(var7 ^ var8 ^ worldIn.getSeed());
            final int var9 = p_175789_3_ * 16 + 8 - 29;
            final int var10 = p_175789_4_ * 16 + 8 - 29;
            final EnumFacing var11 = EnumFacing.Plane.HORIZONTAL.random(p_175789_2_);
            this.components.add(new StructureOceanMonumentPieces.MonumentBuilding(p_175789_2_, var9, var10, var11));
            this.updateBoundingBox();
            this.field_175790_d = true;
        }
        
        @Override
        public void generateStructure(final World worldIn, final Random p_75068_2_, final StructureBoundingBox p_75068_3_) {
            if (!this.field_175790_d) {
                this.components.clear();
                this.func_175789_b(worldIn, p_75068_2_, this.func_143019_e(), this.func_143018_f());
            }
            super.generateStructure(worldIn, p_75068_2_, p_75068_3_);
        }
        
        @Override
        public boolean func_175788_a(final ChunkCoordIntPair p_175788_1_) {
            return !this.field_175791_c.contains(p_175788_1_) && super.func_175788_a(p_175788_1_);
        }
        
        @Override
        public void func_175787_b(final ChunkCoordIntPair p_175787_1_) {
            super.func_175787_b(p_175787_1_);
            this.field_175791_c.add(p_175787_1_);
        }
        
        @Override
        public void func_143022_a(final NBTTagCompound p_143022_1_) {
            super.func_143022_a(p_143022_1_);
            final NBTTagList var2 = new NBTTagList();
            for (final ChunkCoordIntPair var4 : this.field_175791_c) {
                final NBTTagCompound var5 = new NBTTagCompound();
                var5.setInteger("X", var4.chunkXPos);
                var5.setInteger("Z", var4.chunkZPos);
                var2.appendTag(var5);
            }
            p_143022_1_.setTag("Processed", var2);
        }
        
        @Override
        public void func_143017_b(final NBTTagCompound p_143017_1_) {
            super.func_143017_b(p_143017_1_);
            if (p_143017_1_.hasKey("Processed", 9)) {
                final NBTTagList var2 = p_143017_1_.getTagList("Processed", 10);
                for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                    final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                    this.field_175791_c.add(new ChunkCoordIntPair(var4.getInteger("X"), var4.getInteger("Z")));
                }
            }
        }
    }
}
