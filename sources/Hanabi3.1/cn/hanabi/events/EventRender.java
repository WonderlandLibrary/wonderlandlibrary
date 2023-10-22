package cn.hanabi.events;

import com.darkmagician6.eventapi.events.Event;

public class EventRender implements Event {
   int pass;
   float partialTicks;
   long finishTimeNano;

   public EventRender(int pass, float partialTicks, long finishTimeNano) {
      this.pass = pass;
      this.partialTicks = partialTicks;
      this.finishTimeNano = finishTimeNano;
   }

   public int getPass() {
      return this.pass;
   }

   public void setPass(int pass) {
      this.pass = pass;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public void setPartialTicks(float partialTicks) {
      this.partialTicks = partialTicks;
   }

   public long getFinishTimeNano() {
      return this.finishTimeNano;
   }

   public void setFinishTimeNano(long finishTimeNano) {
      this.finishTimeNano = finishTimeNano;
   }
}
