package team.massacre.impl.event;

import team.massacre.api.event.Cancellable;
import team.massacre.api.event.Event;

public class EventJump extends Cancellable implements Event {
   public double motionY;

   public EventJump(double motionY) {
      this.motionY = motionY;
   }

   public void setMotionY(double d) {
      this.motionY = d;
   }

   public double getMotionY() {
      return this.motionY;
   }
}
