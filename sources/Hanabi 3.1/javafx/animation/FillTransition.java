package javafx.animation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public final class FillTransition extends Transition {
   private Color start;
   private Color end;
   private ObjectProperty shape;
   private static final Shape DEFAULT_SHAPE = null;
   private Shape cachedShape;
   private ObjectProperty duration;
   private static final Duration DEFAULT_DURATION = Duration.millis(400.0);
   private ObjectProperty fromValue;
   private static final Color DEFAULT_FROM_VALUE = null;
   private ObjectProperty toValue;
   private static final Color DEFAULT_TO_VALUE = null;

   public final void setShape(Shape var1) {
      if (this.shape != null || var1 != null) {
         this.shapeProperty().set(var1);
      }

   }

   public final Shape getShape() {
      return this.shape == null ? DEFAULT_SHAPE : (Shape)this.shape.get();
   }

   public final ObjectProperty shapeProperty() {
      if (this.shape == null) {
         this.shape = new SimpleObjectProperty(this, "shape", DEFAULT_SHAPE);
      }

      return this.shape;
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
                  FillTransition.this.setCycleDuration(FillTransition.this.getDuration());
               } catch (IllegalArgumentException var2) {
                  if (this.isBound()) {
                     this.unbind();
                  }

                  this.set(FillTransition.this.getCycleDuration());
                  throw var2;
               }
            }

            public Object getBean() {
               return FillTransition.this;
            }

            public String getName() {
               return "duration";
            }
         };
      }

      return this.duration;
   }

   public final void setFromValue(Color var1) {
      if (this.fromValue != null || var1 != null) {
         this.fromValueProperty().set(var1);
      }

   }

   public final Color getFromValue() {
      return this.fromValue == null ? DEFAULT_FROM_VALUE : (Color)this.fromValue.get();
   }

   public final ObjectProperty fromValueProperty() {
      if (this.fromValue == null) {
         this.fromValue = new SimpleObjectProperty(this, "fromValue", DEFAULT_FROM_VALUE);
      }

      return this.fromValue;
   }

   public final void setToValue(Color var1) {
      if (this.toValue != null || var1 != null) {
         this.toValueProperty().set(var1);
      }

   }

   public final Color getToValue() {
      return this.toValue == null ? DEFAULT_TO_VALUE : (Color)this.toValue.get();
   }

   public final ObjectProperty toValueProperty() {
      if (this.toValue == null) {
         this.toValue = new SimpleObjectProperty(this, "toValue", DEFAULT_TO_VALUE);
      }

      return this.toValue;
   }

   public FillTransition(Duration var1, Shape var2, Color var3, Color var4) {
      this.setDuration(var1);
      this.setShape(var2);
      this.setFromValue(var3);
      this.setToValue(var4);
      this.setCycleDuration(var1);
   }

   public FillTransition(Duration var1, Color var2, Color var3) {
      this(var1, (Shape)null, var2, var3);
   }

   public FillTransition(Duration var1, Shape var2) {
      this(var1, var2, (Color)null, (Color)null);
   }

   public FillTransition(Duration var1) {
      this(var1, (Shape)null, (Color)null, (Color)null);
   }

   public FillTransition() {
      this(DEFAULT_DURATION, (Shape)null);
   }

   protected void interpolate(double var1) {
      Color var3 = this.start.interpolate(this.end, var1);
      this.cachedShape.setFill(var3);
   }

   private Shape getTargetShape() {
      Shape var1 = this.getShape();
      if (var1 == null) {
         Node var2 = this.getParentTargetNode();
         if (var2 instanceof Shape) {
            var1 = (Shape)var2;
         }
      }

      return var1;
   }

   boolean impl_startable(boolean var1) {
      if (!super.impl_startable(var1)) {
         return false;
      } else if (!var1 && this.cachedShape != null) {
         return true;
      } else {
         Shape var2 = this.getTargetShape();
         return var2 != null && (this.getFromValue() != null || var2.getFill() instanceof Color) && this.getToValue() != null;
      }
   }

   void impl_sync(boolean var1) {
      super.impl_sync(var1);
      if (var1 || this.cachedShape == null) {
         this.cachedShape = this.getTargetShape();
         Color var2 = this.getFromValue();
         this.start = var2 != null ? var2 : (Color)this.cachedShape.getFill();
         this.end = this.getToValue();
      }

   }
}
