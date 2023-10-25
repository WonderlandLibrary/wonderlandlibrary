package me.rhys.base.event.impl.player;

import net.minecraft.client.entity.EntityPlayerSP;

public class PlayerChatEvent extends PlayerEvent {
  private String message;
  
  public void setMessage(String message) {
    this.message = message;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public PlayerChatEvent(EntityPlayerSP player, String message) {
    super(player);
    this.message = message;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\player\PlayerChatEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */