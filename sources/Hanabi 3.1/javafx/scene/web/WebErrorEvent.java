package javafx.scene.web;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public final class WebErrorEvent extends Event {
   public static final EventType ANY;
   public static final EventType USER_DATA_DIRECTORY_ALREADY_IN_USE;
   public static final EventType USER_DATA_DIRECTORY_IO_ERROR;
   public static final EventType USER_DATA_DIRECTORY_SECURITY_ERROR;
   private final String message;
   private final Throwable exception;

   public WebErrorEvent(@NamedArg("source") Object var1, @NamedArg("type") EventType var2, @NamedArg("message") String var3, @NamedArg("exception") Throwable var4) {
      super(var1, (EventTarget)null, var2);
      this.message = var3;
      this.exception = var4;
   }

   public String getMessage() {
      return this.message;
   }

   public Throwable getException() {
      return this.exception;
   }

   public String toString() {
      return String.format("WebErrorEvent [source = %s, eventType = %s, message = \"%s\", exception = %s]", this.getSource(), this.getEventType(), this.getMessage(), this.getException());
   }

   static {
      ANY = new EventType(Event.ANY, "WEB_ERROR");
      USER_DATA_DIRECTORY_ALREADY_IN_USE = new EventType(ANY, "USER_DATA_DIRECTORY_ALREADY_IN_USE");
      USER_DATA_DIRECTORY_IO_ERROR = new EventType(ANY, "USER_DATA_DIRECTORY_IO_ERROR");
      USER_DATA_DIRECTORY_SECURITY_ERROR = new EventType(ANY, "USER_DATA_DIRECTORY_SECURITY_ERROR");
   }
}
