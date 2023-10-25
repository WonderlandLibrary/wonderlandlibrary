/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEnderPearl
extends EntityThrowable {
    private EntityLivingBase field_181555_c;

    public EntityEnderPearl(World world, EntityLivingBase entityLivingBase) {
        super(world, entityLivingBase);
        this.field_181555_c = entityLivingBase;
    }

    @Override
    public void onUpdate() {
        EntityLivingBase entityLivingBase = this.getThrower();
        if (entityLivingBase != null && entityLivingBase instanceof EntityPlayer && !entityLivingBase.isEntityAlive()) {
            this.setDead();
        } else {
            super.onUpdate();
        }
    }

    public EntityEnderPearl(World world, double d, double d2, double d3) {
        super(world, d, d2, d3);
    }

    public EntityEnderPearl(World world) {
        super(world);
    }

    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        EntityLivingBase entityLivingBase = this.getThrower();
        if (movingObjectPosition.entityHit != null) {
            if (movingObjectPosition.entityHit == this.field_181555_c) {
                return;
            }
            movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, entityLivingBase), 0.0f);
        }
        int n = 0;
        while (n < 32) {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0, this.posZ, this.rand.nextGaussian(), 0.0, this.rand.nextGaussian(), new int[0]);
            ++n;
        }
        if (!this.worldObj.isRemote) {
            if (entityLivingBase instanceof EntityPlayerMP) {
                EntityPlayerMP entityPlayerMP = (EntityPlayerMP)entityLivingBase;
                if (entityPlayerMP.playerNetServerHandler.getNetworkManager().isChannelOpen() && entityPlayerMP.worldObj == this.worldObj && !entityPlayerMP.isPlayerSleeping()) {
                    if (this.rand.nextFloat() < 0.05f && this.worldObj.getGameRules().getBoolean("doMobSpawning")) {
                        EntityEndermite entityEndermite = new EntityEndermite(this.worldObj);
                        entityEndermite.setSpawnedByPlayer(true);
                        entityEndermite.setLocationAndAngles(entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, entityLivingBase.rotationYaw, entityLivingBase.rotationPitch);
                        this.worldObj.spawnEntityInWorld(entityEndermite);
                    }
                    if (entityLivingBase.isRiding()) {
                        entityLivingBase.mountEntity(null);
                    }
                    entityLivingBase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    entityLivingBase.fallDistance = 0.0f;
                    entityLivingBase.attackEntityFrom(DamageSource.fall, 5.0f);
                }
            } else if (entityLivingBase != null) {
                entityLivingBase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                entityLivingBase.fallDistance = 0.0f;
            }
            this.setDead();
        }
    }
}

