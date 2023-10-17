package net.optifine;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class RandomEntity implements IRandomEntity
{
    private Entity entity;

    public int getId()
    {
        UUID uuid = this.entity.getUniqueID();
        long i = uuid.getLeastSignificantBits();
        int j = (int)(i & 2147483647L);
        return j;
    }

    public BlockPos getSpawnPosition()
    {
        return this.entity.getDataManager().spawnPosition;
    }

    public Biome getSpawnBiome()
    {
        return this.entity.getDataManager().spawnBiome;
    }

    public String getName()
    {
        return this.entity.hasCustomName() ? this.entity.getCustomNameTag() : null;
    }

    public int getHealth()
    {
        if (!(this.entity instanceof EntityLiving))
        {
            return 0;
        }
        else
        {
            EntityLiving entityliving = (EntityLiving)this.entity;
            return (int)entityliving.getHealth();
        }
    }

    public int getMaxHealth()
    {
        if (!(this.entity instanceof EntityLiving))
        {
            return 0;
        }
        else
        {
            EntityLiving entityliving = (EntityLiving)this.entity;
            return (int)entityliving.getMaxHealth();
        }
    }

    public Entity getEntity()
    {
        return this.entity;
    }

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }
}
