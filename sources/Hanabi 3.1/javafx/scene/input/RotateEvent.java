package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;

public final class RotateEvent extends GestureEvent {
   private static final long serialVersionUID = 20121107L;
   public static final EventType ANY;
   public static final EventType ROTATE;
   public static final EventType ROTATION_STARTED;
   public static final EventType ROTATION_FINISHED;
   private final double angle;
   private final double totalAngle;

   public RotateEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("eventType") EventType var3, @NamedArg("x") double var4, @NamedArg("y") double var6, @NamedArg("screenX") double var8, @NamedArg("screenY") double var10, @NamedArg("shiftDown") boolean var12, @NamedArg("controlDown") boolean var13, @NamedArg("altDown") boolean var14, @NamedArg("metaDown") boolean var15, @NamedArg("direct") boolean var16, @NamedArg("inertia") boolean var17, @NamedArg("angle") double var18, @NamedArg("totalAngle") double var20, @NamedArg("pickResult") PickResult var22) {
      super(var1, var2, var3, var4, var6, var8, var10, var12, var13, var14, var15, var16, var17, var22);
      this.angle = var18;
      this.totalAngle = var20;
   }

   public RotateEvent(@NamedArg("eventType") EventType var1, @NamedArg("x") double var2, @NamedArg("y") double var4, @NamedArg("screenX") double var6, @NamedArg("screenY") double var8, @NamedArg("shiftDown") boolean var10, @NamedArg("controlDown") boolean var11, @NamedArg("altDown") boolean var12, @NamedArg("metaDown") boolean var13, @NamedArg("direct") boolean var14, @NamedArg("inertia") boolean var15, @NamedArg("angle") double var16, @NamedArg("totalAngle") double var18, @NamedArg("pickResult") PickResult var20) {
      this((Object)null, (EventTarget)null, var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, var16, var18, var20);
   }

   public double getAngle() {
      return this.angle;
   }

   public double getTotalAngle() {
      return this.totalAngle;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("RotateEvent [");
      var1.append("source = ").append(this.getSource());
      var1.append(", target = ").append(this.getTarget());
      var1.append(", eventType = ").append(this.getEventType());
      var1.append(", consumed = ").append(this.isConsumed());
      var1.append(", angle = ").append(this.getAngle());
      var1.append(", totalAngle = ").append(this.getTotalAngle());
      var1.append(", x = ").append(this.getX()).append(", y = ").append(this.getY()).append(", z = ").append(this.getZ());
      var1.append(this.isDirect() ? ", direct" : ", indirect");
      if (this.isInertia()) {
         var1.append(", inertia");
      }

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

   public RotateEvent copyFor(Object var1, EventTarget var2) {
      return (RotateEvent)super.copyFor(var1, var2);
   }

   public RotateEvent copyFor(Object var1, EventTarget var2, EventType var3) {
      RotateEvent var4 = this.copyFor(var1, var2);
      var4.eventType = var3;
      return var4;
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   static {
      ANY = new EventType(GestureEvent.ANY, "ANY_ROTATE");
      ROTATE = new EventType(ANY, "ROTATE");
      ROTATION_STARTED = new EventType(ANY, "ROTATION_STARTED");
      ROTATION_FINISHED = new EventType(ANY, "ROTATION_FINISHED");
   }
}
