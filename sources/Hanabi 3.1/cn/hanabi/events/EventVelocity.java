package cn.hanabi.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventVelocity extends EventCancellable {
   public double x;
   public double y;
   public double z;

   public EventVelocity(double a, double b, double c) {
      this.x = a;
      this.y = b;
      this.z = c;
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = y;
   }

   public double getZ() {
      return this.z;
   }

   public void setZ(double z) {
      this.z = z;
   }
}
