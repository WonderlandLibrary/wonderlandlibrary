package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PotionEffect
{
  private static final Logger LOGGER = ;
  private int potionID;
  private int duration;
  private int amplifier;
  private boolean isSplashPotion;
  private boolean isAmbient;
  private boolean isPotionDurationMax;
  private boolean showParticles;
  private static final String __OBFID = "CL_00001529";
  
  public PotionEffect(int id, int effectDuration)
  {
    this(id, effectDuration, 0);
  }
  
  public PotionEffect(int id, int effectDuration, int effectAmplifier)
  {
    this(id, effectDuration, effectAmplifier, false, true);
  }
  
  public PotionEffect(int id, int effectDuration, int effectAmplifier, boolean ambient, boolean showParticles)
  {
    this.potionID = id;
    this.duration = effectDuration;
    this.amplifier = effectAmplifier;
    this.isAmbient = ambient;
    this.showParticles = showParticles;
  }
  
  public PotionEffect(PotionEffect other)
  {
    this.potionID = other.potionID;
    this.duration = other.duration;
    this.amplifier = other.amplifier;
    this.isAmbient = other.isAmbient;
    this.showParticles = other.showParticles;
  }
  
  public void combine(PotionEffect other)
  {
    if (this.potionID != other.potionID) {
      LOGGER.warn("This method should only be called for matching effects!");
    }
    if (other.amplifier > this.amplifier)
    {
      this.amplifier = other.amplifier;
      this.duration = other.duration;
    }
    else if ((other.amplifier == this.amplifier) && (this.duration < other.duration))
    {
      this.duration = other.duration;
    }
    else if ((!other.isAmbient) && (this.isAmbient))
    {
      this.isAmbient = other.isAmbient;
    }
    this.showParticles = other.showParticles;
  }
  
  public int getPotionID()
  {
    return this.potionID;
  }
  
  public int getDuration()
  {
    return this.duration;
  }
  
  public int getAmplifier()
  {
    return this.amplifier;
  }
  
  public void setSplashPotion(boolean splashPotion)
  {
    this.isSplashPotion = splashPotion;
  }
  
  public boolean getIsAmbient()
  {
    return this.isAmbient;
  }
  
  public boolean func_180154_f()
  {
    return this.showParticles;
  }
  
  public boolean onUpdate(EntityLivingBase entityIn)
  {
    if (this.duration > 0)
    {
      if (Potion.potionTypes[this.potionID].isReady(this.duration, this.amplifier)) {
        performEffect(entityIn);
      }
      deincrementDuration();
    }
    return this.duration > 0;
  }
  
  private int deincrementDuration()
  {
    return --this.duration;
  }
  
  public void performEffect(EntityLivingBase entityIn)
  {
    if (this.duration > 0) {
      Potion.potionTypes[this.potionID].performEffect(entityIn, this.amplifier);
    }
  }
  
  public String getEffectName()
  {
    return Potion.potionTypes[this.potionID].getName();
  }
  
  public int hashCode()
  {
    return this.potionID;
  }
  
  public String toString()
  {
    String var1 = "";
    if (getAmplifier() > 0) {
      var1 = getEffectName() + " x " + (getAmplifier() + 1) + ", Duration: " + getDuration();
    } else {
      var1 = getEffectName() + ", Duration: " + getDuration();
    }
    if (this.isSplashPotion) {
      var1 = var1 + ", Splash: true";
    }
    if (!this.showParticles) {
      var1 = var1 + ", Particles: false";
    }
    return Potion.potionTypes[this.potionID].isUsable() ? "(" + var1 + ")" : var1;
  }
  
  public boolean equals(Object p_equals_1_)
  {
    if (!(p_equals_1_ instanceof PotionEffect)) {
      return false;
    }
    PotionEffect var2 = (PotionEffect)p_equals_1_;
    return (this.potionID == var2.potionID) && (this.amplifier == var2.amplifier) && (this.duration == var2.duration) && (this.isSplashPotion == var2.isSplashPotion) && (this.isAmbient == var2.isAmbient);
  }
  
  public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nbt)
  {
    nbt.setByte("Id", (byte)getPotionID());
    nbt.setByte("Amplifier", (byte)getAmplifier());
    nbt.setInteger("Duration", getDuration());
    nbt.setBoolean("Ambient", getIsAmbient());
    nbt.setBoolean("ShowParticles", func_180154_f());
    return nbt;
  }
  
  public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound nbt)
  {
    byte var1 = nbt.getByte("Id");
    if ((var1 >= 0) && (var1 < Potion.potionTypes.length) && (Potion.potionTypes[var1] != null))
    {
      byte var2 = nbt.getByte("Amplifier");
      int var3 = nbt.getInteger("Duration");
      boolean var4 = nbt.getBoolean("Ambient");
      boolean var5 = true;
      if (nbt.hasKey("ShowParticles", 1)) {
        var5 = nbt.getBoolean("ShowParticles");
      }
      return new PotionEffect(var1, var3, var2, var4, var5);
    }
    return null;
  }
  
  public void setPotionDurationMax(boolean maxDuration)
  {
    this.isPotionDurationMax = maxDuration;
  }
  
  public boolean getIsPotionDurationMax()
  {
    return this.isPotionDurationMax;
  }
}
