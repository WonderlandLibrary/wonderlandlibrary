package javafx.scene.input;

import com.sun.javafx.scene.input.InputEventUtils;
import com.sun.javafx.tk.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point3D;

public class GestureEvent extends InputEvent {
   private static final long serialVersionUID = 20121107L;
   public static final EventType ANY;
   private transient double x;
   private transient double y;
   private transient double z;
   private final double screenX;
   private final double screenY;
   private final double sceneX;
   private final double sceneY;
   private final boolean shiftDown;
   private final boolean controlDown;
   private final boolean altDown;
   private final boolean metaDown;
   private final boolean direct;
   private final boolean inertia;
   private PickResult pickResult;

   /** @deprecated */
   @Deprecated
   protected GestureEvent(EventType var1) {
      this(var1, 0.0, 0.0, 0.0, 0.0, false, false, false, false, false, false, (PickResult)null);
   }

   /** @deprecated */
   @Deprecated
   protected GestureEvent(Object var1, EventTarget var2, EventType var3) {
      super(var1, var2, var3);
      this.x = this.y = this.screenX = this.screenY = this.sceneX = this.sceneY = 0.0;
      this.shiftDown = this.controlDown = this.altDown = this.metaDown = this.direct = this.inertia = false;
   }

   protected GestureEvent(Object var1, EventTarget var2, EventType var3, double var4, double var6, double var8, double var10, boolean var12, boolean var13, boolean var14, boolean var15, boolean var16, boolean var17, PickResult var18) {
      super(var1, var2, var3);
      this.x = var4;
      this.y = var6;
      this.screenX = var8;
      this.screenY = var10;
      this.sceneX = var4;
      this.sceneY = var6;
      this.shiftDown = var12;
      this.controlDown = var13;
      this.altDown = var14;
      this.metaDown = var15;
      this.direct = var16;
      this.inertia = var17;
      this.pickResult = var18 != null ? var18 : new PickResult(var2, var4, var6);
      Point3D var19 = InputEventUtils.recomputeCoordinates(this.pickResult, (Object)null);
      this.x = var19.getX();
      this.y = var19.getY();
      this.z = var19.getZ();
   }

   protected GestureEvent(EventType var1, double var2, double var4, double var6, double var8, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15, PickResult var16) {
      this((Object)null, (EventTarget)null, var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, var16);
   }

   private void recomputeCoordinatesToSource(GestureEvent var1, Object var2) {
      Point3D var3 = InputEventUtils.recomputeCoordinates(this.pickResult, var2);
      var1.x = var3.getX();
      var1.y = var3.getY();
      var1.z = var3.getZ();
   }

   public GestureEvent copyFor(Object var1, EventTarget var2) {
      GestureEvent var3 = (GestureEvent)super.copyFor(var1, var2);
      this.recomputeCoordinatesToSource(var3, var1);
      return var3;
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

   public final boolean isDirect() {
      return this.direct;
   }

   public boolean isInertia() {
      return this.inertia;
   }

   public final PickResult getPickResult() {
      return this.pickResult;
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

   public String toString() {
      StringBuilder var1 = new StringBuilder("GestureEvent [");
      var1.append("source = ").append(this.getSource());
      var1.append(", target = ").append(this.getTarget());
      var1.append(", eventType = ").append(this.getEventType());
      var1.append(", consumed = ").append(this.isConsumed());
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

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.x = this.sceneX;
      this.y = this.sceneY;
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   static {
      ANY = new EventType(InputEvent.ANY, "GESTURE");
   }
}
