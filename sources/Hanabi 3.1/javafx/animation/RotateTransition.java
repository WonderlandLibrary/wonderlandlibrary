package javafx.animation;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.util.Duration;

public final class RotateTransition extends Transition {
   private static final double EPSILON = 1.0E-12;
   private double start;
   private double delta;
   private ObjectProperty node;
   private static final Node DEFAULT_NODE = null;
   private Node cachedNode;
   private ObjectProperty duration;
   private static final Duration DEFAULT_DURATION = Duration.millis(400.0);
   private ObjectProperty axis;
   private static final Point3D DEFAULT_AXIS = null;
   private DoubleProperty fromAngle;
   private static final double DEFAULT_FROM_ANGLE = Double.NaN;
   private DoubleProperty toAngle;
   private static final double DEFAULT_TO_ANGLE = Double.NaN;
   private DoubleProperty byAngle;
   private static final double DEFAULT_BY_ANGLE = 0.0;

   public final void setNode(Node var1) {
      if (this.node != null || var1 != null) {
         this.nodeProperty().set(var1);
      }

   }

   public final Node getNode() {
      return this.node == null ? DEFAULT_NODE : (Node)this.node.get();
   }

   public final ObjectProperty nodeProperty() {
      if (this.node == null) {
         this.node = new SimpleObjectProperty(this, "node", DEFAULT_NODE);
      }

      return this.node;
   }

   public final void setDuration(Duration var1) {
      if (this.duration != null || !DEFAULT_DURATION.equals(var1)) {
         this.durationProperty().set(var1);
      }

   }

   public final Duration getDuration() {
      return this.duration == null ? DEFAULT_DURATION : (Duration)this.duration.get();
   }

   public final ObjectProperty durationProperty() {
      if (this.duration == null) {
         this.duration = new ObjectPropertyBase(DEFAULT_DURATION) {
            public void invalidated() {
               try {
                  RotateTransition.this.setCycleDuration(RotateTransition.this.getDuration());
               } catch (IllegalArgumentException var2) {
                  if (this.isBound()) {
                     this.unbind();
                  }

                  this.set(RotateTransition.this.getCycleDuration());
                  throw var2;
               }
            }

            public Object getBean() {
               return RotateTransition.this;
            }

            public String getName() {
               return "duration";
            }
         };
      }

      return this.duration;
   }

   public final void setAxis(Point3D var1) {
      if (this.axis != null || var1 != null) {
         this.axisProperty().set(var1);
      }

   }

   public final Point3D getAxis() {
      return this.axis == null ? DEFAULT_AXIS : (Point3D)this.axis.get();
   }

   public final ObjectProperty axisProperty() {
      if (this.axis == null) {
         this.axis = new SimpleObjectProperty(this, "axis", DEFAULT_AXIS);
      }

      return this.axis;
   }

   public final void setFromAngle(double var1) {
      if (this.fromAngle != null || !Double.isNaN(var1)) {
         this.fromAngleProperty().set(var1);
      }

   }

   public final double getFromAngle() {
      return this.fromAngle == null ? Double.NaN : this.fromAngle.get();
   }

   public final DoubleProperty fromAngleProperty() {
      if (this.fromAngle == null) {
         this.fromAngle = new SimpleDoubleProperty(this, "fromAngle", Double.NaN);
      }

      return this.fromAngle;
   }

   public final void setToAngle(double var1) {
      if (this.toAngle != null || !Double.isNaN(var1)) {
         this.toAngleProperty().set(var1);
      }

   }

   public final double getToAngle() {
      return this.toAngle == null ? Double.NaN : this.toAngle.get();
   }

   public final DoubleProperty toAngleProperty() {
      if (this.toAngle == null) {
         this.toAngle = new SimpleDoubleProperty(this, "toAngle", Double.NaN);
      }

      return this.toAngle;
   }

   public final void setByAngle(double var1) {
      if (this.byAngle != null || Math.abs(var1 - 0.0) > 1.0E-12) {
         this.byAngleProperty().set(var1);
      }

   }

   public final double getByAngle() {
      return this.byAngle == null ? 0.0 : this.byAngle.get();
   }

   public final DoubleProperty byAngleProperty() {
      if (this.byAngle == null) {
         this.byAngle = new SimpleDoubleProperty(this, "byAngle", 0.0);
      }

      return this.byAngle;
   }

   public RotateTransition(Duration var1, Node var2) {
      this.setDuration(var1);
      this.setNode(var2);
      this.setCycleDuration(var1);
   }

   public RotateTransition(Duration var1) {
      this(var1, (Node)null);
   }

   public RotateTransition() {
      this(DEFAULT_DURATION, (Node)null);
   }

   protected void interpolate(double var1) {
      this.cachedNode.setRotate(this.start + var1 * this.delta);
   }

   private Node getTargetNode() {
      Node var1 = this.getNode();
      return var1 != null ? var1 : this.getParentTargetNode();
   }

   boolean impl_startable(boolean var1) {
      return super.impl_startable(var1) && (this.getTargetNode() != null || !var1 && this.cachedNode != null);
   }

   void impl_sync(boolean var1) {
      super.impl_sync(var1);
      if (var1 || this.cachedNode == null) {
         this.cachedNode = this.getTargetNode();
         double var2 = this.getFromAngle();
         double var4 = this.getToAngle();
         this.start = !Double.isNaN(var2) ? var2 : this.cachedNode.getRotate();
         this.delta = !Double.isNaN(var4) ? var4 - this.start : this.getByAngle();
         Point3D var6 = this.getAxis();
         if (var6 != null) {
            ((Node)this.node.get()).setRotationAxis(var6);
         }
      }

   }
}
