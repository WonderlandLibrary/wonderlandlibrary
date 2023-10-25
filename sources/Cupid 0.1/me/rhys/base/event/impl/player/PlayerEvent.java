package me.rhys.base.event.impl.player;

import me.rhys.base.event.Event;
import net.minecraft.client.entity.EntityPlayerSP;

public class PlayerEvent extends Event {
  private final EntityPlayerSP player;
  
  public PlayerEvent(EntityPlayerSP player) {
    this.player = player;
  }
  
  public EntityPlayerSP getPlayer() {
    return this.player;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\player\PlayerEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */