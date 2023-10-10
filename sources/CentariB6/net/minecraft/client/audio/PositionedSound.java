package net.minecraft.client.audio;

import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;

public abstract class PositionedSound implements ISound {
   protected final ResourceLocation positionedSoundLocation;
   protected float volume = 1.0F;
   protected float pitch = 1.0F;
   protected float xPosF;
   protected float yPosF;
   protected float zPosF;
   protected boolean repeat = false;
   protected int repeatDelay = 0;
   protected ISound.AttenuationType attenuationType = ISound.AttenuationType.LINEAR;

   protected PositionedSound(ResourceLocation soundResource) {
      this.positionedSoundLocation = soundResource;
   }

   public float getVolume() {
      return this.volume;
   }

   public ResourceLocation getSoundLocation() {
      return this.positionedSoundLocation;
   }

   public float getPitch() {
      return this.pitch;
   }

   public float getXPosF() {
      return this.xPosF;
   }

   public int getRepeatDelay() {
      return this.repeatDelay;
   }

   public float getZPosF() {
      return this.zPosF;
   }

   public float getYPosF() {
      return this.yPosF;
   }

   public boolean canRepeat() {
      return this.repeat;
   }

   public ISound.AttenuationType getAttenuationType() {
      return this.attenuationType;
   }
}
