// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntitySnowball extends EntityThrowable
{
    public EntitySnowball(final World worldIn) {
        super(worldIn);
    }
    
    public EntitySnowball(final World worldIn, final EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }
    
    public EntitySnowball(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
    }
    
    public static void registerFixesSnowball(final DataFixer fixer) {
        EntityThrowable.registerFixesThrowable(fixer, "Snowball");
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }
    
    @Override
    protected void onImpact(final RayTraceResult result) {
        if (result.entityHit != null) {
            int i = 0;
            if (result.entityHit instanceof EntityBlaze) {
                i = 3;
            }
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)i);
        }
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.setDead();
        }
    }
}
