// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.entity.monster.EntityBlaze;
import com.google.common.collect.Lists;
import java.util.List;

public class MapGenNetherBridge extends MapGenStructure
{
    private List spawnList;
    private static final String __OBFID = "CL_00000451";
    
    public MapGenNetherBridge() {
        (this.spawnList = Lists.newArrayList()).add(new BiomeGenBase.SpawnListEntry(EntityBlaze.class, 10, 2, 3));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
        this.spawnList.add(new BiomeGenBase.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
    }
    
    @Override
    public String getStructureName() {
        return "Fortress";
    }
    
    public List getSpawnList() {
        return this.spawnList;
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(final int p_75047_1_, final int p_75047_2_) {
        final int var3 = p_75047_1_ >> 4;
        final int var4 = p_75047_2_ >> 4;
        this.rand.setSeed(var3 ^ var4 << 4 ^ this.worldObj.getSeed());
        this.rand.nextInt();
        return this.rand.nextInt(3) == 0 && p_75047_1_ == (var3 << 4) + 4 + this.rand.nextInt(8) && p_75047_2_ == (var4 << 4) + 4 + this.rand.nextInt(8);
    }
    
    @Override
    protected StructureStart getStructureStart(final int p_75049_1_, final int p_75049_2_) {
        return new Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_);
    }
    
    public static class Start extends StructureStart
    {
        private static final String __OBFID = "CL_00000452";
        
        public Start() {
        }
        
        public Start(final World worldIn, final Random p_i2040_2_, final int p_i2040_3_, final int p_i2040_4_) {
            super(p_i2040_3_, p_i2040_4_);
            final StructureNetherBridgePieces.Start var5 = new StructureNetherBridgePieces.Start(p_i2040_2_, (p_i2040_3_ << 4) + 2, (p_i2040_4_ << 4) + 2);
            this.components.add(var5);
            var5.buildComponent(var5, this.components, p_i2040_2_);
            final List var6 = var5.field_74967_d;
            while (!var6.isEmpty()) {
                final int var7 = p_i2040_2_.nextInt(var6.size());
                final StructureComponent var8 = var6.remove(var7);
                var8.buildComponent(var5, this.components, p_i2040_2_);
            }
            this.updateBoundingBox();
            this.setRandomHeight(worldIn, p_i2040_2_, 48, 70);
        }
    }
}
