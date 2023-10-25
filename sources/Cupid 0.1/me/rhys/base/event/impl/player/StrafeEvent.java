package me.rhys.base.event.impl.player;

import me.rhys.base.event.Event;

public class StrafeEvent extends Event {
  public float strafe;
  
  public float friction;
  
  public float forward;
  
  public float getForward() {
    return this.forward;
  }
  
  public float getFriction() {
    return this.friction;
  }
  
  public float getStrafe() {
    return this.strafe;
  }
  
  public StrafeEvent(float strafe, float friction, float forward) {
    this.strafe = strafe;
    this.friction = friction;
    this.forward = forward;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\player\StrafeEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */