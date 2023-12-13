// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntityThrowable;

public class EntityEnderPearl extends EntityThrowable
{
    private static final String __OBFID = "CL_00001725";
    
    public EntityEnderPearl(final World worldIn, final EntityLivingBase p_i1783_2_) {
        super(worldIn, p_i1783_2_);
    }
    
    public EntityEnderPearl(final World worldIn, final double p_i1784_2_, final double p_i1784_4_, final double p_i1784_6_) {
        super(worldIn, p_i1784_2_, p_i1784_4_, p_i1784_6_);
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition p_70184_1_) {
        final EntityLivingBase var2 = this.getThrower();
        if (p_70184_1_.entityHit != null) {
            p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, var2), 0.0f);
        }
        for (int var3 = 0; var3 < 32; ++var3) {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0, this.posZ, this.rand.nextGaussian(), 0.0, this.rand.nextGaussian(), new int[0]);
        }
        if (!this.worldObj.isRemote) {
            if (var2 instanceof EntityPlayerMP) {
                final EntityPlayerMP var4 = (EntityPlayerMP)var2;
                if (var4.playerNetServerHandler.getNetworkManager().isChannelOpen() && var4.worldObj == this.worldObj && !var4.isPlayerSleeping()) {
                    if (this.rand.nextFloat() < 0.05f && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobSpawning")) {
                        final EntityEndermite var5 = new EntityEndermite(this.worldObj);
                        var5.setSpawnedByPlayer(true);
                        var5.setLocationAndAngles(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
                        this.worldObj.spawnEntityInWorld(var5);
                    }
                    if (var2.isRiding()) {
                        var2.mountEntity(null);
                    }
                    var2.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    var2.fallDistance = 0.0f;
                    var2.attackEntityFrom(DamageSource.fall, 5.0f);
                }
            }
            this.setDead();
        }
    }
    
    @Override
    public void onUpdate() {
        final EntityLivingBase var1 = this.getThrower();
        if (var1 != null && var1 instanceof EntityPlayer && !var1.isEntityAlive()) {
            this.setDead();
        }
        else {
            super.onUpdate();
        }
    }
}
