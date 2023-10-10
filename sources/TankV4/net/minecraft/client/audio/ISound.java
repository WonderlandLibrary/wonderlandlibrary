package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public interface ISound {
   float getZPosF();

   ResourceLocation getSoundLocation();

   float getXPosF();

   int getRepeatDelay();

   float getPitch();

   boolean canRepeat();

   float getYPosF();

   float getVolume();

   ISound.AttenuationType getAttenuationType();

   public static enum AttenuationType {
      LINEAR(2),
      NONE(0);

      private static final ISound.AttenuationType[] ENUM$VALUES = new ISound.AttenuationType[]{NONE, LINEAR};
      private final int type;

      private AttenuationType(int var3) {
         this.type = var3;
      }

      public int getTypeInt() {
         return this.type;
      }
   }
}
