// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.math.BlockPos;
import java.util.UUID;
import net.minecraft.entity.Entity;

public class RandomEntity implements IRandomEntity
{
    private Entity entity;
    
    @Override
    public int getId() {
        final UUID uuid = this.entity.getUniqueID();
        final long i = uuid.getLeastSignificantBits();
        final int j = (int)(i & 0x7FFFFFFFL);
        return j;
    }
    
    @Override
    public BlockPos getSpawnPosition() {
        return this.entity.getDataManager().spawnPosition;
    }
    
    @Override
    public Biome getSpawnBiome() {
        return this.entity.getDataManager().spawnBiome;
    }
    
    @Override
    public String getName() {
        return this.entity.hasCustomName() ? this.entity.getCustomNameTag() : null;
    }
    
    @Override
    public int getHealth() {
        if (!(this.entity instanceof EntityLiving)) {
            return 0;
        }
        final EntityLiving entityliving = (EntityLiving)this.entity;
        return (int)entityliving.getHealth();
    }
    
    @Override
    public int getMaxHealth() {
        if (!(this.entity instanceof EntityLiving)) {
            return 0;
        }
        final EntityLiving entityliving = (EntityLiving)this.entity;
        return (int)entityliving.getMaxHealth();
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }
}
