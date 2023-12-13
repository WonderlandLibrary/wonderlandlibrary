package javafx.animation;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.util.Duration;

public final class FadeTransition extends Transition {
   private static final double EPSILON = 1.0E-12;
   private double start;
   private double delta;
   private ObjectProperty node;
   private static final Node DEFAULT_NODE = null;
   private Node cachedNode;
   private ObjectProperty duration;
   private static final Duration DEFAULT_DURATION = Duration.millis(400.0);
   private DoubleProperty fromValue;
   private static final double DEFAULT_FROM_VALUE = Double.NaN;
   private DoubleProperty toValue;
   private static final double DEFAULT_TO_VALUE = Double.NaN;
   private DoubleProperty byValue;
   private static final double DEFAULT_BY_VALUE = 0.0;

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
                  FadeTransition.this.setCycleDuration(FadeTransition.this.getDuration());
               } catch (IllegalArgumentException var2) {
                  if (this.isBound()) {
                     this.unbind();
                  }

                  this.set(FadeTransition.this.getCycleDuration());
                  throw var2;
               }
            }

            public Object getBean() {
               return FadeTransition.this;
            }

            public String getName() {
               return "duration";
            }
         };
      }

      return this.duration;
   }

   public final void setFromValue(double var1) {
      if (this.fromValue != null || !Double.isNaN(var1)) {
         this.fromValueProperty().set(var1);
      }

   }

   public final double getFromValue() {
      return this.fromValue == null ? Double.NaN : this.fromValue.get();
   }

   public final DoubleProperty fromValueProperty() {
      if (this.fromValue == null) {
         this.fromValue = new SimpleDoubleProperty(this, "fromValue", Double.NaN);
      }

      return this.fromValue;
   }

   public final void setToValue(double var1) {
      if (this.toValue != null || !Double.isNaN(var1)) {
         this.toValueProperty().set(var1);
      }

   }

   public final double getToValue() {
      return this.toValue == null ? Double.NaN : this.toValue.get();
   }

   public final DoubleProperty toValueProperty() {
      if (this.toValue == null) {
         this.toValue = new SimpleDoubleProperty(this, "toValue", Double.NaN);
      }

      return this.toValue;
   }

   public final void setByValue(double var1) {
      if (this.byValue != null || Math.abs(var1 - 0.0) > 1.0E-12) {
         this.byValueProperty().set(var1);
      }

   }

   public final double getByValue() {
      return this.byValue == null ? 0.0 : this.byValue.get();
   }

   public final DoubleProperty byValueProperty() {
      if (this.byValue == null) {
         this.byValue = new SimpleDoubleProperty(this, "byValue", 0.0);
      }

      return this.byValue;
   }

   public FadeTransition(Duration var1, Node var2) {
      this.setDuration(var1);
      this.setNode(var2);
      this.setCycleDuration(var1);
   }

   public FadeTransition(Duration var1) {
      this(var1, (Node)null);
   }

   public FadeTransition() {
      this(DEFAULT_DURATION, (Node)null);
   }

   protected void interpolate(double var1) {
      double var3 = Math.max(0.0, Math.min(this.start + var1 * this.delta, 1.0));
      this.cachedNode.setOpacity(var3);
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
         double var2 = this.getFromValue();
         double var4 = this.getToValue();
         this.start = !Double.isNaN(var2) ? Math.max(0.0, Math.min(var2, 1.0)) : this.cachedNode.getOpacity();
         this.delta = !Double.isNaN(var4) ? var4 - this.start : this.getByValue();
         if (this.start + this.delta > 1.0) {
            this.delta = 1.0 - this.start;
         } else if (this.start + this.delta < 0.0) {
            this.delta = -this.start;
         }
      }

   }
}
