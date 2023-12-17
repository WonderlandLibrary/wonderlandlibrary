// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import javax.annotation.Nullable;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAISit;
import java.util.UUID;
import com.google.common.base.Optional;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.IEntityOwnable;

public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable
{
    protected static final DataParameter<Byte> TAMED;
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID;
    protected EntityAISit aiSit;
    
    public EntityTameable(final World worldIn) {
        super(worldIn);
        this.setupTamedAI();
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityTameable.TAMED, (Byte)0);
        this.dataManager.register(EntityTameable.OWNER_UNIQUE_ID, (Optional<UUID>)Optional.absent());
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.getOwnerId() == null) {
            compound.setString("OwnerUUID", "");
        }
        else {
            compound.setString("OwnerUUID", this.getOwnerId().toString());
        }
        compound.setBoolean("Sitting", this.isSitting());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        String s;
        if (compound.hasKey("OwnerUUID", 8)) {
            s = compound.getString("OwnerUUID");
        }
        else {
            final String s2 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s2);
        }
        if (!s.isEmpty()) {
            try {
                this.setOwnerId(UUID.fromString(s));
                this.setTamed(true);
            }
            catch (Throwable var4) {
                this.setTamed(false);
            }
        }
        if (this.aiSit != null) {
            this.aiSit.setSitting(compound.getBoolean("Sitting"));
        }
        this.setSitting(compound.getBoolean("Sitting"));
    }
    
    @Override
    public boolean canBeLeashedTo(final EntityPlayer player) {
        return !this.getLeashed();
    }
    
    protected void playTameEffect(final boolean play) {
        EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;
        if (!play) {
            enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;
        }
        for (int i = 0; i < 7; ++i) {
            final double d0 = this.rand.nextGaussian() * 0.02;
            final double d2 = this.rand.nextGaussian() * 0.02;
            final double d3 = this.rand.nextGaussian() * 0.02;
            this.world.spawnParticle(enumparticletypes, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d0, d2, d3, new int[0]);
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 7) {
            this.playTameEffect(true);
        }
        else if (id == 6) {
            this.playTameEffect(false);
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    public boolean isTamed() {
        return (this.dataManager.get(EntityTameable.TAMED) & 0x4) != 0x0;
    }
    
    public void setTamed(final boolean tamed) {
        final byte b0 = this.dataManager.get(EntityTameable.TAMED);
        if (tamed) {
            this.dataManager.set(EntityTameable.TAMED, (byte)(b0 | 0x4));
        }
        else {
            this.dataManager.set(EntityTameable.TAMED, (byte)(b0 & 0xFFFFFFFB));
        }
        this.setupTamedAI();
    }
    
    protected void setupTamedAI() {
    }
    
    public boolean isSitting() {
        return (this.dataManager.get(EntityTameable.TAMED) & 0x1) != 0x0;
    }
    
    public void setSitting(final boolean sitting) {
        final byte b0 = this.dataManager.get(EntityTameable.TAMED);
        if (sitting) {
            this.dataManager.set(EntityTameable.TAMED, (byte)(b0 | 0x1));
        }
        else {
            this.dataManager.set(EntityTameable.TAMED, (byte)(b0 & 0xFFFFFFFE));
        }
    }
    
    @Nullable
    @Override
    public UUID getOwnerId() {
        return (UUID)this.dataManager.get(EntityTameable.OWNER_UNIQUE_ID).orNull();
    }
    
    public void setOwnerId(@Nullable final UUID p_184754_1_) {
        this.dataManager.set(EntityTameable.OWNER_UNIQUE_ID, (Optional<UUID>)Optional.fromNullable((Object)p_184754_1_));
    }
    
    public void setTamedBy(final EntityPlayer player) {
        this.setTamed(true);
        this.setOwnerId(player.getUniqueID());
        if (player instanceof EntityPlayerMP) {
            CriteriaTriggers.TAME_ANIMAL.trigger((EntityPlayerMP)player, this);
        }
    }
    
    @Nullable
    @Override
    public EntityLivingBase getOwner() {
        try {
            final UUID uuid = this.getOwnerId();
            return (uuid == null) ? null : this.world.getPlayerEntityByUUID(uuid);
        }
        catch (IllegalArgumentException var2) {
            return null;
        }
    }
    
    public boolean isOwner(final EntityLivingBase entityIn) {
        return entityIn == this.getOwner();
    }
    
    public EntityAISit getAISit() {
        return this.aiSit;
    }
    
    public boolean shouldAttackEntity(final EntityLivingBase target, final EntityLivingBase owner) {
        return true;
    }
    
    @Override
    public Team getTeam() {
        if (this.isTamed()) {
            final EntityLivingBase entitylivingbase = this.getOwner();
            if (entitylivingbase != null) {
                return entitylivingbase.getTeam();
            }
        }
        return super.getTeam();
    }
    
    @Override
    public boolean isOnSameTeam(final Entity entityIn) {
        if (this.isTamed()) {
            final EntityLivingBase entitylivingbase = this.getOwner();
            if (entityIn == entitylivingbase) {
                return true;
            }
            if (entitylivingbase != null) {
                return entitylivingbase.isOnSameTeam(entityIn);
            }
        }
        return super.isOnSameTeam(entityIn);
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        if (!this.world.isRemote && this.world.getGameRules().getBoolean("showDeathMessages") && this.getOwner() instanceof EntityPlayerMP) {
            this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage());
        }
        super.onDeath(cause);
    }
    
    static {
        TAMED = EntityDataManager.createKey(EntityTameable.class, DataSerializers.BYTE);
        OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityTameable.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    }
}
