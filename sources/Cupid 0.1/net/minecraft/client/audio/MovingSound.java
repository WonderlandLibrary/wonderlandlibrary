package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public abstract class MovingSound extends PositionedSound implements ITickableSound {
  protected boolean donePlaying = false;
  
  protected MovingSound(ResourceLocation location) {
    super(location);
  }
  
  public boolean isDonePlaying() {
    return this.donePlaying;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\audio\MovingSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */