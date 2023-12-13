package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public abstract class PositionedSound implements ISound {
   protected ISound.AttenuationType attenuationType;
   protected int repeatDelay = 0;
   protected float zPosF;
   protected float pitch = 1.0F;
   protected boolean repeat = false;
   protected float xPosF;
   protected float volume = 1.0F;
   protected float yPosF;
   protected final ResourceLocation positionedSoundLocation;

   public float getYPosF() {
      return this.yPosF;
   }

   public ResourceLocation getSoundLocation() {
      return this.positionedSoundLocation;
   }

   public float getZPosF() {
      return this.zPosF;
   }

   public float getXPosF() {
      return this.xPosF;
   }

   public ISound.AttenuationType getAttenuationType() {
      return this.attenuationType;
   }

   public boolean canRepeat() {
      return this.repeat;
   }

   public float getPitch() {
      return this.pitch;
   }

   public float getVolume() {
      return this.volume;
   }

   public int getRepeatDelay() {
      return this.repeatDelay;
   }

   protected PositionedSound(ResourceLocation var1) {
      this.attenuationType = ISound.AttenuationType.LINEAR;
      this.positionedSoundLocation = var1;
   }
}
