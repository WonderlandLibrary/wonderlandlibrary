// 
// Decompiled by Procyon v0.5.36
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
    
    public FoodStats() {
        this.foodLevel = 20;
        this.foodSaturationLevel = 5.0f;
        this.prevFoodLevel = 20;
    }
    
    public void addStats(final int foodLevelIn, final float foodSaturationModifier) {
        this.foodLevel = Math.min(foodLevelIn + this.foodLevel, 20);
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + foodLevelIn * foodSaturationModifier * 2.0f, (float)this.foodLevel);
    }
    
    public void addStats(final ItemFood foodItem, final ItemStack stack) {
        this.addStats(foodItem.getHealAmount(stack), foodItem.getSaturationModifier(stack));
    }
    
    public void onUpdate(final EntityPlayer player) {
        final EnumDifficulty enumdifficulty = player.world.getDifficulty();
        this.prevFoodLevel = this.foodLevel;
        if (this.foodExhaustionLevel > 4.0f) {
            this.foodExhaustionLevel -= 4.0f;
            if (this.foodSaturationLevel > 0.0f) {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0f, 0.0f);
            }
            else if (enumdifficulty != EnumDifficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }
        final boolean flag = player.world.getGameRules().getBoolean("naturalRegeneration");
        if (flag && this.foodSaturationLevel > 0.0f && player.shouldHeal() && this.foodLevel >= 20) {
            ++this.foodTimer;
            if (this.foodTimer >= 10) {
                final float f = Math.min(this.foodSaturationLevel, 6.0f);
                player.heal(f / 6.0f);
                this.addExhaustion(f);
                this.foodTimer = 0;
            }
        }
        else if (flag && this.foodLevel >= 18 && player.shouldHeal()) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                player.heal(1.0f);
                this.addExhaustion(6.0f);
                this.foodTimer = 0;
            }
        }
        else if (this.foodLevel <= 0) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                if (player.getHealth() > 10.0f || enumdifficulty == EnumDifficulty.HARD || (player.getHealth() > 1.0f && enumdifficulty == EnumDifficulty.NORMAL)) {
                    player.attackEntityFrom(DamageSource.STARVE, 1.0f);
                }
                this.foodTimer = 0;
            }
        }
        else {
            this.foodTimer = 0;
        }
    }
    
    public void readNBT(final NBTTagCompound compound) {
        if (compound.hasKey("foodLevel", 99)) {
            this.foodLevel = compound.getInteger("foodLevel");
            this.foodTimer = compound.getInteger("foodTickTimer");
            this.foodSaturationLevel = compound.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = compound.getFloat("foodExhaustionLevel");
        }
    }
    
    public void writeNBT(final NBTTagCompound compound) {
        compound.setInteger("foodLevel", this.foodLevel);
        compound.setInteger("foodTickTimer", this.foodTimer);
        compound.setFloat("foodSaturationLevel", this.foodSaturationLevel);
        compound.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
    }
    
    public int getFoodLevel() {
        return this.foodLevel;
    }
    
    public boolean needFood() {
        return this.foodLevel < 20;
    }
    
    public void addExhaustion(final float exhaustion) {
        this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + exhaustion, 40.0f);
    }
    
    public float getSaturationLevel() {
        return this.foodSaturationLevel;
    }
    
    public void setFoodLevel(final int foodLevelIn) {
        this.foodLevel = foodLevelIn;
    }
    
    public void setFoodSaturationLevel(final float foodSaturationLevelIn) {
        this.foodSaturationLevel = foodSaturationLevelIn;
    }
}
