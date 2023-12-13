// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.projectile;

import net.minecraft.util.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntitySmallFireball extends EntityFireball
{
    private static final String __OBFID = "CL_00001721";
    
    public EntitySmallFireball(final World worldIn) {
        super(worldIn);
        this.setSize(0.3125f, 0.3125f);
    }
    
    public EntitySmallFireball(final World worldIn, final EntityLivingBase p_i1771_2_, final double p_i1771_3_, final double p_i1771_5_, final double p_i1771_7_) {
        super(worldIn, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
        this.setSize(0.3125f, 0.3125f);
    }
    
    public EntitySmallFireball(final World worldIn, final double p_i1772_2_, final double p_i1772_4_, final double p_i1772_6_, final double p_i1772_8_, final double p_i1772_10_, final double p_i1772_12_) {
        super(worldIn, p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_);
        this.setSize(0.3125f, 0.3125f);
    }
    
    @Override
    protected void onImpact(final MovingObjectPosition p_70227_1_) {
        if (!this.worldObj.isRemote) {
            if (p_70227_1_.entityHit != null) {
                final boolean var2 = p_70227_1_.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0f);
                if (var2) {
                    this.func_174815_a(this.shootingEntity, p_70227_1_.entityHit);
                    if (!p_70227_1_.entityHit.isImmuneToFire()) {
                        p_70227_1_.entityHit.setFire(5);
                    }
                }
            }
            else {
                boolean var2 = true;
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving) {
                    var2 = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
                }
                if (var2) {
                    final BlockPos var3 = p_70227_1_.getBlockPos().offset(p_70227_1_.facing);
                    if (this.worldObj.isAirBlock(var3)) {
                        this.worldObj.setBlockState(var3, Blocks.fire.getDefaultState());
                    }
                }
            }
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
}
