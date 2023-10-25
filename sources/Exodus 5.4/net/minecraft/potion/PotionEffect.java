/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PotionEffect {
    private boolean isAmbient;
    private int duration;
    private boolean showParticles;
    private boolean isPotionDurationMax;
    private int amplifier;
    private boolean isSplashPotion;
    private static final Logger LOGGER = LogManager.getLogger();
    private int potionID;

    private int deincrementDuration() {
        return --this.duration;
    }

    public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound nBTTagCompound) {
        byte by = nBTTagCompound.getByte("Id");
        if (by >= 0 && by < Potion.potionTypes.length && Potion.potionTypes[by] != null) {
            byte by2 = nBTTagCompound.getByte("Amplifier");
            int n = nBTTagCompound.getInteger("Duration");
            boolean bl = nBTTagCompound.getBoolean("Ambient");
            boolean bl2 = true;
            if (nBTTagCompound.hasKey("ShowParticles", 1)) {
                bl2 = nBTTagCompound.getBoolean("ShowParticles");
            }
            return new PotionEffect(by, n, by2, bl, bl2);
        }
        return null;
    }

    public void setSplashPotion(boolean bl) {
        this.isSplashPotion = bl;
    }

    public void combine(PotionEffect potionEffect) {
        if (this.potionID != potionEffect.potionID) {
            LOGGER.warn("This method should only be called for matching effects!");
        }
        if (potionEffect.amplifier > this.amplifier) {
            this.amplifier = potionEffect.amplifier;
            this.duration = potionEffect.duration;
        } else if (potionEffect.amplifier == this.amplifier && this.duration < potionEffect.duration) {
            this.duration = potionEffect.duration;
        } else if (!potionEffect.isAmbient && this.isAmbient) {
            this.isAmbient = potionEffect.isAmbient;
        }
        this.showParticles = potionEffect.showParticles;
    }

    public void performEffect(EntityLivingBase entityLivingBase) {
        if (this.duration > 0) {
            Potion.potionTypes[this.potionID].performEffect(entityLivingBase, this.amplifier);
        }
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean onUpdate(EntityLivingBase entityLivingBase) {
        if (this.duration > 0) {
            if (Potion.potionTypes[this.potionID].isReady(this.duration, this.amplifier)) {
                this.performEffect(entityLivingBase);
            }
            this.deincrementDuration();
        }
        return this.duration > 0;
    }

    public PotionEffect(int n, int n2, int n3, boolean bl, boolean bl2) {
        this.potionID = n;
        this.duration = n2;
        this.amplifier = n3;
        this.isAmbient = bl;
        this.showParticles = bl2;
    }

    public PotionEffect(PotionEffect potionEffect) {
        this.potionID = potionEffect.potionID;
        this.duration = potionEffect.duration;
        this.amplifier = potionEffect.amplifier;
        this.isAmbient = potionEffect.isAmbient;
        this.showParticles = potionEffect.showParticles;
    }

    public String toString() {
        String string = "";
        string = this.getAmplifier() > 0 ? String.valueOf(this.getEffectName()) + " x " + (this.getAmplifier() + 1) + ", Duration: " + this.getDuration() : String.valueOf(this.getEffectName()) + ", Duration: " + this.getDuration();
        if (this.isSplashPotion) {
            string = String.valueOf(string) + ", Splash: true";
        }
        if (!this.showParticles) {
            string = String.valueOf(string) + ", Particles: false";
        }
        return Potion.potionTypes[this.potionID].isUsable() ? "(" + string + ")" : string;
    }

    public boolean getIsShowParticles() {
        return this.showParticles;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setByte("Id", (byte)this.getPotionID());
        nBTTagCompound.setByte("Amplifier", (byte)this.getAmplifier());
        nBTTagCompound.setInteger("Duration", this.getDuration());
        nBTTagCompound.setBoolean("Ambient", this.getIsAmbient());
        nBTTagCompound.setBoolean("ShowParticles", this.getIsShowParticles());
        return nBTTagCompound;
    }

    public boolean getIsPotionDurationMax() {
        return this.isPotionDurationMax;
    }

    public boolean getIsAmbient() {
        return this.isAmbient;
    }

    public boolean equals(Object object) {
        if (!(object instanceof PotionEffect)) {
            return false;
        }
        PotionEffect potionEffect = (PotionEffect)object;
        return this.potionID == potionEffect.potionID && this.amplifier == potionEffect.amplifier && this.duration == potionEffect.duration && this.isSplashPotion == potionEffect.isSplashPotion && this.isAmbient == potionEffect.isAmbient;
    }

    public void setPotionDurationMax(boolean bl) {
        this.isPotionDurationMax = bl;
    }

    public int hashCode() {
        return this.potionID;
    }

    public PotionEffect(int n, int n2, int n3) {
        this(n, n2, n3, false, true);
    }

    public PotionEffect(int n, int n2) {
        this(n, n2, 0);
    }

    public int getPotionID() {
        return this.potionID;
    }

    public String getEffectName() {
        return Potion.potionTypes[this.potionID].getName();
    }
}

