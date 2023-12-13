package my.NewSnake.event.events;

import my.NewSnake.event.Event;

public class MoveEvent extends Event {
   private double x;
   private double y;
   private double z;

   public MoveEvent(double var1, double var3, double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
   }

   public void setX(double var1) {
      this.x = var1;
   }

   public double getX() {
      return this.x;
   }

   public double getZ() {
      return this.z;
   }

   public void setY(double var1) {
      this.y = var1;
   }

   public void setZ(double var1) {
      this.z = var1;
   }

   public double getY() {
      return this.y;
   }
}
