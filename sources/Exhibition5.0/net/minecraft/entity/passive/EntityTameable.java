// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.scoreboard.Team;
import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.IEntityOwnable;

public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable
{
    protected EntityAISit aiSit;
    private static final String __OBFID = "CL_00001561";
    
    public EntityTameable(final World worldIn) {
        super(worldIn);
        this.aiSit = new EntityAISit(this);
        this.func_175544_ck();
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)0);
        this.dataWatcher.addObject(17, "");
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        if (this.func_152113_b() == null) {
            tagCompound.setString("OwnerUUID", "");
        }
        else {
            tagCompound.setString("OwnerUUID", this.func_152113_b());
        }
        tagCompound.setBoolean("Sitting", this.isSitting());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        String var2 = "";
        if (tagCompund.hasKey("OwnerUUID", 8)) {
            var2 = tagCompund.getString("OwnerUUID");
        }
        else {
            final String var3 = tagCompund.getString("Owner");
            var2 = PreYggdrasilConverter.func_152719_a(var3);
        }
        if (var2.length() > 0) {
            this.func_152115_b(var2);
            this.setTamed(true);
        }
        this.aiSit.setSitting(tagCompund.getBoolean("Sitting"));
        this.setSitting(tagCompund.getBoolean("Sitting"));
    }
    
    protected void playTameEffect(final boolean p_70908_1_) {
        EnumParticleTypes var2 = EnumParticleTypes.HEART;
        if (!p_70908_1_) {
            var2 = EnumParticleTypes.SMOKE_NORMAL;
        }
        for (int var3 = 0; var3 < 7; ++var3) {
            final double var4 = this.rand.nextGaussian() * 0.02;
            final double var5 = this.rand.nextGaussian() * 0.02;
            final double var6 = this.rand.nextGaussian() * 0.02;
            this.worldObj.spawnParticle(var2, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, var4, var5, var6, new int[0]);
        }
    }
    
    @Override
    public void handleHealthUpdate(final byte p_70103_1_) {
        if (p_70103_1_ == 7) {
            this.playTameEffect(true);
        }
        else if (p_70103_1_ == 6) {
            this.playTameEffect(false);
        }
        else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }
    
    public boolean isTamed() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x4) != 0x0;
    }
    
    public void setTamed(final boolean p_70903_1_) {
        final byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (p_70903_1_) {
            this.dataWatcher.updateObject(16, (byte)(var2 | 0x4));
        }
        else {
            this.dataWatcher.updateObject(16, (byte)(var2 & 0xFFFFFFFB));
        }
        this.func_175544_ck();
    }
    
    protected void func_175544_ck() {
    }
    
    public boolean isSitting() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0x0;
    }
    
    public void setSitting(final boolean p_70904_1_) {
        final byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (p_70904_1_) {
            this.dataWatcher.updateObject(16, (byte)(var2 | 0x1));
        }
        else {
            this.dataWatcher.updateObject(16, (byte)(var2 & 0xFFFFFFFE));
        }
    }
    
    @Override
    public String func_152113_b() {
        return this.dataWatcher.getWatchableObjectString(17);
    }
    
    public void func_152115_b(final String p_152115_1_) {
        this.dataWatcher.updateObject(17, p_152115_1_);
    }
    
    public EntityLivingBase func_180492_cm() {
        try {
            final UUID var1 = UUID.fromString(this.func_152113_b());
            return (var1 == null) ? null : this.worldObj.getPlayerEntityByUUID(var1);
        }
        catch (IllegalArgumentException var2) {
            return null;
        }
    }
    
    public boolean func_152114_e(final EntityLivingBase p_152114_1_) {
        return p_152114_1_ == this.func_180492_cm();
    }
    
    public EntityAISit getAISit() {
        return this.aiSit;
    }
    
    public boolean func_142018_a(final EntityLivingBase p_142018_1_, final EntityLivingBase p_142018_2_) {
        return true;
    }
    
    @Override
    public Team getTeam() {
        if (this.isTamed()) {
            final EntityLivingBase var1 = this.func_180492_cm();
            if (var1 != null) {
                return var1.getTeam();
            }
        }
        return super.getTeam();
    }
    
    @Override
    public boolean isOnSameTeam(final EntityLivingBase p_142014_1_) {
        if (this.isTamed()) {
            final EntityLivingBase var2 = this.func_180492_cm();
            if (p_142014_1_ == var2) {
                return true;
            }
            if (var2 != null) {
                return var2.isOnSameTeam(p_142014_1_);
            }
        }
        return super.isOnSameTeam(p_142014_1_);
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        if (!this.worldObj.isRemote && this.worldObj.getGameRules().getGameRuleBooleanValue("showDeathMessages") && this.hasCustomName() && this.func_180492_cm() instanceof EntityPlayerMP) {
            ((EntityPlayerMP)this.func_180492_cm()).addChatMessage(this.getCombatTracker().func_151521_b());
        }
        super.onDeath(cause);
    }
    
    @Override
    public Entity getOwner() {
        return this.func_180492_cm();
    }
}
