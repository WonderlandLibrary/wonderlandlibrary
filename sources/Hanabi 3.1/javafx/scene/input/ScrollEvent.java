package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;

public final class ScrollEvent extends GestureEvent {
   private static final long serialVersionUID = 20121107L;
   public static final EventType ANY;
   public static final EventType SCROLL;
   public static final EventType SCROLL_STARTED;
   public static final EventType SCROLL_FINISHED;
   private final double deltaX;
   private final double deltaY;
   private double totalDeltaX;
   private final double totalDeltaY;
   private final HorizontalTextScrollUnits textDeltaXUnits;
   private final VerticalTextScrollUnits textDeltaYUnits;
   private final double textDeltaX;
   private final double textDeltaY;
   private final int touchCount;
   private final double multiplierX;
   private final double multiplierY;

   private ScrollEvent(Object var1, EventTarget var2, EventType var3, double var4, double var6, double var8, double var10, boolean var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, double var18, double var20, double var22, double var24, double var26, double var28, HorizontalTextScrollUnits var30, double var31, VerticalTextScrollUnits var33, double var34, int var36, PickResult var37) {
      super(var1, var2, var3, var4, var6, var8, var10, var12, var13, var14, var15, var16, var17, var37);
      this.deltaX = var18;
      this.deltaY = var20;
      this.totalDeltaX = var22;
      this.totalDeltaY = var24;
      this.textDeltaXUnits = var30;
      this.textDeltaX = var31;
      this.textDeltaYUnits = var33;
      this.textDeltaY = var34;
      this.touchCount = var36;
      this.multiplierX = var26;
      this.multiplierY = var28;
   }

   public ScrollEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("eventType") EventType var3, @NamedArg("x") double var4, @NamedArg("y") double var6, @NamedArg("screenX") double var8, @NamedArg("screenY") double var10, @NamedArg("shiftDown") boolean var12, @NamedArg("controlDown") boolean var13, @NamedArg("altDown") boolean var14, @NamedArg("metaDown") boolean var15, @NamedArg("direct") boolean var16, @NamedArg("inertia") boolean var17, @NamedArg("deltaX") double var18, @NamedArg("deltaY") double var20, @NamedArg("totalDeltaX") double var22, @NamedArg("totalDeltaY") double var24, @NamedArg("textDeltaXUnits") HorizontalTextScrollUnits var26, @NamedArg("textDeltaX") double var27, @NamedArg("textDeltaYUnits") VerticalTextScrollUnits var29, @NamedArg("textDeltaY") double var30, @NamedArg("touchCount") int var32, @NamedArg("pickResult") PickResult var33) {
      this(var1, var2, var3, var4, var6, var8, var10, var12, var13, var14, var15, var16, var17, var18, var20, var22, var24, 1.0, 1.0, var26, var27, var29, var30, var32, var33);
   }

   public ScrollEvent(@NamedArg("eventType") EventType var1, @NamedArg("x") double var2, @NamedArg("y") double var4, @NamedArg("screenX") double var6, @NamedArg("screenY") double var8, @NamedArg("shiftDown") boolean var10, @NamedArg("controlDown") boolean var11, @NamedArg("altDown") boolean var12, @NamedArg("metaDown") boolean var13, @NamedArg("direct") boolean var14, @NamedArg("inertia") boolean var15, @NamedArg("deltaX") double var16, @NamedArg("deltaY") double var18, @NamedArg("totalDeltaX") double var20, @NamedArg("totalDeltaY") double var22, @NamedArg("textDeltaXUnits") HorizontalTextScrollUnits var24, @NamedArg("textDeltaX") double var25, @NamedArg("textDeltaYUnits") VerticalTextScrollUnits var27, @NamedArg("textDeltaY") double var28, @NamedArg("touchCount") int var30, @NamedArg("pickResult") PickResult var31) {
      this((Object)null, (EventTarget)null, var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, var16, var18, var20, var22, 1.0, 1.0, var24, var25, var27, var28, var30, var31);
   }

   public ScrollEvent(@NamedArg("eventType") EventType var1, @NamedArg("x") double var2, @NamedArg("y") double var4, @NamedArg("screenX") double var6, @NamedArg("screenY") double var8, @NamedArg("shiftDown") boolean var10, @NamedArg("controlDown") boolean var11, @NamedArg("altDown") boolean var12, @NamedArg("metaDown") boolean var13, @NamedArg("direct") boolean var14, @NamedArg("inertia") boolean var15, @NamedArg("deltaX") double var16, @NamedArg("deltaY") double var18, @NamedArg("totalDeltaX") double var20, @NamedArg("totalDeltaY") double var22, @NamedArg("multiplierX") double var24, @NamedArg("multiplierY") double var26, @NamedArg("textDeltaXUnits") HorizontalTextScrollUnits var28, @NamedArg("textDeltaX") double var29, @NamedArg("textDeltaYUnits") VerticalTextScrollUnits var31, @NamedArg("textDeltaY") double var32, @NamedArg("touchCount") int var34, @NamedArg("pickResult") PickResult var35) {
      this((Object)null, (EventTarget)null, var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, var16, var18, var20, var22, var24, var26, var28, var29, var31, var32, var34, var35);
   }

   public double getDeltaX() {
      return this.deltaX;
   }

   public double getDeltaY() {
      return this.deltaY;
   }

   public double getTotalDeltaX() {
      return this.totalDeltaX;
   }

   public double getTotalDeltaY() {
      return this.totalDeltaY;
   }

   public HorizontalTextScrollUnits getTextDeltaXUnits() {
      return this.textDeltaXUnits;
   }

   public VerticalTextScrollUnits getTextDeltaYUnits() {
      return this.textDeltaYUnits;
   }

   public double getTextDeltaX() {
      return this.textDeltaX;
   }

   public double getTextDeltaY() {
      return this.textDeltaY;
   }

   public int getTouchCount() {
      return this.touchCount;
   }

   public double getMultiplierX() {
      return this.multiplierX;
   }

   public double getMultiplierY() {
      return this.multiplierY;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("ScrollEvent [");
      var1.append("source = ").append(this.getSource());
      var1.append(", target = ").append(this.getTarget());
      var1.append(", eventType = ").append(this.getEventType());
      var1.append(", consumed = ").append(this.isConsumed());
      var1.append(", deltaX = ").append(this.getDeltaX()).append(", deltaY = ").append(this.getDeltaY());
      var1.append(", totalDeltaX = ").append(this.getTotalDeltaX()).append(", totalDeltaY = ").append(this.getTotalDeltaY());
      var1.append(", textDeltaXUnits = ").append(this.getTextDeltaXUnits()).append(", textDeltaX = ").append(this.getTextDeltaX());
      var1.append(", textDeltaYUnits = ").append(this.getTextDeltaYUnits()).append(", textDeltaY = ").append(this.getTextDeltaY());
      var1.append(", touchCount = ").append(this.getTouchCount());
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

   public ScrollEvent copyFor(Object var1, EventTarget var2) {
      return (ScrollEvent)super.copyFor(var1, var2);
   }

   public ScrollEvent copyFor(Object var1, EventTarget var2, EventType var3) {
      ScrollEvent var4 = this.copyFor(var1, var2);
      var4.eventType = var3;
      return var4;
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   static {
      ANY = new EventType(GestureEvent.ANY, "ANY_SCROLL");
      SCROLL = new EventType(ANY, "SCROLL");
      SCROLL_STARTED = new EventType(ANY, "SCROLL_STARTED");
      SCROLL_FINISHED = new EventType(ANY, "SCROLL_FINISHED");
   }

   public static enum VerticalTextScrollUnits {
      NONE,
      LINES,
      PAGES;
   }

   public static enum HorizontalTextScrollUnits {
      NONE,
      CHARACTERS;
   }
}
