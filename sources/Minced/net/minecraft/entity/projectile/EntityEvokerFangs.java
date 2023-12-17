// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import java.util.Iterator;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

public class EntityEvokerFangs extends Entity
{
    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks;
    private boolean clientSideAttackStarted;
    private EntityLivingBase caster;
    private UUID casterUuid;
    
    public EntityEvokerFangs(final World worldIn) {
        super(worldIn);
        this.lifeTicks = 22;
        this.setSize(0.5f, 0.8f);
    }
    
    public EntityEvokerFangs(final World worldIn, final double x, final double y, final double z, final float p_i47276_8_, final int p_i47276_9_, final EntityLivingBase casterIn) {
        this(worldIn);
        this.warmupDelayTicks = p_i47276_9_;
        this.setCaster(casterIn);
        this.rotationYaw = p_i47276_8_ * 57.295776f;
        this.setPosition(x, y, z);
    }
    
    @Override
    protected void entityInit() {
    }
    
    public void setCaster(@Nullable final EntityLivingBase p_190549_1_) {
        this.caster = p_190549_1_;
        this.casterUuid = ((p_190549_1_ == null) ? null : p_190549_1_.getUniqueID());
    }
    
    @Nullable
    public EntityLivingBase getCaster() {
        if (this.caster == null && this.casterUuid != null && this.world instanceof WorldServer) {
            final Entity entity = ((WorldServer)this.world).getEntityFromUuid(this.casterUuid);
            if (entity instanceof EntityLivingBase) {
                this.caster = (EntityLivingBase)entity;
            }
        }
        return this.caster;
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        this.warmupDelayTicks = compound.getInteger("Warmup");
        this.casterUuid = compound.getUniqueId("OwnerUUID");
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setInteger("Warmup", this.warmupDelayTicks);
        if (this.casterUuid != null) {
            compound.setUniqueId("OwnerUUID", this.casterUuid);
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (this.lifeTicks == 14) {
                    for (int i = 0; i < 12; ++i) {
                        final double d0 = this.posX + (this.rand.nextDouble() * 2.0 - 1.0) * this.width * 0.5;
                        final double d2 = this.posY + 0.05 + this.rand.nextDouble() * 1.0;
                        final double d3 = this.posZ + (this.rand.nextDouble() * 2.0 - 1.0) * this.width * 0.5;
                        final double d4 = (this.rand.nextDouble() * 2.0 - 1.0) * 0.3;
                        final double d5 = 0.3 + this.rand.nextDouble() * 0.3;
                        final double d6 = (this.rand.nextDouble() * 2.0 - 1.0) * 0.3;
                        this.world.spawnParticle(EnumParticleTypes.CRIT, d0, d2 + 1.0, d3, d4, d5, d6, new int[0]);
                    }
                }
            }
        }
        else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -8) {
                for (final EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, this.getEntityBoundingBox().grow(0.2, 0.0, 0.2))) {
                    this.damage(entitylivingbase);
                }
            }
            if (!this.sentSpikeEvent) {
                this.world.setEntityState(this, (byte)4);
                this.sentSpikeEvent = true;
            }
            if (--this.lifeTicks < 0) {
                this.setDead();
            }
        }
    }
    
    private void damage(final EntityLivingBase p_190551_1_) {
        final EntityLivingBase entitylivingbase = this.getCaster();
        if (p_190551_1_.isEntityAlive() && !p_190551_1_.getIsInvulnerable() && p_190551_1_ != entitylivingbase) {
            if (entitylivingbase == null) {
                p_190551_1_.attackEntityFrom(DamageSource.MAGIC, 6.0f);
            }
            else {
                if (entitylivingbase.isOnSameTeam(p_190551_1_)) {
                    return;
                }
                p_190551_1_.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, entitylivingbase), 6.0f);
            }
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        super.handleStatusUpdate(id);
        if (id == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent()) {
                this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.EVOCATION_FANGS_ATTACK, this.getSoundCategory(), 1.0f, this.rand.nextFloat() * 0.2f + 0.85f, false);
            }
        }
    }
    
    public float getAnimationProgress(final float partialTicks) {
        if (!this.clientSideAttackStarted) {
            return 0.0f;
        }
        final int i = this.lifeTicks - 2;
        return (i <= 0) ? 1.0f : (1.0f - (i - partialTicks) / 20.0f);
    }
}
