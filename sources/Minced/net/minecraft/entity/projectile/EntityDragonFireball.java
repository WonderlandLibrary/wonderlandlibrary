// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.util.DamageSource;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityDragonFireball extends EntityFireball
{
    public EntityDragonFireball(final World worldIn) {
        super(worldIn);
        this.setSize(1.0f, 1.0f);
    }
    
    public EntityDragonFireball(final World worldIn, final double x, final double y, final double z, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(1.0f, 1.0f);
    }
    
    public EntityDragonFireball(final World worldIn, final EntityLivingBase shooter, final double accelX, final double accelY, final double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(1.0f, 1.0f);
    }
    
    public static void registerFixesDragonFireball(final DataFixer fixer) {
        EntityFireball.registerFixesFireball(fixer, "DragonFireball");
    }
    
    @Override
    protected void onImpact(final RayTraceResult result) {
        if ((result.entityHit == null || !result.entityHit.isEntityEqual(this.shootingEntity)) && !this.world.isRemote) {
            final List<EntityLivingBase> list = this.world.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, this.getEntityBoundingBox().grow(4.0, 2.0, 4.0));
            final EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
            entityareaeffectcloud.setOwner(this.shootingEntity);
            entityareaeffectcloud.setParticle(EnumParticleTypes.DRAGON_BREATH);
            entityareaeffectcloud.setRadius(3.0f);
            entityareaeffectcloud.setDuration(600);
            entityareaeffectcloud.setRadiusPerTick((7.0f - entityareaeffectcloud.getRadius()) / entityareaeffectcloud.getDuration());
            entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1));
            if (!list.isEmpty()) {
                for (final EntityLivingBase entitylivingbase : list) {
                    final double d0 = this.getDistanceSq(entitylivingbase);
                    if (d0 < 16.0) {
                        entityareaeffectcloud.setPosition(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ);
                        break;
                    }
                }
            }
            this.world.playEvent(2006, new BlockPos(this.posX, this.posY, this.posZ), 0);
            this.world.spawnEntity(entityareaeffectcloud);
            this.setDead();
        }
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return false;
    }
    
    @Override
    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.DRAGON_BREATH;
    }
    
    @Override
    protected boolean isFireballFiery() {
        return false;
    }
}
