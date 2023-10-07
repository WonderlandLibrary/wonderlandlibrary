// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLiving;

public class EntityMoveHelper
{
    protected EntityLiving entity;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected double speed;
    protected boolean update;
    private static final String __OBFID = "CL_00001573";
    
    public EntityMoveHelper(final EntityLiving p_i1614_1_) {
        this.entity = p_i1614_1_;
        this.posX = p_i1614_1_.posX;
        this.posY = p_i1614_1_.posY;
        this.posZ = p_i1614_1_.posZ;
    }
    
    public boolean isUpdating() {
        return this.update;
    }
    
    public double getSpeed() {
        return this.speed;
    }
    
    public void setMoveTo(final double p_75642_1_, final double p_75642_3_, final double p_75642_5_, final double p_75642_7_) {
        this.posX = p_75642_1_;
        this.posY = p_75642_3_;
        this.posZ = p_75642_5_;
        this.speed = p_75642_7_;
        this.update = true;
    }
    
    public void onUpdateMoveHelper() {
        this.entity.setMoveForward(0.0f);
        if (this.update) {
            this.update = false;
            final int var1 = MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5);
            final double var2 = this.posX - this.entity.posX;
            final double var3 = this.posZ - this.entity.posZ;
            final double var4 = this.posY - var1;
            final double var5 = var2 * var2 + var4 * var4 + var3 * var3;
            if (var5 >= 2.500000277905201E-7) {
                final float var6 = (float)(Math.atan2(var3, var2) * 180.0 / 3.141592653589793) - 90.0f;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, var6, 30.0f);
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                if (var4 > 0.0 && var2 * var2 + var3 * var3 < 1.0) {
                    this.entity.getJumpHelper().setJumping();
                }
            }
        }
    }
    
    protected float limitAngle(final float p_75639_1_, final float p_75639_2_, final float p_75639_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);
        if (var4 > p_75639_3_) {
            var4 = p_75639_3_;
        }
        if (var4 < -p_75639_3_) {
            var4 = -p_75639_3_;
        }
        float var5 = p_75639_1_ + var4;
        if (var5 < 0.0f) {
            var5 += 360.0f;
        }
        else if (var5 > 360.0f) {
            var5 -= 360.0f;
        }
        return var5;
    }
    
    public double func_179917_d() {
        return this.posX;
    }
    
    public double func_179919_e() {
        return this.posY;
    }
    
    public double func_179918_f() {
        return this.posZ;
    }
}
