// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import net.minecraft.world.World;
import java.util.ArrayList;
import net.minecraft.util.BlockPos;
import java.util.Random;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import java.util.Map;
import net.minecraft.world.biome.BiomeGenBase;
import com.google.common.collect.Lists;
import net.minecraft.world.ChunkCoordIntPair;
import java.util.List;

public class MapGenStronghold extends MapGenStructure
{
    private List field_151546_e;
    private boolean ranBiomeCheck;
    private ChunkCoordIntPair[] structureCoords;
    private double field_82671_h;
    private int field_82672_i;
    private static final String __OBFID = "CL_00000481";
    
    public MapGenStronghold() {
        this.structureCoords = new ChunkCoordIntPair[3];
        this.field_82671_h = 32.0;
        this.field_82672_i = 3;
        this.field_151546_e = Lists.newArrayList();
        for (final BiomeGenBase var4 : BiomeGenBase.getBiomeGenArray()) {
            if (var4 != null && var4.minHeight > 0.0f) {
                this.field_151546_e.add(var4);
            }
        }
    }
    
    public MapGenStronghold(final Map p_i2068_1_) {
        this();
        for (final Map.Entry var3 : p_i2068_1_.entrySet()) {
            if (var3.getKey().equals("distance")) {
                this.field_82671_h = MathHelper.parseDoubleWithDefaultAndMax(var3.getValue(), this.field_82671_h, 1.0);
            }
            else if (var3.getKey().equals("count")) {
                this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax(var3.getValue(), this.structureCoords.length, 1)];
            }
            else {
                if (!var3.getKey().equals("spread")) {
                    continue;
                }
                this.field_82672_i = MathHelper.parseIntWithDefaultAndMax(var3.getValue(), this.field_82672_i, 1);
            }
        }
    }
    
    @Override
    public String getStructureName() {
        return "Stronghold";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(final int p_75047_1_, final int p_75047_2_) {
        if (!this.ranBiomeCheck) {
            final Random var3 = new Random();
            var3.setSeed(this.worldObj.getSeed());
            double var4 = var3.nextDouble() * 3.141592653589793 * 2.0;
            int var5 = 1;
            for (int var6 = 0; var6 < this.structureCoords.length; ++var6) {
                final double var7 = (1.25 * var5 + var3.nextDouble()) * this.field_82671_h * var5;
                int var8 = (int)Math.round(Math.cos(var4) * var7);
                int var9 = (int)Math.round(Math.sin(var4) * var7);
                final BlockPos var10 = this.worldObj.getWorldChunkManager().findBiomePosition((var8 << 4) + 8, (var9 << 4) + 8, 112, this.field_151546_e, var3);
                if (var10 != null) {
                    var8 = var10.getX() >> 4;
                    var9 = var10.getZ() >> 4;
                }
                this.structureCoords[var6] = new ChunkCoordIntPair(var8, var9);
                var4 += 6.283185307179586 * var5 / this.field_82672_i;
                if (var6 == this.field_82672_i) {
                    var5 += 2 + var3.nextInt(5);
                    this.field_82672_i += 1 + var3.nextInt(2);
                }
            }
            this.ranBiomeCheck = true;
        }
        for (final ChunkCoordIntPair var14 : this.structureCoords) {
            if (p_75047_1_ == var14.chunkXPos && p_75047_2_ == var14.chunkZPos) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected List getCoordList() {
        final ArrayList var1 = Lists.newArrayList();
        for (final ChunkCoordIntPair var5 : this.structureCoords) {
            if (var5 != null) {
                var1.add(var5.getCenterBlock(64));
            }
        }
        return var1;
    }
    
    @Override
    protected StructureStart getStructureStart(final int p_75049_1_, final int p_75049_2_) {
        Start var3;
        for (var3 = new Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_); var3.getComponents().isEmpty() || var3.getComponents().get(0).strongholdPortalRoom == null; var3 = new Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_)) {}
        return var3;
    }
    
    public static class Start extends StructureStart
    {
        private static final String __OBFID = "CL_00000482";
        
        public Start() {
        }
        
        public Start(final World worldIn, final Random p_i2067_2_, final int p_i2067_3_, final int p_i2067_4_) {
            super(p_i2067_3_, p_i2067_4_);
            StructureStrongholdPieces.prepareStructurePieces();
            final StructureStrongholdPieces.Stairs2 var5 = new StructureStrongholdPieces.Stairs2(0, p_i2067_2_, (p_i2067_3_ << 4) + 2, (p_i2067_4_ << 4) + 2);
            this.components.add(var5);
            var5.buildComponent(var5, this.components, p_i2067_2_);
            final List var6 = var5.field_75026_c;
            while (!var6.isEmpty()) {
                final int var7 = p_i2067_2_.nextInt(var6.size());
                final StructureComponent var8 = var6.remove(var7);
                var8.buildComponent(var5, this.components, p_i2067_2_);
            }
            this.updateBoundingBox();
            this.markAvailableHeight(worldIn, p_i2067_2_, 10);
        }
    }
}
