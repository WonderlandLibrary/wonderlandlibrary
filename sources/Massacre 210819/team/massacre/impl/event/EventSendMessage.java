package team.massacre.impl.event;

import team.massacre.api.event.Cancellable;
import team.massacre.api.event.Event;

public final class EventSendMessage extends Cancellable implements Event {
   private String message;

   public EventSendMessage(String message) {
      this.message = message;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
