package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public abstract class MovingSound extends PositionedSound implements ITickableSound {
   protected boolean donePlaying = false;

   protected MovingSound(ResourceLocation var1) {
      super(var1);
   }

   public boolean isDonePlaying() {
      return this.donePlaying;
   }
}
