/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.potion;

import com.google.common.collect.ComparisonChain;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PotionEffect
implements Comparable<PotionEffect> {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Potion potion;
    private int duration;
    private int amplifier;
    private boolean isSplashPotion;
    private boolean isAmbient;
    private boolean isPotionDurationMax;
    private boolean showParticles;

    public PotionEffect(Potion potionIn) {
        this(potionIn, 0, 0);
    }

    public PotionEffect(Potion potionIn, int durationIn) {
        this(potionIn, durationIn, 0);
    }

    public PotionEffect(Potion potionIn, int durationIn, int amplifierIn) {
        this(potionIn, durationIn, amplifierIn, false, true);
    }

    public PotionEffect(Potion potionIn, int durationIn, int amplifierIn, boolean ambientIn, boolean showParticlesIn) {
        this.potion = potionIn;
        this.duration = durationIn;
        this.amplifier = amplifierIn;
        this.isAmbient = ambientIn;
        this.showParticles = showParticlesIn;
    }

    public PotionEffect(PotionEffect other) {
        this.potion = other.potion;
        this.duration = other.duration;
        this.amplifier = other.amplifier;
        this.isAmbient = other.isAmbient;
        this.showParticles = other.showParticles;
    }

    public void combine(PotionEffect other) {
        if (this.potion != other.potion) {
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

    public Potion getPotion() {
        return this.potion;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public boolean getIsAmbient() {
        return this.isAmbient;
    }

    public boolean doesShowParticles() {
        return this.showParticles;
    }

    public boolean onUpdate(EntityLivingBase entityIn) {
        if (this.duration > 0) {
            if (this.potion.isReady(this.duration, this.amplifier)) {
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
            this.potion.performEffect(entityIn, this.amplifier);
        }
    }

    public String getEffectName() {
        return this.potion.getName();
    }

    public String toString() {
        String s = this.amplifier > 0 ? this.getEffectName() + " x " + (this.amplifier + 1) + ", Duration: " + this.duration : this.getEffectName() + ", Duration: " + this.duration;
        if (this.isSplashPotion) {
            s = s + ", Splash: true";
        }
        if (!this.showParticles) {
            s = s + ", Particles: false";
        }
        return s;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof PotionEffect)) {
            return false;
        }
        PotionEffect potioneffect = (PotionEffect)p_equals_1_;
        return this.duration == potioneffect.duration && this.amplifier == potioneffect.amplifier && this.isSplashPotion == potioneffect.isSplashPotion && this.isAmbient == potioneffect.isAmbient && this.potion.equals(potioneffect.potion);
    }

    public int hashCode() {
        int i = this.potion.hashCode();
        i = 31 * i + this.duration;
        i = 31 * i + this.amplifier;
        i = 31 * i + (this.isSplashPotion ? 1 : 0);
        i = 31 * i + (this.isAmbient ? 1 : 0);
        return i;
    }

    public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nbt) {
        nbt.setByte("Id", (byte)Potion.getIdFromPotion(this.getPotion()));
        nbt.setByte("Amplifier", (byte)this.getAmplifier());
        nbt.setInteger("Duration", this.getDuration());
        nbt.setBoolean("Ambient", this.getIsAmbient());
        nbt.setBoolean("ShowParticles", this.doesShowParticles());
        return nbt;
    }

    public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound nbt) {
        byte i = nbt.getByte("Id");
        Potion potion = Potion.getPotionById(i);
        if (potion == null) {
            return null;
        }
        byte j = nbt.getByte("Amplifier");
        int k = nbt.getInteger("Duration");
        boolean flag = nbt.getBoolean("Ambient");
        boolean flag1 = true;
        if (nbt.hasKey("ShowParticles", 1)) {
            flag1 = nbt.getBoolean("ShowParticles");
        }
        return new PotionEffect(potion, k, j < 0 ? (byte)0 : j, flag, flag1);
    }

    public void setPotionDurationMax(boolean maxDuration) {
        this.isPotionDurationMax = maxDuration;
    }

    public boolean getIsPotionDurationMax() {
        return this.isPotionDurationMax;
    }

    @Override
    public int compareTo(PotionEffect p_compareTo_1_) {
        int i = 32147;
        return !(this.getDuration() > 32147 && p_compareTo_1_.getDuration() > 32147 || this.getIsAmbient() && p_compareTo_1_.getIsAmbient()) ? ComparisonChain.start().compare(this.getIsAmbient(), p_compareTo_1_.getIsAmbient()).compare(this.getDuration(), p_compareTo_1_.getDuration()).compare(this.getPotion().getLiquidColor(), p_compareTo_1_.getPotion().getLiquidColor()).result() : ComparisonChain.start().compare(this.getIsAmbient(), p_compareTo_1_.getIsAmbient()).compare(this.getPotion().getLiquidColor(), p_compareTo_1_.getPotion().getLiquidColor()).result();
    }
}

