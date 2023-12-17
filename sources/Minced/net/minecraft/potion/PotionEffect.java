// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import org.apache.logging.log4j.LogManager;
import com.google.common.collect.ComparisonChain;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityLivingBase;
import org.apache.logging.log4j.Logger;

public class PotionEffect implements Comparable<PotionEffect>
{
    private static final Logger LOGGER;
    private final Potion potion;
    private int duration;
    private int amplifier;
    private boolean isSplashPotion;
    private boolean isAmbient;
    private boolean isPotionDurationMax;
    private boolean showParticles;
    
    public PotionEffect(final Potion potionIn) {
        this(potionIn, 0, 0);
    }
    
    public PotionEffect(final Potion potionIn, final int durationIn) {
        this(potionIn, durationIn, 0);
    }
    
    public PotionEffect(final Potion potionIn, final int durationIn, final int amplifierIn) {
        this(potionIn, durationIn, amplifierIn, false, true);
    }
    
    public PotionEffect(final Potion potionIn, final int durationIn, final int amplifierIn, final boolean ambientIn, final boolean showParticlesIn) {
        this.potion = potionIn;
        this.duration = durationIn;
        this.amplifier = amplifierIn;
        this.isAmbient = ambientIn;
        this.showParticles = showParticlesIn;
    }
    
    public PotionEffect(final PotionEffect other) {
        this.potion = other.potion;
        this.duration = other.duration;
        this.amplifier = other.amplifier;
        this.isAmbient = other.isAmbient;
        this.showParticles = other.showParticles;
    }
    
    public void combine(final PotionEffect other) {
        if (this.potion != other.potion) {
            PotionEffect.LOGGER.warn("This method should only be called for matching effects!");
        }
        if (other.amplifier > this.amplifier) {
            this.amplifier = other.amplifier;
            this.duration = other.duration;
        }
        else if (other.amplifier == this.amplifier && this.duration < other.duration) {
            this.duration = other.duration;
        }
        else if (!other.isAmbient && this.isAmbient) {
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
    
    public boolean onUpdate(final EntityLivingBase entityIn) {
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
    
    public void performEffect(final EntityLivingBase entityIn) {
        if (this.duration > 0) {
            this.potion.performEffect(entityIn, this.amplifier);
        }
    }
    
    public String getEffectName() {
        return this.potion.getName();
    }
    
    @Override
    public String toString() {
        String s;
        if (this.amplifier > 0) {
            s = this.getEffectName() + " x " + (this.amplifier + 1) + ", Duration: " + this.duration;
        }
        else {
            s = this.getEffectName() + ", Duration: " + this.duration;
        }
        if (this.isSplashPotion) {
            s += ", Splash: true";
        }
        if (!this.showParticles) {
            s += ", Particles: false";
        }
        return s;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof PotionEffect)) {
            return false;
        }
        final PotionEffect potioneffect = (PotionEffect)p_equals_1_;
        return this.duration == potioneffect.duration && this.amplifier == potioneffect.amplifier && this.isSplashPotion == potioneffect.isSplashPotion && this.isAmbient == potioneffect.isAmbient && this.potion.equals(potioneffect.potion);
    }
    
    @Override
    public int hashCode() {
        int i = this.potion.hashCode();
        i = 31 * i + this.duration;
        i = 31 * i + this.amplifier;
        i = 31 * i + (this.isSplashPotion ? 1 : 0);
        i = 31 * i + (this.isAmbient ? 1 : 0);
        return i;
    }
    
    public NBTTagCompound writeCustomPotionEffectToNBT(final NBTTagCompound nbt) {
        nbt.setByte("Id", (byte)Potion.getIdFromPotion(this.getPotion()));
        nbt.setByte("Amplifier", (byte)this.getAmplifier());
        nbt.setInteger("Duration", this.getDuration());
        nbt.setBoolean("Ambient", this.getIsAmbient());
        nbt.setBoolean("ShowParticles", this.doesShowParticles());
        return nbt;
    }
    
    public static PotionEffect readCustomPotionEffectFromNBT(final NBTTagCompound nbt) {
        final int i = nbt.getByte("Id");
        final Potion potion = Potion.getPotionById(i);
        if (potion == null) {
            return null;
        }
        final int j = nbt.getByte("Amplifier");
        final int k = nbt.getInteger("Duration");
        final boolean flag = nbt.getBoolean("Ambient");
        boolean flag2 = true;
        if (nbt.hasKey("ShowParticles", 1)) {
            flag2 = nbt.getBoolean("ShowParticles");
        }
        return new PotionEffect(potion, k, (j < 0) ? 0 : j, flag, flag2);
    }
    
    public void setPotionDurationMax(final boolean maxDuration) {
        this.isPotionDurationMax = maxDuration;
    }
    
    public boolean getIsPotionDurationMax() {
        return this.isPotionDurationMax;
    }
    
    @Override
    public int compareTo(final PotionEffect p_compareTo_1_) {
        final int i = 32147;
        return ((this.getDuration() <= 32147 || p_compareTo_1_.getDuration() <= 32147) && (!this.getIsAmbient() || !p_compareTo_1_.getIsAmbient())) ? ComparisonChain.start().compare(Boolean.valueOf(this.getIsAmbient()), Boolean.valueOf(p_compareTo_1_.getIsAmbient())).compare(this.getDuration(), p_compareTo_1_.getDuration()).compare(this.getPotion().getLiquidColor(), p_compareTo_1_.getPotion().getLiquidColor()).result() : ComparisonChain.start().compare(Boolean.valueOf(this.getIsAmbient()), Boolean.valueOf(p_compareTo_1_.getIsAmbient())).compare(this.getPotion().getLiquidColor(), p_compareTo_1_.getPotion().getLiquidColor()).result();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
