package javafx.scene.input;

import com.sun.javafx.scene.input.InputEventUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point3D;

public final class DragEvent extends InputEvent {
   private static final long serialVersionUID = 20121107L;
   public static final EventType ANY;
   public static final EventType DRAG_ENTERED_TARGET;
   public static final EventType DRAG_ENTERED;
   public static final EventType DRAG_EXITED_TARGET;
   public static final EventType DRAG_EXITED;
   public static final EventType DRAG_OVER;
   public static final EventType DRAG_DROPPED;
   public static final EventType DRAG_DONE;
   private transient double x;
   private transient double y;
   private transient double z;
   private final double screenX;
   private final double screenY;
   private final double sceneX;
   private final double sceneY;
   private PickResult pickResult;
   private Object gestureSource;
   private Object gestureTarget;
   private TransferMode transferMode;
   private final State state;
   private transient Dragboard dragboard;

   public DragEvent copyFor(Object var1, EventTarget var2, Object var3, Object var4, EventType var5) {
      DragEvent var6 = this.copyFor(var1, var2, var5);
      this.recomputeCoordinatesToSource(var6, var1);
      var6.gestureSource = var3;
      var6.gestureTarget = var4;
      return var6;
   }

   public DragEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("eventType") EventType var3, @NamedArg("dragboard") Dragboard var4, @NamedArg("x") double var5, @NamedArg("y") double var7, @NamedArg("screenX") double var9, @NamedArg("screenY") double var11, @NamedArg("transferMode") TransferMode var13, @NamedArg("gestureSource") Object var14, @NamedArg("gestureTarget") Object var15, @NamedArg("pickResult") PickResult var16) {
      super(var1, var2, var3);
      this.state = new State();
      this.gestureSource = var14;
      this.gestureTarget = var15;
      this.x = var5;
      this.y = var7;
      this.screenX = var9;
      this.screenY = var11;
      this.sceneX = var5;
      this.sceneY = var7;
      this.transferMode = var13;
      this.dragboard = var4;
      if (var3 == DRAG_DROPPED || var3 == DRAG_DONE) {
         this.state.accepted = var13 != null;
         this.state.acceptedTrasferMode = var13;
      }

      this.pickResult = var16 != null ? var16 : new PickResult(var3 == DRAG_DONE ? null : var2, var5, var7);
      Point3D var17 = InputEventUtils.recomputeCoordinates(this.pickResult, (Object)null);
      this.x = var17.getX();
      this.y = var17.getY();
      this.z = var17.getZ();
   }

   public DragEvent(@NamedArg("eventType") EventType var1, @NamedArg("dragboard") Dragboard var2, @NamedArg("x") double var3, @NamedArg("y") double var5, @NamedArg("screenX") double var7, @NamedArg("screenY") double var9, @NamedArg("transferMode") TransferMode var11, @NamedArg("gestureSource") Object var12, @NamedArg("gestureTarget") Object var13, @NamedArg("pickResult") PickResult var14) {
      this((Object)null, (EventTarget)null, var1, var2, var3, var5, var7, var9, var11, var12, var13, var14);
   }

   private void recomputeCoordinatesToSource(DragEvent var1, Object var2) {
      if (var1.getEventType() != DRAG_DONE) {
         Point3D var3 = InputEventUtils.recomputeCoordinates(this.pickResult, var2);
         var1.x = var3.getX();
         var1.y = var3.getY();
         var1.z = var3.getZ();
      }
   }

   public DragEvent copyFor(Object var1, EventTarget var2) {
      DragEvent var3 = (DragEvent)super.copyFor(var1, var2);
      this.recomputeCoordinatesToSource(var3, var1);
      return var3;
   }

   public DragEvent copyFor(Object var1, EventTarget var2, EventType var3) {
      DragEvent var4 = this.copyFor(var1, var2);
      var4.eventType = var3;
      return var4;
   }

   public EventType getEventType() {
      return super.getEventType();
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

   public final Object getGestureSource() {
      return this.gestureSource;
   }

   public final Object getGestureTarget() {
      return this.gestureTarget;
   }

   public final TransferMode getTransferMode() {
      return this.transferMode;
   }

   public final boolean isAccepted() {
      return this.state.accepted;
   }

   public final TransferMode getAcceptedTransferMode() {
      return this.state.acceptedTrasferMode;
   }

   public final Object getAcceptingObject() {
      return this.state.acceptingObject;
   }

   public final Dragboard getDragboard() {
      return this.dragboard;
   }

   private static TransferMode chooseTransferMode(Set var0, TransferMode[] var1, TransferMode var2) {
      TransferMode var3 = null;
      EnumSet var4 = EnumSet.noneOf(TransferMode.class);
      Iterator var5 = InputEventUtils.safeTransferModes(var1).iterator();

      while(var5.hasNext()) {
         TransferMode var6 = (TransferMode)var5.next();
         if (var0.contains(var6)) {
            var4.add(var6);
         }
      }

      if (var4.contains(var2)) {
         var3 = var2;
      } else if (var4.contains(TransferMode.MOVE)) {
         var3 = TransferMode.MOVE;
      } else if (var4.contains(TransferMode.COPY)) {
         var3 = TransferMode.COPY;
      } else if (var4.contains(TransferMode.LINK)) {
         var3 = TransferMode.LINK;
      }

      return var3;
   }

   public void acceptTransferModes(TransferMode... var1) {
      if (this.dragboard != null && this.dragboard.getTransferModes() != null && this.transferMode != null) {
         TransferMode var2 = chooseTransferMode(this.dragboard.getTransferModes(), var1, this.transferMode);
         if (var2 == null && this.getEventType() == DRAG_DROPPED) {
            throw new IllegalStateException("Accepting unsupported transfer modes inside DRAG_DROPPED handler");
         } else {
            this.state.accepted = var2 != null;
            this.state.acceptedTrasferMode = var2;
            this.state.acceptingObject = this.state.accepted ? this.source : null;
         }
      } else {
         this.state.accepted = false;
      }
   }

   public void setDropCompleted(boolean var1) {
      if (this.getEventType() != DRAG_DROPPED) {
         throw new IllegalStateException("setDropCompleted can be called only from DRAG_DROPPED handler");
      } else {
         this.state.dropCompleted = var1;
      }
   }

   public boolean isDropCompleted() {
      return this.state.dropCompleted;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.x = this.sceneX;
      this.y = this.sceneY;
   }

   static {
      ANY = new EventType(InputEvent.ANY, "DRAG");
      DRAG_ENTERED_TARGET = new EventType(ANY, "DRAG_ENTERED_TARGET");
      DRAG_ENTERED = new EventType(DRAG_ENTERED_TARGET, "DRAG_ENTERED");
      DRAG_EXITED_TARGET = new EventType(ANY, "DRAG_EXITED_TARGET");
      DRAG_EXITED = new EventType(DRAG_EXITED_TARGET, "DRAG_EXITED");
      DRAG_OVER = new EventType(ANY, "DRAG_OVER");
      DRAG_DROPPED = new EventType(ANY, "DRAG_DROPPED");
      DRAG_DONE = new EventType(ANY, "DRAG_DONE");
   }

   private static class State {
      boolean accepted;
      boolean dropCompleted;
      TransferMode acceptedTrasferMode;
      Object acceptingObject;

      private State() {
         this.accepted = false;
         this.dropCompleted = false;
         this.acceptedTrasferMode = null;
         this.acceptingObject = null;
      }

      // $FF: synthetic method
      State(Object var1) {
         this();
      }
   }
}
