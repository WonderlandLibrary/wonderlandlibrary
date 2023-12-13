/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PotionEffect {
    private static final Logger LOGGER = LogManager.getLogger();
    private int potionID;
    private int duration;
    private int amplifier;
    private boolean isSplashPotion;
    private boolean isAmbient;
    private boolean isPotionDurationMax;
    private boolean showParticles;

    public PotionEffect(int id, int effectDuration) {
        this(id, effectDuration, 0);
    }

    public PotionEffect(int id, int effectDuration, int effectAmplifier) {
        this(id, effectDuration, effectAmplifier, false, true);
    }

    public PotionEffect(int id, int effectDuration, int effectAmplifier, boolean ambient, boolean showParticles) {
        this.potionID = id;
        this.duration = effectDuration;
        this.amplifier = effectAmplifier;
        this.isAmbient = ambient;
        this.showParticles = showParticles;
    }

    public PotionEffect(PotionEffect other) {
        this.potionID = other.potionID;
        this.duration = other.duration;
        this.amplifier = other.amplifier;
        this.isAmbient = other.isAmbient;
        this.showParticles = other.showParticles;
    }

    public void combine(PotionEffect other) {
        if (this.potionID != other.potionID) {
            LOGGER.warn("This method should only be called for matching effects!");
        }
        if (other.amplifier > this.amplifier) {
            this.amplifier = other.amplifier;
            this.duration = other.duration;
        } else if (other.amplifier == this.amplifier && this.duration < other.duration) {
            this.duration = other.duration;
        } else if (!other.isAmbient && this.isAmbient) {
            this.isAmbient = other.isAmbient;
        }
        this.showParticles = other.showParticles;
    }

    public int getPotionID() {
        return this.potionID;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public void setSplashPotion(boolean splashPotion) {
        this.isSplashPotion = splashPotion;
    }

    public boolean getIsAmbient() {
        return this.isAmbient;
    }

    public boolean getIsShowParticles() {
        return this.showParticles;
    }

    public boolean onUpdate(EntityLivingBase entityIn) {
        if (this.duration > 0) {
            if (Potion.potionTypes[this.potionID].isReady(this.duration, this.amplifier)) {
                this.performEffect(entityIn);
            }
            this.deincrementDuration();
        }
        return this.duration > 0;
    }

    private int deincrementDuration() {
        return --this.duration;
    }

    public void performEffect(EntityLivingBase entityIn) {
        if (this.duration > 0) {
            Potion.potionTypes[this.potionID].performEffect(entityIn, this.amplifier);
        }
    }

    public String getEffectName() {
        return Potion.potionTypes[this.potionID].getName();
    }

    public int hashCode() {
        return this.potionID;
    }

    public String toString() {
        String s2 = "";
        s2 = this.getAmplifier() > 0 ? this.getEffectName() + " x " + (this.getAmplifier() + 1) + ", Duration: " + this.getDuration() : this.getEffectName() + ", Duration: " + this.getDuration();
        if (this.isSplashPotion) {
            s2 = s2 + ", Splash: true";
        }
        if (!this.showParticles) {
            s2 = s2 + ", Particles: false";
        }
        return Potion.potionTypes[this.potionID].isUsable() ? "(" + s2 + ")" : s2;
    }

    public boolean equals(Object p_equals_1_) {
        if (!(p_equals_1_ instanceof PotionEffect)) {
            return false;
        }
        PotionEffect potioneffect = (PotionEffect)p_equals_1_;
        return this.potionID == potioneffect.potionID && this.amplifier == potioneffect.amplifier && this.duration == potioneffect.duration && this.isSplashPotion == potioneffect.isSplashPotion && this.isAmbient == potioneffect.isAmbient;
    }

    public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nbt) {
        nbt.setByte("Id", (byte)this.getPotionID());
        nbt.setByte("Amplifier", (byte)this.getAmplifier());
        nbt.setInteger("Duration", this.getDuration());
        nbt.setBoolean("Ambient", this.getIsAmbient());
        nbt.setBoolean("ShowParticles", this.getIsShowParticles());
        return nbt;
    }

    public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound nbt) {
        byte i2 = nbt.getByte("Id");
        if (i2 >= 0 && i2 < Potion.potionTypes.length && Potion.potionTypes[i2] != null) {
            byte j2 = nbt.getByte("Amplifier");
            int k2 = nbt.getInteger("Duration");
            boolean flag = nbt.getBoolean("Ambient");
            boolean flag1 = true;
            if (nbt.hasKey("ShowParticles", 1)) {
                flag1 = nbt.getBoolean("ShowParticles");
            }
            return new PotionEffect(i2, k2, j2, flag, flag1);
        }
        return null;
    }

    public void setPotionDurationMax(boolean maxDuration) {
        this.isPotionDurationMax = maxDuration;
    }

    public boolean getIsPotionDurationMax() {
        return this.isPotionDurationMax;
    }
}

