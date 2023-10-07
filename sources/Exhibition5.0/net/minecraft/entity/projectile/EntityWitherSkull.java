// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityWitherSkull extends EntityFireball
{
    private static final String __OBFID = "CL_00001728";
    
    public EntityWitherSkull(final World worldIn) {
        super(worldIn);
        this.setSize(0.3125f, 0.3125f);
    }
    
    public EntityWitherSkull(final World worldIn, final EntityLivingBase p_i1794_2_, final double p_i1794_3_, final double p_i1794_5_, final double p_i1794_7_) {
        super(worldIn, p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_);
        this.setSize(0.3125f, 0.3125f);
    }
    
    @Override
    protected float getMotionFactor() {
        return this.isInvulnerable() ? 0.73f : super.getMotionFactor();
    }
    
    public EntityWitherSkull(final World worldIn, final double p_i1795_2_, final double p_i1795_4_, final double p_i1795_6_, final double p_i1795_8_, final double p_i1795_10_, final double p_i1795_12_) {
        super(worldIn, p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_);
        this.setSize(0.3125f, 0.3125f);
    }
    
    @Override
    public boolean isBurning() {
        return false;
    }
    
    @Override
    public float getExplosionResistance(final Explosion p_180428_1_, final World worldIn, final BlockPos p_180428_3_, final IBlockState p_180428_4_) {
        float var5 = super.getExplosionResistance(p_180428_1_, worldIn, p_180428_3_, p_180428_4_);
        if (this.isInvulnerable() && p_180428_4_.getBlock() != Blocks.bedrock && p_180428_4_.getBlock() != Blocks.end_portal && p_180428_4_.getBlock() != Blocks.end_portal_frame && p_180428_4_.getBlock() != Blocks.command_block) {
            var5 = Math.min(0.8f, var5);
        }
        return var5;
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition p_70227_1_) {
        if (!this.worldObj.isRemote) {
            if (p_70227_1_.entityHit != null) {
                if (this.shootingEntity != null) {
                    if (p_70227_1_.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0f)) {
                        if (!p_70227_1_.entityHit.isEntityAlive()) {
                            this.shootingEntity.heal(5.0f);
                        }
                        else {
                            this.func_174815_a(this.shootingEntity, p_70227_1_.entityHit);
                        }
                    }
                }
                else {
                    p_70227_1_.entityHit.attackEntityFrom(DamageSource.magic, 5.0f);
                }
                if (p_70227_1_.entityHit instanceof EntityLivingBase) {
                    byte var2 = 0;
                    if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
                        var2 = 10;
                    }
                    else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                        var2 = 40;
                    }
                    if (var2 > 0) {
                        ((EntityLivingBase)p_70227_1_.entityHit).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * var2, 1));
                    }
                }
            }
            this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0f, false, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
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
    protected void entityInit() {
        this.dataWatcher.addObject(10, (byte)0);
    }
    
    public boolean isInvulnerable() {
        return this.dataWatcher.getWatchableObjectByte(10) == 1;
    }
    
    public void setInvulnerable(final boolean p_82343_1_) {
        this.dataWatcher.updateObject(10, (byte)(byte)(p_82343_1_ ? 1 : 0));
    }
}
