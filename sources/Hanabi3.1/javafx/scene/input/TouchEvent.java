package javafx.scene.input;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;

public final class TouchEvent extends InputEvent {
   private static final long serialVersionUID = 20121107L;
   public static final EventType ANY;
   public static final EventType TOUCH_PRESSED;
   public static final EventType TOUCH_MOVED;
   public static final EventType TOUCH_RELEASED;
   public static final EventType TOUCH_STATIONARY;
   private final int eventSetId;
   private final boolean shiftDown;
   private final boolean controlDown;
   private final boolean altDown;
   private final boolean metaDown;
   private final TouchPoint touchPoint;
   private final List touchPoints;

   public TouchEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("eventType") EventType var3, @NamedArg("touchPoint") TouchPoint var4, @NamedArg("touchPoints") List var5, @NamedArg("eventSetId") int var6, @NamedArg("shiftDown") boolean var7, @NamedArg("controlDown") boolean var8, @NamedArg("altDown") boolean var9, @NamedArg("metaDown") boolean var10) {
      super(var1, var2, var3);
      this.touchPoints = var5 != null ? Collections.unmodifiableList(var5) : null;
      this.eventSetId = var6;
      this.shiftDown = var7;
      this.controlDown = var8;
      this.altDown = var9;
      this.metaDown = var10;
      this.touchPoint = var4;
   }

   public TouchEvent(@NamedArg("eventType") EventType var1, @NamedArg("touchPoint") TouchPoint var2, @NamedArg("touchPoints") List var3, @NamedArg("eventSetId") int var4, @NamedArg("shiftDown") boolean var5, @NamedArg("controlDown") boolean var6, @NamedArg("altDown") boolean var7, @NamedArg("metaDown") boolean var8) {
      this((Object)null, (EventTarget)null, var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public int getTouchCount() {
      return this.touchPoints.size();
   }

   private static void recomputeToSource(TouchEvent var0, Object var1, Object var2) {
      Iterator var3 = var0.touchPoints.iterator();

      while(var3.hasNext()) {
         TouchPoint var4 = (TouchPoint)var3.next();
         var4.recomputeToSource(var1, var2);
      }

   }

   public TouchEvent copyFor(Object var1, EventTarget var2) {
      TouchEvent var3 = (TouchEvent)super.copyFor(var1, var2);
      recomputeToSource(var3, this.getSource(), var1);
      return var3;
   }

   public TouchEvent copyFor(Object var1, EventTarget var2, EventType var3) {
      TouchEvent var4 = this.copyFor(var1, var2);
      var4.eventType = var3;
      return var4;
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   public final int getEventSetId() {
      return this.eventSetId;
   }

   public final boolean isShiftDown() {
      return this.shiftDown;
   }

   public final boolean isControlDown() {
      return this.controlDown;
   }

   public final boolean isAltDown() {
      return this.altDown;
   }

   public final boolean isMetaDown() {
      return this.metaDown;
   }

   public TouchPoint getTouchPoint() {
      return this.touchPoint;
   }

   public List getTouchPoints() {
      return this.touchPoints;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("TouchEvent [");
      var1.append("source = ").append(this.getSource());
      var1.append(", target = ").append(this.getTarget());
      var1.append(", eventType = ").append(this.getEventType());
      var1.append(", consumed = ").append(this.isConsumed());
      var1.append(", touchCount = ").append(this.getTouchCount());
      var1.append(", eventSetId = ").append(this.getEventSetId());
      var1.append(", touchPoint = ").append(this.getTouchPoint().toString());
      return var1.append("]").toString();
   }

   static {
      ANY = new EventType(InputEvent.ANY, "TOUCH");
      TOUCH_PRESSED = new EventType(ANY, "TOUCH_PRESSED");
      TOUCH_MOVED = new EventType(ANY, "TOUCH_MOVED");
      TOUCH_RELEASED = new EventType(ANY, "TOUCH_RELEASED");
      TOUCH_STATIONARY = new EventType(ANY, "TOUCH_STATIONARY");
   }
}
