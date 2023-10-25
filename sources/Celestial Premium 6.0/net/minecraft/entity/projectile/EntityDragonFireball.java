/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDragonFireball
extends EntityFireball {
    public EntityDragonFireball(World worldIn) {
        super(worldIn);
        this.setSize(1.0f, 1.0f);
    }

    public EntityDragonFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(1.0f, 1.0f);
    }

    public EntityDragonFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(1.0f, 1.0f);
    }

    public static void registerFixesDragonFireball(DataFixer fixer) {
        EntityFireball.registerFixesFireball(fixer, "DragonFireball");
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!(result.entityHit != null && result.entityHit.isEntityEqual(this.shootingEntity) || this.world.isRemote)) {
            List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(4.0, 2.0, 4.0));
            EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
            entityareaeffectcloud.setOwner(this.shootingEntity);
            entityareaeffectcloud.setParticle(EnumParticleTypes.DRAGON_BREATH);
            entityareaeffectcloud.setRadius(3.0f);
            entityareaeffectcloud.setDuration(600);
            entityareaeffectcloud.setRadiusPerTick((7.0f - entityareaeffectcloud.getRadius()) / (float)entityareaeffectcloud.getDuration());
            entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1));
            if (!list.isEmpty()) {
                for (EntityLivingBase entitylivingbase : list) {
                    double d0 = this.getDistanceSqToEntity(entitylivingbase);
                    if (!(d0 < 16.0)) continue;
                    entityareaeffectcloud.setPosition(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ);
                    break;
                }
            }
            this.world.playEvent(2006, new BlockPos(this.posX, this.posY, this.posZ), 0);
            this.world.spawnEntityInWorld(entityareaeffectcloud);
            this.setDead();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
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

