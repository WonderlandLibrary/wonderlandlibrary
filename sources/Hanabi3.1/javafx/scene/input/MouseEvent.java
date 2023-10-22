package javafx.scene.input;

import com.sun.javafx.scene.input.InputEventUtils;
import com.sun.javafx.tk.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point3D;

public class MouseEvent extends InputEvent {
   private static final long serialVersionUID = 20121107L;
   public static final EventType ANY;
   public static final EventType MOUSE_PRESSED;
   public static final EventType MOUSE_RELEASED;
   public static final EventType MOUSE_CLICKED;
   public static final EventType MOUSE_ENTERED_TARGET;
   public static final EventType MOUSE_ENTERED;
   public static final EventType MOUSE_EXITED_TARGET;
   public static final EventType MOUSE_EXITED;
   public static final EventType MOUSE_MOVED;
   public static final EventType MOUSE_DRAGGED;
   public static final EventType DRAG_DETECTED;
   private final Flags flags;
   private transient double x;
   private transient double y;
   private transient double z;
   private final double screenX;
   private final double screenY;
   private final double sceneX;
   private final double sceneY;
   private final MouseButton button;
   private final int clickCount;
   private final boolean stillSincePress;
   private final boolean shiftDown;
   private final boolean controlDown;
   private final boolean altDown;
   private final boolean metaDown;
   private final boolean synthesized;
   private final boolean popupTrigger;
   private final boolean primaryButtonDown;
   private final boolean secondaryButtonDown;
   private final boolean middleButtonDown;
   private PickResult pickResult;

   void recomputeCoordinatesToSource(MouseEvent var1, Object var2) {
      Point3D var3 = InputEventUtils.recomputeCoordinates(this.pickResult, var2);
      this.x = var3.getX();
      this.y = var3.getY();
      this.z = var3.getZ();
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   public MouseEvent copyFor(Object var1, EventTarget var2) {
      MouseEvent var3 = (MouseEvent)super.copyFor(var1, var2);
      var3.recomputeCoordinatesToSource(this, var1);
      return var3;
   }

   public MouseEvent copyFor(Object var1, EventTarget var2, EventType var3) {
      MouseEvent var4 = this.copyFor(var1, var2);
      var4.eventType = var3;
      return var4;
   }

   public MouseEvent(@NamedArg("eventType") EventType var1, @NamedArg("x") double var2, @NamedArg("y") double var4, @NamedArg("screenX") double var6, @NamedArg("screenY") double var8, @NamedArg("button") MouseButton var10, @NamedArg("clickCount") int var11, @NamedArg("shiftDown") boolean var12, @NamedArg("controlDown") boolean var13, @NamedArg("altDown") boolean var14, @NamedArg("metaDown") boolean var15, @NamedArg("primaryButtonDown") boolean var16, @NamedArg("middleButtonDown") boolean var17, @NamedArg("secondaryButtonDown") boolean var18, @NamedArg("synthesized") boolean var19, @NamedArg("popupTrigger") boolean var20, @NamedArg("stillSincePress") boolean var21, @NamedArg("pickResult") PickResult var22) {
      this((Object)null, (EventTarget)null, var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, var22);
   }

   public MouseEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("eventType") EventType var3, @NamedArg("x") double var4, @NamedArg("y") double var6, @NamedArg("screenX") double var8, @NamedArg("screenY") double var10, @NamedArg("button") MouseButton var12, @NamedArg("clickCount") int var13, @NamedArg("shiftDown") boolean var14, @NamedArg("controlDown") boolean var15, @NamedArg("altDown") boolean var16, @NamedArg("metaDown") boolean var17, @NamedArg("primaryButtonDown") boolean var18, @NamedArg("middleButtonDown") boolean var19, @NamedArg("secondaryButtonDown") boolean var20, @NamedArg("synthesized") boolean var21, @NamedArg("popupTrigger") boolean var22, @NamedArg("stillSincePress") boolean var23, @NamedArg("pickResult") PickResult var24) {
      super(var1, var2, var3);
      this.flags = new Flags();
      this.x = var4;
      this.y = var6;
      this.screenX = var8;
      this.screenY = var10;
      this.sceneX = var4;
      this.sceneY = var6;
      this.button = var12;
      this.clickCount = var13;
      this.shiftDown = var14;
      this.controlDown = var15;
      this.altDown = var16;
      this.metaDown = var17;
      this.primaryButtonDown = var18;
      this.middleButtonDown = var19;
      this.secondaryButtonDown = var20;
      this.synthesized = var21;
      this.stillSincePress = var23;
      this.popupTrigger = var22;
      this.pickResult = var24;
      this.pickResult = var24 != null ? var24 : new PickResult(var2, var4, var6);
      Point3D var25 = InputEventUtils.recomputeCoordinates(this.pickResult, (Object)null);
      this.x = var25.getX();
      this.y = var25.getY();
      this.z = var25.getZ();
   }

   public static MouseDragEvent copyForMouseDragEvent(MouseEvent var0, Object var1, EventTarget var2, EventType var3, Object var4, PickResult var5) {
      MouseDragEvent var6 = new MouseDragEvent(var1, var2, var3, var0.sceneX, var0.sceneY, var0.screenX, var0.screenY, var0.button, var0.clickCount, var0.shiftDown, var0.controlDown, var0.altDown, var0.metaDown, var0.primaryButtonDown, var0.middleButtonDown, var0.secondaryButtonDown, var0.synthesized, var0.popupTrigger, var5, var4);
      var6.recomputeCoordinatesToSource(var0, var1);
      return var6;
   }

   public boolean isDragDetect() {
      return this.flags.dragDetect;
   }

   public void setDragDetect(boolean var1) {
      this.flags.dragDetect = var1;
   }

   public final double getX() {
      return this.x;
   }

   public final double getY() {
      return this.y;
   }

   public final double getZ() {
      return this.z;
   }

   public final double getScreenX() {
      return this.screenX;
   }

   public final double getScreenY() {
      return this.screenY;
   }

   public final double getSceneX() {
      return this.sceneX;
   }

   public final double getSceneY() {
      return this.sceneY;
   }

   public final MouseButton getButton() {
      return this.button;
   }

   public final int getClickCount() {
      return this.clickCount;
   }

   public final boolean isStillSincePress() {
      return this.stillSincePress;
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

   public boolean isSynthesized() {
      return this.synthesized;
   }

   public final boolean isShortcutDown() {
      switch (Toolkit.getToolkit().getPlatformShortcutKey()) {
         case SHIFT:
            return this.shiftDown;
         case CONTROL:
            return this.controlDown;
         case ALT:
            return this.altDown;
         case META:
            return this.metaDown;
         default:
            return false;
      }
   }

   public final boolean isPopupTrigger() {
      return this.popupTrigger;
   }

   public final boolean isPrimaryButtonDown() {
      return this.primaryButtonDown;
   }

   public final boolean isSecondaryButtonDown() {
      return this.secondaryButtonDown;
   }

   public final boolean isMiddleButtonDown() {
      return this.middleButtonDown;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("MouseEvent [");
      var1.append("source = ").append(this.getSource());
      var1.append(", target = ").append(this.getTarget());
      var1.append(", eventType = ").append(this.getEventType());
      var1.append(", consumed = ").append(this.isConsumed());
      var1.append(", x = ").append(this.getX()).append(", y = ").append(this.getY()).append(", z = ").append(this.getZ());
      if (this.getButton() != null) {
         var1.append(", button = ").append(this.getButton());
      }

      if (this.getClickCount() > 1) {
         var1.append(", clickCount = ").append(this.getClickCount());
      }

      if (this.isPrimaryButtonDown()) {
         var1.append(", primaryButtonDown");
      }

      if (this.isMiddleButtonDown()) {
         var1.append(", middleButtonDown");
      }

      if (this.isSecondaryButtonDown()) {
         var1.append(", secondaryButtonDown");
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

      if (this.isSynthesized()) {
         var1.append(", synthesized");
      }

      var1.append(", pickResult = ").append(this.getPickResult());
      return var1.append("]").toString();
   }

   public final PickResult getPickResult() {
      return this.pickResult;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.x = this.sceneX;
      this.y = this.sceneY;
   }

   static {
      ANY = new EventType(InputEvent.ANY, "MOUSE");
      MOUSE_PRESSED = new EventType(ANY, "MOUSE_PRESSED");
      MOUSE_RELEASED = new EventType(ANY, "MOUSE_RELEASED");
      MOUSE_CLICKED = new EventType(ANY, "MOUSE_CLICKED");
      MOUSE_ENTERED_TARGET = new EventType(ANY, "MOUSE_ENTERED_TARGET");
      MOUSE_ENTERED = new EventType(MOUSE_ENTERED_TARGET, "MOUSE_ENTERED");
      MOUSE_EXITED_TARGET = new EventType(ANY, "MOUSE_EXITED_TARGET");
      MOUSE_EXITED = new EventType(MOUSE_EXITED_TARGET, "MOUSE_EXITED");
      MOUSE_MOVED = new EventType(ANY, "MOUSE_MOVED");
      MOUSE_DRAGGED = new EventType(ANY, "MOUSE_DRAGGED");
      DRAG_DETECTED = new EventType(ANY, "DRAG_DETECTED");
   }

   private static class Flags implements Cloneable {
      boolean dragDetect;

      private Flags() {
         this.dragDetect = true;
      }

      public Flags clone() {
         try {
            return (Flags)super.clone();
         } catch (CloneNotSupportedException var2) {
            return null;
         }
      }

      // $FF: synthetic method
      Flags(Object var1) {
         this();
      }
   }
}
