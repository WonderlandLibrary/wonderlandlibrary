// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import java.util.Map;

public class MapGenMineshaft extends MapGenStructure
{
    private double chance;
    
    public MapGenMineshaft() {
        this.chance = 0.004;
    }
    
    @Override
    public String getStructureName() {
        return "Mineshaft";
    }
    
    public MapGenMineshaft(final Map<String, String> p_i2034_1_) {
        this.chance = 0.004;
        for (final Map.Entry<String, String> entry : p_i2034_1_.entrySet()) {
            if (entry.getKey().equals("chance")) {
                this.chance = MathHelper.getDouble(entry.getValue(), this.chance);
            }
        }
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(final int chunkX, final int chunkZ) {
        return this.rand.nextDouble() < this.chance && this.rand.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkZ));
    }
    
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final BlockPos pos, final boolean findUnexplored) {
        final int i = 1000;
        final int j = pos.getX() >> 4;
        final int k = pos.getZ() >> 4;
        for (int l = 0; l <= 1000; ++l) {
            for (int i2 = -l; i2 <= l; ++i2) {
                final boolean flag = i2 == -l || i2 == l;
                for (int j2 = -l; j2 <= l; ++j2) {
                    final boolean flag2 = j2 == -l || j2 == l;
                    if (flag || flag2) {
                        final int k2 = j + i2;
                        final int l2 = k + j2;
                        this.rand.setSeed((long)(k2 ^ l2) ^ worldIn.getSeed());
                        this.rand.nextInt();
                        if (this.canSpawnStructureAtCoords(k2, l2) && (!findUnexplored || !worldIn.isChunkGeneratedAt(k2, l2))) {
                            return new BlockPos((k2 << 4) + 8, 64, (l2 << 4) + 8);
                        }
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    protected StructureStart getStructureStart(final int chunkX, final int chunkZ) {
        final Biome biome = this.world.getBiome(new BlockPos((chunkX << 4) + 8, 64, (chunkZ << 4) + 8));
        final Type mapgenmineshaft$type = (biome instanceof BiomeMesa) ? Type.MESA : Type.NORMAL;
        return new StructureMineshaftStart(this.world, this.rand, chunkX, chunkZ, mapgenmineshaft$type);
    }
    
    public enum Type
    {
        NORMAL, 
        MESA;
        
        public static Type byId(final int id) {
            return (id >= 0 && id < values().length) ? values()[id] : Type.NORMAL;
        }
    }
}
