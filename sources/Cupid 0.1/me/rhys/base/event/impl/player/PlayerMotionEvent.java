package me.rhys.base.event.impl.player;

import me.rhys.base.util.entity.Location;
import net.minecraft.client.entity.EntityPlayerSP;

public class PlayerMotionEvent extends PlayerEvent {
  public static Location lastLocation;
  
  public static Location position;
  
  private boolean onGround;
  
  public PlayerMotionEvent(EntityPlayerSP playerSP, Location position, boolean onGround) {
    super(playerSP);
    PlayerMotionEvent.position = position;
    this.onGround = onGround;
  }
  
  public void setOnGround(boolean b) {
    this.onGround = b;
  }
  
  public Location getPosition() {
    return position;
  }
  
  public boolean isOnGround() {
    return this.onGround;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\player\PlayerMotionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */