// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import java.util.Arrays;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.Random;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import java.util.Map;
import java.util.List;

public class MapGenVillage extends MapGenStructure
{
    public static final List villageSpawnBiomes;
    private int terrainType;
    private int field_82665_g;
    private int field_82666_h;
    private static final String __OBFID = "CL_00000514";
    
    public MapGenVillage() {
        this.field_82665_g = 32;
        this.field_82666_h = 8;
    }
    
    public MapGenVillage(final Map p_i2093_1_) {
        this();
        for (final Map.Entry var3 : p_i2093_1_.entrySet()) {
            if (var3.getKey().equals("size")) {
                this.terrainType = MathHelper.parseIntWithDefaultAndMax(var3.getValue(), this.terrainType, 0);
            }
            else {
                if (!var3.getKey().equals("distance")) {
                    continue;
                }
                this.field_82665_g = MathHelper.parseIntWithDefaultAndMax(var3.getValue(), this.field_82665_g, this.field_82666_h + 1);
            }
        }
    }
    
    @Override
    public String getStructureName() {
        return "Village";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
        final int var3 = p_75047_1_;
        final int var4 = p_75047_2_;
        if (p_75047_1_ < 0) {
            p_75047_1_ -= this.field_82665_g - 1;
        }
        if (p_75047_2_ < 0) {
            p_75047_2_ -= this.field_82665_g - 1;
        }
        int var5 = p_75047_1_ / this.field_82665_g;
        int var6 = p_75047_2_ / this.field_82665_g;
        final Random var7 = this.worldObj.setRandomSeed(var5, var6, 10387312);
        var5 *= this.field_82665_g;
        var6 *= this.field_82665_g;
        var5 += var7.nextInt(this.field_82665_g - this.field_82666_h);
        var6 += var7.nextInt(this.field_82665_g - this.field_82666_h);
        if (var3 == var5 && var4 == var6) {
            final boolean var8 = this.worldObj.getWorldChunkManager().areBiomesViable(var3 * 16 + 8, var4 * 16 + 8, 0, MapGenVillage.villageSpawnBiomes);
            if (var8) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected StructureStart getStructureStart(final int p_75049_1_, final int p_75049_2_) {
        return new Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_, this.terrainType);
    }
    
    static {
        villageSpawnBiomes = Arrays.asList(BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.savanna);
    }
    
    public static class Start extends StructureStart
    {
        private boolean hasMoreThanTwoComponents;
        private static final String __OBFID = "CL_00000515";
        
        public Start() {
        }
        
        public Start(final World worldIn, final Random p_i2092_2_, final int p_i2092_3_, final int p_i2092_4_, final int p_i2092_5_) {
            super(p_i2092_3_, p_i2092_4_);
            final List var6 = StructureVillagePieces.getStructureVillageWeightedPieceList(p_i2092_2_, p_i2092_5_);
            final StructureVillagePieces.Start var7 = new StructureVillagePieces.Start(worldIn.getWorldChunkManager(), 0, p_i2092_2_, (p_i2092_3_ << 4) + 2, (p_i2092_4_ << 4) + 2, var6, p_i2092_5_);
            this.components.add(var7);
            var7.buildComponent(var7, this.components, p_i2092_2_);
            final List var8 = var7.field_74930_j;
            final List var9 = var7.field_74932_i;
            while (!var8.isEmpty() || !var9.isEmpty()) {
                if (var8.isEmpty()) {
                    final int var10 = p_i2092_2_.nextInt(var9.size());
                    final StructureComponent var11 = var9.remove(var10);
                    var11.buildComponent(var7, this.components, p_i2092_2_);
                }
                else {
                    final int var10 = p_i2092_2_.nextInt(var8.size());
                    final StructureComponent var11 = var8.remove(var10);
                    var11.buildComponent(var7, this.components, p_i2092_2_);
                }
            }
            this.updateBoundingBox();
            int var10 = 0;
            for (final StructureComponent var13 : this.components) {
                if (!(var13 instanceof StructureVillagePieces.Road)) {
                    ++var10;
                }
            }
            this.hasMoreThanTwoComponents = (var10 > 2);
        }
        
        @Override
        public boolean isSizeableStructure() {
            return this.hasMoreThanTwoComponents;
        }
        
        @Override
        public void func_143022_a(final NBTTagCompound p_143022_1_) {
            super.func_143022_a(p_143022_1_);
            p_143022_1_.setBoolean("Valid", this.hasMoreThanTwoComponents);
        }
        
        @Override
        public void func_143017_b(final NBTTagCompound p_143017_1_) {
            super.func_143017_b(p_143017_1_);
            this.hasMoreThanTwoComponents = p_143017_1_.getBoolean("Valid");
        }
    }
}
