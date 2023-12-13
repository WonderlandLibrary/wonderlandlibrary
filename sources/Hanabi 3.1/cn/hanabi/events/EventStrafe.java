package cn.hanabi.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventStrafe extends EventCancellable {
   public float forward;
   public float strafe;
   public float friction;
   public float yaw;

   public EventStrafe(float forward, float strafe, float friction, float yaw) {
      this.forward = forward;
      this.strafe = strafe;
      this.friction = friction;
      this.yaw = yaw;
   }
}
