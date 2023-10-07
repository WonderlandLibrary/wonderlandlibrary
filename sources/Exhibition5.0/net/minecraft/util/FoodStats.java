// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemFood;

public class FoodStats
{
    private int foodLevel;
    private float foodSaturationLevel;
    private float foodExhaustionLevel;
    private int foodTimer;
    private int prevFoodLevel;
    private static final String __OBFID = "CL_00001729";
    
    public FoodStats() {
        this.foodLevel = 20;
        this.foodSaturationLevel = 5.0f;
        this.prevFoodLevel = 20;
    }
    
    public void addStats(final int p_75122_1_, final float p_75122_2_) {
        this.foodLevel = Math.min(p_75122_1_ + this.foodLevel, 20);
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + p_75122_1_ * p_75122_2_ * 2.0f, this.foodLevel);
    }
    
    public void addStats(final ItemFood p_151686_1_, final ItemStack p_151686_2_) {
        this.addStats(p_151686_1_.getHealAmount(p_151686_2_), p_151686_1_.getSaturationModifier(p_151686_2_));
    }
    
    public void onUpdate(final EntityPlayer p_75118_1_) {
        final EnumDifficulty var2 = p_75118_1_.worldObj.getDifficulty();
        this.prevFoodLevel = this.foodLevel;
        if (this.foodExhaustionLevel > 4.0f) {
            this.foodExhaustionLevel -= 4.0f;
            if (this.foodSaturationLevel > 0.0f) {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0f, 0.0f);
            }
            else if (var2 != EnumDifficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }
        if (p_75118_1_.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && this.foodLevel >= 18 && p_75118_1_.shouldHeal()) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                p_75118_1_.heal(1.0f);
                this.addExhaustion(3.0f);
                this.foodTimer = 0;
            }
        }
        else if (this.foodLevel <= 0) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                if (p_75118_1_.getHealth() > 10.0f || var2 == EnumDifficulty.HARD || (p_75118_1_.getHealth() > 1.0f && var2 == EnumDifficulty.NORMAL)) {
                    p_75118_1_.attackEntityFrom(DamageSource.starve, 1.0f);
                }
                this.foodTimer = 0;
            }
        }
        else {
            this.foodTimer = 0;
        }
    }
    
    public void readNBT(final NBTTagCompound p_75112_1_) {
        if (p_75112_1_.hasKey("foodLevel", 99)) {
            this.foodLevel = p_75112_1_.getInteger("foodLevel");
            this.foodTimer = p_75112_1_.getInteger("foodTickTimer");
            this.foodSaturationLevel = p_75112_1_.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = p_75112_1_.getFloat("foodExhaustionLevel");
        }
    }
    
    public void writeNBT(final NBTTagCompound p_75117_1_) {
        p_75117_1_.setInteger("foodLevel", this.foodLevel);
        p_75117_1_.setInteger("foodTickTimer", this.foodTimer);
        p_75117_1_.setFloat("foodSaturationLevel", this.foodSaturationLevel);
        p_75117_1_.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
    }
    
    public int getFoodLevel() {
        return this.foodLevel;
    }
    
    public int getPrevFoodLevel() {
        return this.prevFoodLevel;
    }
    
    public boolean needFood() {
        return this.foodLevel < 20;
    }
    
    public void addExhaustion(final float p_75113_1_) {
        this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + p_75113_1_, 40.0f);
    }
    
    public float getSaturationLevel() {
        return this.foodSaturationLevel;
    }
    
    public void setFoodLevel(final int p_75114_1_) {
        this.foodLevel = p_75114_1_;
    }
    
    public void setFoodSaturationLevel(final float p_75119_1_) {
        this.foodSaturationLevel = p_75119_1_;
    }
}
