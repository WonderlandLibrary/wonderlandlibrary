/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;

public class FoodStats {
    private int foodLevel = 20;
    private int foodTimer;
    private float foodExhaustionLevel;
    private float foodSaturationLevel = 5.0f;
    private int prevFoodLevel = 20;

    public void addExhaustion(float f) {
        this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + f, 40.0f);
    }

    public boolean needFood() {
        return this.foodLevel < 20;
    }

    public void readNBT(NBTTagCompound nBTTagCompound) {
        if (nBTTagCompound.hasKey("foodLevel", 99)) {
            this.foodLevel = nBTTagCompound.getInteger("foodLevel");
            this.foodTimer = nBTTagCompound.getInteger("foodTickTimer");
            this.foodSaturationLevel = nBTTagCompound.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = nBTTagCompound.getFloat("foodExhaustionLevel");
        }
    }

    public void setFoodLevel(int n) {
        this.foodLevel = n;
    }

    public void addStats(int n, float f) {
        this.foodLevel = Math.min(n + this.foodLevel, 20);
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float)n * f * 2.0f, (float)this.foodLevel);
    }

    public void setFoodSaturationLevel(float f) {
        this.foodSaturationLevel = f;
    }

    public void writeNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setInteger("foodLevel", this.foodLevel);
        nBTTagCompound.setInteger("foodTickTimer", this.foodTimer);
        nBTTagCompound.setFloat("foodSaturationLevel", this.foodSaturationLevel);
        nBTTagCompound.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
    }

    public void addStats(ItemFood itemFood, ItemStack itemStack) {
        this.addStats(itemFood.getHealAmount(itemStack), itemFood.getSaturationModifier(itemStack));
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    public float getSaturationLevel() {
        return this.foodSaturationLevel;
    }

    public void onUpdate(EntityPlayer entityPlayer) {
        EnumDifficulty enumDifficulty = entityPlayer.worldObj.getDifficulty();
        this.prevFoodLevel = this.foodLevel;
        if (this.foodExhaustionLevel > 4.0f) {
            this.foodExhaustionLevel -= 4.0f;
            if (this.foodSaturationLevel > 0.0f) {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0f, 0.0f);
            } else if (enumDifficulty != EnumDifficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }
        if (entityPlayer.worldObj.getGameRules().getBoolean("naturalRegeneration") && this.foodLevel >= 18 && entityPlayer.shouldHeal()) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                entityPlayer.heal(1.0f);
                this.addExhaustion(3.0f);
                this.foodTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                if (entityPlayer.getHealth() > 10.0f || enumDifficulty == EnumDifficulty.HARD || entityPlayer.getHealth() > 1.0f && enumDifficulty == EnumDifficulty.NORMAL) {
                    entityPlayer.attackEntityFrom(DamageSource.starve, 1.0f);
                }
                this.foodTimer = 0;
            }
        } else {
            this.foodTimer = 0;
        }
    }

    public int getPrevFoodLevel() {
        return this.prevFoodLevel;
    }
}

