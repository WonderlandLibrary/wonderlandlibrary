/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.passive;

import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityTameable
extends EntityAnimal
implements IEntityOwnable {
    protected EntityAISit aiSit = new EntityAISit(this);

    public boolean isTamed() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 4) != 0;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("showDeathMessages") && this.hasCustomName() && this.getOwner() instanceof EntityPlayerMP) {
            ((EntityPlayerMP)this.getOwner()).addChatMessage(this.getCombatTracker().getDeathMessage());
        }
        super.onDeath(damageSource);
    }

    public boolean isSitting() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public boolean shouldAttackEntity(EntityLivingBase entityLivingBase, EntityLivingBase entityLivingBase2) {
        return true;
    }

    protected void playTameEffect(boolean bl) {
        EnumParticleTypes enumParticleTypes = EnumParticleTypes.HEART;
        if (!bl) {
            enumParticleTypes = EnumParticleTypes.SMOKE_NORMAL;
        }
        int n = 0;
        while (n < 7) {
            double d = this.rand.nextGaussian() * 0.02;
            double d2 = this.rand.nextGaussian() * 0.02;
            double d3 = this.rand.nextGaussian() * 0.02;
            this.worldObj.spawnParticle(enumParticleTypes, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, d, d2, d3, new int[0]);
            ++n;
        }
    }

    public void setTamed(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        if (bl) {
            this.dataWatcher.updateObject(16, (byte)(by | 4));
        } else {
            this.dataWatcher.updateObject(16, (byte)(by & 0xFFFFFFFB));
        }
        this.setupTamedAI();
    }

    @Override
    public boolean isOnSameTeam(EntityLivingBase entityLivingBase) {
        if (this.isTamed()) {
            EntityLivingBase entityLivingBase2 = this.getOwner();
            if (entityLivingBase == entityLivingBase2) {
                return true;
            }
            if (entityLivingBase2 != null) {
                return entityLivingBase2.isOnSameTeam(entityLivingBase);
            }
        }
        return super.isOnSameTeam(entityLivingBase);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        String string = "";
        if (nBTTagCompound.hasKey("OwnerUUID", 8)) {
            string = nBTTagCompound.getString("OwnerUUID");
        } else {
            String string2 = nBTTagCompound.getString("Owner");
            string = PreYggdrasilConverter.getStringUUIDFromName(string2);
        }
        if (string.length() > 0) {
            this.setOwnerId(string);
            this.setTamed(true);
        }
        this.aiSit.setSitting(nBTTagCompound.getBoolean("Sitting"));
        this.setSitting(nBTTagCompound.getBoolean("Sitting"));
    }

    public void setSitting(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        if (bl) {
            this.dataWatcher.updateObject(16, (byte)(by | 1));
        } else {
            this.dataWatcher.updateObject(16, (byte)(by & 0xFFFFFFFE));
        }
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 7) {
            this.playTameEffect(true);
        } else if (by == 6) {
            this.playTameEffect(false);
        } else {
            super.handleStatusUpdate(by);
        }
    }

    @Override
    public EntityLivingBase getOwner() {
        try {
            UUID uUID = UUID.fromString(this.getOwnerId());
            return uUID == null ? null : this.worldObj.getPlayerEntityByUUID(uUID);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }

    public EntityAISit getAISit() {
        return this.aiSit;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)0);
        this.dataWatcher.addObject(17, "");
    }

    @Override
    public Team getTeam() {
        EntityLivingBase entityLivingBase;
        if (this.isTamed() && (entityLivingBase = this.getOwner()) != null) {
            return entityLivingBase.getTeam();
        }
        return super.getTeam();
    }

    public boolean isOwner(EntityLivingBase entityLivingBase) {
        return entityLivingBase == this.getOwner();
    }

    public EntityTameable(World world) {
        super(world);
        this.setupTamedAI();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        if (this.getOwnerId() == null) {
            nBTTagCompound.setString("OwnerUUID", "");
        } else {
            nBTTagCompound.setString("OwnerUUID", this.getOwnerId());
        }
        nBTTagCompound.setBoolean("Sitting", this.isSitting());
    }

    public void setOwnerId(String string) {
        this.dataWatcher.updateObject(17, string);
    }

    protected void setupTamedAI() {
    }

    @Override
    public String getOwnerId() {
        return this.dataWatcher.getWatchableObjectString(17);
    }
}

