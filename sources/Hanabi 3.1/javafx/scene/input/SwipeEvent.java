package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;

public final class SwipeEvent extends GestureEvent {
   private static final long serialVersionUID = 20121107L;
   public static final EventType ANY;
   public static final EventType SWIPE_LEFT;
   public static final EventType SWIPE_RIGHT;
   public static final EventType SWIPE_UP;
   public static final EventType SWIPE_DOWN;
   private final int touchCount;

   public SwipeEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("eventType") EventType var3, @NamedArg("x") double var4, @NamedArg("y") double var6, @NamedArg("screenX") double var8, @NamedArg("screenY") double var10, @NamedArg("shiftDown") boolean var12, @NamedArg("controlDown") boolean var13, @NamedArg("altDown") boolean var14, @NamedArg("metaDown") boolean var15, @NamedArg("direct") boolean var16, @NamedArg("touchCount") int var17, @NamedArg("pickResult") PickResult var18) {
      super(var1, var2, var3, var4, var6, var8, var10, var12, var13, var14, var15, var16, false, var18);
      this.touchCount = var17;
   }

   public SwipeEvent(@NamedArg("eventType") EventType var1, @NamedArg("x") double var2, @NamedArg("y") double var4, @NamedArg("screenX") double var6, @NamedArg("screenY") double var8, @NamedArg("shiftDown") boolean var10, @NamedArg("controlDown") boolean var11, @NamedArg("altDown") boolean var12, @NamedArg("metaDown") boolean var13, @NamedArg("direct") boolean var14, @NamedArg("touchCount") int var15, @NamedArg("pickResult") PickResult var16) {
      this((Object)null, (EventTarget)null, var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, var16);
   }

   public int getTouchCount() {
      return this.touchCount;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("SwipeEvent [");
      var1.append("source = ").append(this.getSource());
      var1.append(", target = ").append(this.getTarget());
      var1.append(", eventType = ").append(this.getEventType());
      var1.append(", consumed = ").append(this.isConsumed());
      var1.append(", touchCount = ").append(this.getTouchCount());
      var1.append(", x = ").append(this.getX()).append(", y = ").append(this.getY()).append(", z = ").append(this.getZ());
      var1.append(this.isDirect() ? ", direct" : ", indirect");
      if (this.isShiftDown()) {
         var1.append(", shiftDown");
      }

      if (this.isControlDown()) {
         var1.append(", controlDown");
      }

      if (this.isAltDown()) {
         var1.append(", altDown");
      }

      if (this.isMetaDown()) {
         var1.append(", metaDown");
      }

      if (this.isShortcutDown()) {
         var1.append(", shortcutDown");
      }

      var1.append(", pickResult = ").append(this.getPickResult());
      return var1.append("]").toString();
   }

   public SwipeEvent copyFor(Object var1, EventTarget var2) {
      return (SwipeEvent)super.copyFor(var1, var2);
   }

   public SwipeEvent copyFor(Object var1, EventTarget var2, EventType var3) {
      SwipeEvent var4 = this.copyFor(var1, var2);
      var4.eventType = var3;
      return var4;
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   static {
      ANY = new EventType(GestureEvent.ANY, "ANY_SWIPE");
      SWIPE_LEFT = new EventType(ANY, "SWIPE_LEFT");
      SWIPE_RIGHT = new EventType(ANY, "SWIPE_RIGHT");
      SWIPE_UP = new EventType(ANY, "SWIPE_UP");
      SWIPE_DOWN = new EventType(ANY, "SWIPE_DOWN");
   }
}
