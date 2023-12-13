package my.NewSnake.event.events;

import my.NewSnake.event.Event;

public class StepEvent extends Event {
   private double realHeight;
   private double stepHeight;
   private boolean active;
   private Event.State state;

   public boolean isActive() {
      return this.active;
   }

   public void setStepHeight(double var1) {
      this.stepHeight = var1;
   }

   public void setRealHeight(double var1) {
      this.realHeight = var1;
   }

   public StepEvent(double var1, Event.State var3, double var4) {
      this.stepHeight = var1;
      this.state = var3;
      this.realHeight = var4;
   }

   public void setState(Event.State var1) {
      this.state = var1;
   }

   public Event.State getState() {
      return this.state;
   }

   public double getRealHeight() {
      return this.realHeight;
   }

   public void setActive(boolean var1) {
      this.active = var1;
   }

   public double getStepHeight() {
      return this.stepHeight;
   }

   public StepEvent(double var1, Event.State var3) {
      this.stepHeight = var1;
      this.state = var3;
   }
}
