// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;

public class EntityEnderPearl extends EntityThrowable
{
    private EntityLivingBase perlThrower;
    
    public EntityEnderPearl(final World worldIn) {
        super(worldIn);
    }
    
    public EntityEnderPearl(final World worldIn, final EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
        this.perlThrower = throwerIn;
    }
    
    public EntityEnderPearl(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
    }
    
    public static void registerFixesEnderPearl(final DataFixer fixer) {
        EntityThrowable.registerFixesThrowable(fixer, "ThrownEnderpearl");
    }
    
    @Override
    protected void onImpact(final RayTraceResult result) {
        final EntityLivingBase entitylivingbase = this.getThrower();
        if (result.entityHit != null) {
            if (result.entityHit == this.perlThrower) {
                return;
            }
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase), 0.0f);
        }
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            final BlockPos blockpos = result.getBlockPos();
            final TileEntity tileentity = this.world.getTileEntity(blockpos);
            if (tileentity instanceof TileEntityEndGateway) {
                final TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;
                if (entitylivingbase != null) {
                    if (entitylivingbase instanceof EntityPlayerMP) {
                        CriteriaTriggers.ENTER_BLOCK.trigger((EntityPlayerMP)entitylivingbase, this.world.getBlockState(blockpos));
                    }
                    tileentityendgateway.teleportEntity(entitylivingbase);
                    this.setDead();
                    return;
                }
                tileentityendgateway.teleportEntity(this);
                return;
            }
        }
        for (int i = 0; i < 32; ++i) {
            this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0, this.posZ, this.rand.nextGaussian(), 0.0, this.rand.nextGaussian(), new int[0]);
        }
        if (!this.world.isRemote) {
            if (entitylivingbase instanceof EntityPlayerMP) {
                final EntityPlayerMP entityplayermp = (EntityPlayerMP)entitylivingbase;
                if (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.world == this.world && !entityplayermp.isPlayerSleeping()) {
                    if (this.rand.nextFloat() < 0.05f && this.world.getGameRules().getBoolean("doMobSpawning")) {
                        final EntityEndermite entityendermite = new EntityEndermite(this.world);
                        entityendermite.setSpawnedByPlayer(true);
                        entityendermite.setLocationAndAngles(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, entitylivingbase.rotationYaw, entitylivingbase.rotationPitch);
                        this.world.spawnEntity(entityendermite);
                    }
                    if (entitylivingbase.isRiding()) {
                        entitylivingbase.dismountRidingEntity();
                    }
                    entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    entitylivingbase.fallDistance = 0.0f;
                    entitylivingbase.attackEntityFrom(DamageSource.FALL, 5.0f);
                }
            }
            else if (entitylivingbase != null) {
                entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                entitylivingbase.fallDistance = 0.0f;
            }
            this.setDead();
        }
    }
    
    @Override
    public void onUpdate() {
        final EntityLivingBase entitylivingbase = this.getThrower();
        if (entitylivingbase != null && entitylivingbase instanceof EntityPlayer && !entitylivingbase.isEntityAlive()) {
            this.setDead();
        }
        else {
            super.onUpdate();
        }
    }
    
    @Nullable
    @Override
    public Entity changeDimension(final int dimensionIn) {
        if (this.thrower.dimension != dimensionIn) {
            this.thrower = null;
        }
        return super.changeDimension(dimensionIn);
    }
}
