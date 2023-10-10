package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public interface ISound {
   float getVolume();

   ResourceLocation getSoundLocation();

   float getPitch();

   float getXPosF();

   int getRepeatDelay();

   float getZPosF();

   float getYPosF();

   boolean canRepeat();

   ISound.AttenuationType getAttenuationType();

   public static enum AttenuationType {
      NONE(0),
      LINEAR(2);

      private final int type;

      private AttenuationType(int typeIn) {
         this.type = typeIn;
      }

      public int getTypeInt() {
         return this.type;
      }
   }
}
