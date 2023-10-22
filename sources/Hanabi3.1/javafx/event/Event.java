package javafx.event;

import com.sun.javafx.event.EventUtil;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.EventObject;
import javafx.beans.NamedArg;

public class Event extends EventObject implements Cloneable {
   private static final long serialVersionUID = 20121107L;
   public static final EventTarget NULL_SOURCE_TARGET = (var0) -> {
      return var0;
   };
   public static final EventType ANY;
   protected EventType eventType;
   protected transient EventTarget target;
   protected boolean consumed;

   public Event(@NamedArg("eventType") EventType var1) {
      this((Object)null, (EventTarget)null, var1);
   }

   public Event(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("eventType") EventType var3) {
      super(var1 != null ? var1 : NULL_SOURCE_TARGET);
      this.target = var2 != null ? var2 : NULL_SOURCE_TARGET;
      this.eventType = var3;
   }

   public EventTarget getTarget() {
      return this.target;
   }

   public EventType getEventType() {
      return this.eventType;
   }

   public Event copyFor(Object var1, EventTarget var2) {
      Event var3 = (Event)this.clone();
      var3.source = var1 != null ? var1 : NULL_SOURCE_TARGET;
      var3.target = var2 != null ? var2 : NULL_SOURCE_TARGET;
      var3.consumed = false;
      return var3;
   }

   public boolean isConsumed() {
      return this.consumed;
   }

   public void consume() {
      this.consumed = true;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException("Can't clone Event");
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.source = NULL_SOURCE_TARGET;
      this.target = NULL_SOURCE_TARGET;
   }

   public static void fireEvent(EventTarget var0, Event var1) {
      if (var0 == null) {
         throw new NullPointerException("Event target must not be null!");
      } else if (var1 == null) {
         throw new NullPointerException("Event must not be null!");
      } else {
         EventUtil.fireEvent(var0, var1);
      }
   }

   static {
      ANY = EventType.ROOT;
   }
}
