package javafx.scene.input;

import com.sun.javafx.scene.input.InputEventUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point3D;

public class ContextMenuEvent extends InputEvent {
   private static final long serialVersionUID = 20121107L;
   public static final EventType CONTEXT_MENU_REQUESTED;
   public static final EventType ANY;
   private final boolean keyboardTrigger;
   private transient double x;
   private transient double y;
   private transient double z;
   private final double screenX;
   private final double screenY;
   private final double sceneX;
   private final double sceneY;
   private PickResult pickResult;

   public ContextMenuEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("eventType") EventType var3, @NamedArg("x") double var4, @NamedArg("y") double var6, @NamedArg("screenX") double var8, @NamedArg("screenY") double var10, @NamedArg("keyboardTrigger") boolean var12, @NamedArg("pickResult") PickResult var13) {
      super(var1, var2, var3);
      this.screenX = var8;
      this.screenY = var10;
      this.sceneX = var4;
      this.sceneY = var6;
      this.x = var4;
      this.y = var6;
      this.pickResult = var13 != null ? var13 : new PickResult(var2, var4, var6);
      Point3D var14 = InputEventUtils.recomputeCoordinates(this.pickResult, (Object)null);
      this.x = var14.getX();
      this.y = var14.getY();
      this.z = var14.getZ();
      this.keyboardTrigger = var12;
   }

   public ContextMenuEvent(@NamedArg("eventType") EventType var1, @NamedArg("x") double var2, @NamedArg("y") double var4, @NamedArg("screenX") double var6, @NamedArg("screenY") double var8, @NamedArg("keyboardTrigger") boolean var10, @NamedArg("pickResult") PickResult var11) {
      this((Object)null, (EventTarget)null, var1, var2, var4, var6, var8, var10, var11);
   }

   private void recomputeCoordinatesToSource(ContextMenuEvent var1, Object var2) {
      Point3D var3 = InputEventUtils.recomputeCoordinates(this.pickResult, var2);
      var1.x = var3.getX();
      var1.y = var3.getY();
      var1.z = var3.getZ();
   }

   public ContextMenuEvent copyFor(Object var1, EventTarget var2) {
      ContextMenuEvent var3 = (ContextMenuEvent)super.copyFor(var1, var2);
      this.recomputeCoordinatesToSource(var3, var1);
      return var3;
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   public boolean isKeyboardTrigger() {
      return this.keyboardTrigger;
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

   public final PickResult getPickResult() {
      return this.pickResult;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("ContextMenuEvent [");
      var1.append("source = ").append(this.getSource());
      var1.append(", target = ").append(this.getTarget());
      var1.append(", eventType = ").append(this.getEventType());
      var1.append(", consumed = ").append(this.isConsumed());
      var1.append(", x = ").append(this.getX()).append(", y = ").append(this.getY()).append(", z = ").append(this.getZ());
      var1.append(", pickResult = ").append(this.getPickResult());
      return var1.append("]").toString();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.x = this.sceneX;
      this.y = this.sceneY;
   }

   static {
      CONTEXT_MENU_REQUESTED = new EventType(InputEvent.ANY, "CONTEXTMENUREQUESTED");
      ANY = CONTEXT_MENU_REQUESTED;
   }
}
