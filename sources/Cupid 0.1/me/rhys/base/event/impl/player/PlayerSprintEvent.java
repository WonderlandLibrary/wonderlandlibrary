package me.rhys.base.event.impl.player;

import net.minecraft.client.entity.EntityPlayerSP;

public class PlayerSprintEvent extends PlayerEvent {
  private boolean sprinting;
  
  public void setSprinting(boolean sprinting) {
    this.sprinting = sprinting;
  }
  
  public boolean isSprinting() {
    return this.sprinting;
  }
  
  public PlayerSprintEvent(EntityPlayerSP playerSP, boolean sprinting) {
    super(playerSP);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\player\PlayerSprintEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */