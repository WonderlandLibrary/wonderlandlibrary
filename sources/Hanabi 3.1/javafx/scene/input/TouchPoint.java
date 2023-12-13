package javafx.scene.input;

import com.sun.javafx.scene.input.InputEventUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.Scene;

public final class TouchPoint implements Serializable {
   private transient EventTarget target;
   private transient Object source;
   private EventTarget grabbed = null;
   private int id;
   private State state;
   private transient double x;
   private transient double y;
   private transient double z;
   private double screenX;
   private double screenY;
   private double sceneX;
   private double sceneY;
   private PickResult pickResult;

   public TouchPoint(@NamedArg("id") int var1, @NamedArg("state") State var2, @NamedArg("x") double var3, @NamedArg("y") double var5, @NamedArg("screenX") double var7, @NamedArg("screenY") double var9, @NamedArg("target") EventTarget var11, @NamedArg("pickResult") PickResult var12) {
      this.target = var11;
      this.id = var1;
      this.state = var2;
      this.x = var3;
      this.y = var5;
      this.sceneX = var3;
      this.sceneY = var5;
      this.screenX = var7;
      this.screenY = var9;
      this.pickResult = var12 != null ? var12 : new PickResult(var11, var3, var5);
      Point3D var13 = InputEventUtils.recomputeCoordinates(this.pickResult, (Object)null);
      this.x = var13.getX();
      this.y = var13.getY();
      this.z = var13.getZ();
   }

   void recomputeToSource(Object var1, Object var2) {
      Point3D var3 = InputEventUtils.recomputeCoordinates(this.pickResult, var2);
      this.x = var3.getX();
      this.y = var3.getY();
      this.z = var3.getZ();
      this.source = var2;
   }

   public boolean belongsTo(EventTarget var1) {
      if (this.target instanceof Node) {
         Object var2 = (Node)this.target;
         if (var1 instanceof Scene) {
            return ((Node)var2).getScene() == var1;
         }

         while(var2 != null) {
            if (var2 == var1) {
               return true;
            }

            var2 = ((Node)var2).getParent();
         }
      }

      return var1 == this.target;
   }

   /** @deprecated */
   @Deprecated
   public void impl_reset() {
      Point3D var1 = InputEventUtils.recomputeCoordinates(this.pickResult, (Object)null);
      this.x = var1.getX();
      this.y = var1.getY();
      this.z = var1.getZ();
   }

   public EventTarget getGrabbed() {
      return this.grabbed;
   }

   public void grab() {
      if (this.source instanceof EventTarget) {
         this.grabbed = (EventTarget)this.source;
      } else {
         throw new IllegalStateException("Cannot grab touch point, source is not an instance of EventTarget: " + this.source);
      }
   }

   public void grab(EventTarget var1) {
      this.grabbed = var1;
   }

   public void ungrab() {
      this.grabbed = null;
   }

   public final int getId() {
      return this.id;
   }

   public final State getState() {
      return this.state;
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

   public EventTarget getTarget() {
      return this.target;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("TouchPoint [");
      var1.append("state = ").append(this.getState());
      var1.append(", id = ").append(this.getId());
      var1.append(", target = ").append(this.getTarget());
      var1.append(", x = ").append(this.getX()).append(", y = ").append(this.getY()).append(", z = ").append(this.getZ());
      var1.append(", pickResult = ").append(this.getPickResult());
      return var1.append("]").toString();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.x = this.sceneX;
      this.y = this.sceneY;
   }

   public static enum State {
      PRESSED,
      MOVED,
      STATIONARY,
      RELEASED;
   }
}
