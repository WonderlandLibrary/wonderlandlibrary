package javafx.scene.media;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class MediaErrorEvent extends Event {
   private static final long serialVersionUID = 20121107L;
   public static final EventType MEDIA_ERROR;
   private MediaException error;

   MediaErrorEvent(Object var1, EventTarget var2, MediaException var3) {
      super(var1, var2, MEDIA_ERROR);
      if (var3 == null) {
         throw new IllegalArgumentException("error == null!");
      } else {
         this.error = var3;
      }
   }

   public MediaException getMediaError() {
      return this.error;
   }

   public String toString() {
      return super.toString() + ": source " + this.getSource() + "; target " + this.getTarget() + "; error " + this.error;
   }

   public MediaErrorEvent copyFor(Object var1, EventTarget var2) {
      return (MediaErrorEvent)super.copyFor(var1, var2);
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   static {
      MEDIA_ERROR = new EventType(Event.ANY, "Media Error Event");
   }
}
