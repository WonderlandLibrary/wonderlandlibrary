package javafx.animation;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.util.Duration;

public final class TranslateTransition extends Transition {
   private static final double EPSILON = 1.0E-12;
   private double startX;
   private double startY;
   private double startZ;
   private double deltaX;
   private double deltaY;
   private double deltaZ;
   private ObjectProperty node;
   private static final Node DEFAULT_NODE = null;
   private Node cachedNode;
   private ObjectProperty duration;
   private static final Duration DEFAULT_DURATION = Duration.millis(400.0);
   private DoubleProperty fromX;
   private static final double DEFAULT_FROM_X = Double.NaN;
   private DoubleProperty fromY;
   private static final double DEFAULT_FROM_Y = Double.NaN;
   private DoubleProperty fromZ;
   private static final double DEFAULT_FROM_Z = Double.NaN;
   private DoubleProperty toX;
   private static final double DEFAULT_TO_X = Double.NaN;
   private DoubleProperty toY;
   private static final double DEFAULT_TO_Y = Double.NaN;
   private DoubleProperty toZ;
   private static final double DEFAULT_TO_Z = Double.NaN;
   private DoubleProperty byX;
   private static final double DEFAULT_BY_X = 0.0;
   private DoubleProperty byY;
   private static final double DEFAULT_BY_Y = 0.0;
   private DoubleProperty byZ;
   private static final double DEFAULT_BY_Z = 0.0;

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
                  TranslateTransition.this.setCycleDuration(TranslateTransition.this.getDuration());
               } catch (IllegalArgumentException var2) {
                  if (this.isBound()) {
                     this.unbind();
                  }

                  this.set(TranslateTransition.this.getCycleDuration());
                  throw var2;
               }
            }

            public Object getBean() {
               return TranslateTransition.this;
            }

            public String getName() {
               return "duration";
            }
         };
      }

      return this.duration;
   }

   public final void setFromX(double var1) {
      if (this.fromX != null || !Double.isNaN(var1)) {
         this.fromXProperty().set(var1);
      }

   }

   public final double getFromX() {
      return this.fromX == null ? Double.NaN : this.fromX.get();
   }

   public final DoubleProperty fromXProperty() {
      if (this.fromX == null) {
         this.fromX = new SimpleDoubleProperty(this, "fromX", Double.NaN);
      }

      return this.fromX;
   }

   public final void setFromY(double var1) {
      if (this.fromY != null || !Double.isNaN(var1)) {
         this.fromYProperty().set(var1);
      }

   }

   public final double getFromY() {
      return this.fromY == null ? Double.NaN : this.fromY.get();
   }

   public final DoubleProperty fromYProperty() {
      if (this.fromY == null) {
         this.fromY = new SimpleDoubleProperty(this, "fromY", Double.NaN);
      }

      return this.fromY;
   }

   public final void setFromZ(double var1) {
      if (this.fromZ != null || !Double.isNaN(var1)) {
         this.fromZProperty().set(var1);
      }

   }

   public final double getFromZ() {
      return this.fromZ == null ? Double.NaN : this.fromZ.get();
   }

   public final DoubleProperty fromZProperty() {
      if (this.fromZ == null) {
         this.fromZ = new SimpleDoubleProperty(this, "fromZ", Double.NaN);
      }

      return this.fromZ;
   }

   public final void setToX(double var1) {
      if (this.toX != null || !Double.isNaN(var1)) {
         this.toXProperty().set(var1);
      }

   }

   public final double getToX() {
      return this.toX == null ? Double.NaN : this.toX.get();
   }

   public final DoubleProperty toXProperty() {
      if (this.toX == null) {
         this.toX = new SimpleDoubleProperty(this, "toX", Double.NaN);
      }

      return this.toX;
   }

   public final void setToY(double var1) {
      if (this.toY != null || !Double.isNaN(var1)) {
         this.toYProperty().set(var1);
      }

   }

   public final double getToY() {
      return this.toY == null ? Double.NaN : this.toY.get();
   }

   public final DoubleProperty toYProperty() {
      if (this.toY == null) {
         this.toY = new SimpleDoubleProperty(this, "toY", Double.NaN);
      }

      return this.toY;
   }

   public final void setToZ(double var1) {
      if (this.toZ != null || !Double.isNaN(var1)) {
         this.toZProperty().set(var1);
      }

   }

   public final double getToZ() {
      return this.toZ == null ? Double.NaN : this.toZ.get();
   }

   public final DoubleProperty toZProperty() {
      if (this.toZ == null) {
         this.toZ = new SimpleDoubleProperty(this, "toZ", Double.NaN);
      }

      return this.toZ;
   }

   public final void setByX(double var1) {
      if (this.byX != null || Math.abs(var1 - 0.0) > 1.0E-12) {
         this.byXProperty().set(var1);
      }

   }

   public final double getByX() {
      return this.byX == null ? 0.0 : this.byX.get();
   }

   public final DoubleProperty byXProperty() {
      if (this.byX == null) {
         this.byX = new SimpleDoubleProperty(this, "byX", 0.0);
      }

      return this.byX;
   }

   public final void setByY(double var1) {
      if (this.byY != null || Math.abs(var1 - 0.0) > 1.0E-12) {
         this.byYProperty().set(var1);
      }

   }

   public final double getByY() {
      return this.byY == null ? 0.0 : this.byY.get();
   }

   public final DoubleProperty byYProperty() {
      if (this.byY == null) {
         this.byY = new SimpleDoubleProperty(this, "byY", 0.0);
      }

      return this.byY;
   }

   public final void setByZ(double var1) {
      if (this.byZ != null || Math.abs(var1 - 0.0) > 1.0E-12) {
         this.byZProperty().set(var1);
      }

   }

   public final double getByZ() {
      return this.byZ == null ? 0.0 : this.byZ.get();
   }

   public final DoubleProperty byZProperty() {
      if (this.byZ == null) {
         this.byZ = new SimpleDoubleProperty(this, "byZ", 0.0);
      }

      return this.byZ;
   }

   public TranslateTransition(Duration var1, Node var2) {
      this.setDuration(var1);
      this.setNode(var2);
      this.setCycleDuration(var1);
   }

   public TranslateTransition(Duration var1) {
      this(var1, (Node)null);
   }

   public TranslateTransition() {
      this(DEFAULT_DURATION, (Node)null);
   }

   public void interpolate(double var1) {
      if (!Double.isNaN(this.startX)) {
         this.cachedNode.setTranslateX(this.startX + var1 * this.deltaX);
      }

      if (!Double.isNaN(this.startY)) {
         this.cachedNode.setTranslateY(this.startY + var1 * this.deltaY);
      }

      if (!Double.isNaN(this.startZ)) {
         this.cachedNode.setTranslateZ(this.startZ + var1 * this.deltaZ);
      }

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
         double var2 = this.getFromX();
         double var4 = this.getFromY();
         double var6 = this.getFromZ();
         double var8 = this.getToX();
         double var10 = this.getToY();
         double var12 = this.getToZ();
         double var14 = this.getByX();
         double var16 = this.getByY();
         double var18 = this.getByZ();
         if (Double.isNaN(var2) && Double.isNaN(var8) && Math.abs(var14) < 1.0E-12) {
            this.startX = Double.NaN;
         } else {
            this.startX = !Double.isNaN(var2) ? var2 : this.cachedNode.getTranslateX();
            this.deltaX = !Double.isNaN(var8) ? var8 - this.startX : var14;
         }

         if (Double.isNaN(var4) && Double.isNaN(var10) && Math.abs(var16) < 1.0E-12) {
            this.startY = Double.NaN;
         } else {
            this.startY = !Double.isNaN(var4) ? var4 : this.cachedNode.getTranslateY();
            this.deltaY = !Double.isNaN(var10) ? var10 - this.startY : this.getByY();
         }

         if (Double.isNaN(var6) && Double.isNaN(var12) && Math.abs(var18) < 1.0E-12) {
            this.startZ = Double.NaN;
         } else {
            this.startZ = !Double.isNaN(var6) ? var6 : this.cachedNode.getTranslateZ();
            this.deltaZ = !Double.isNaN(var12) ? var12 - this.startZ : this.getByZ();
         }
      }

   }
}
