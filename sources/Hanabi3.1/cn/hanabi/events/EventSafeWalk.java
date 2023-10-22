package cn.hanabi.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventSafeWalk extends EventCancellable {
   public boolean safe;

   public EventSafeWalk(boolean safe) {
      this.safe = safe;
   }

   public boolean getSafe() {
      return this.safe;
   }

   public void setSafe(boolean safe) {
      this.safe = safe;
   }
}
