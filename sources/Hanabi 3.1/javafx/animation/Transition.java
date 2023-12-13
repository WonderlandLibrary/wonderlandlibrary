package javafx.animation;

import com.sun.scenario.animation.AbstractPrimaryTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public abstract class Transition extends Animation {
   private ObjectProperty interpolator;
   private static final Interpolator DEFAULT_INTERPOLATOR;
   private Interpolator cachedInterpolator;

   public final void setInterpolator(Interpolator var1) {
      if (this.interpolator != null || !DEFAULT_INTERPOLATOR.equals(var1)) {
         this.interpolatorProperty().set(var1);
      }

   }

   public final Interpolator getInterpolator() {
      return this.interpolator == null ? DEFAULT_INTERPOLATOR : (Interpolator)this.interpolator.get();
   }

   public final ObjectProperty interpolatorProperty() {
      if (this.interpolator == null) {
         this.interpolator = new SimpleObjectProperty(this, "interpolator", DEFAULT_INTERPOLATOR);
      }

      return this.interpolator;
   }

   protected Interpolator getCachedInterpolator() {
      return this.cachedInterpolator;
   }

   public Transition(double var1) {
      super(var1);
   }

   public Transition() {
   }

   Transition(AbstractPrimaryTimer var1) {
      super(var1);
   }

   protected Node getParentTargetNode() {
      return this.parent != null && this.parent instanceof Transition ? ((Transition)this.parent).getParentTargetNode() : null;
   }

   protected abstract void interpolate(double var1);

   private double calculateFraction(long var1, long var3) {
      double var5 = var3 <= 0L ? 1.0 : (double)var1 / (double)var3;
      return this.cachedInterpolator.interpolate(0.0, 1.0, var5);
   }

   boolean impl_startable(boolean var1) {
      return super.impl_startable(var1) && (this.getInterpolator() != null || !var1 && this.cachedInterpolator != null);
   }

   void impl_sync(boolean var1) {
      super.impl_sync(var1);
      if (var1 || this.cachedInterpolator == null) {
         this.cachedInterpolator = this.getInterpolator();
      }

   }

   void impl_playTo(long var1, long var3) {
      this.impl_setCurrentTicks(var1);
      this.interpolate(this.calculateFraction(var1, var3));
   }

   void impl_jumpTo(long var1, long var3, boolean var5) {
      this.impl_setCurrentTicks(var1);
      if (this.getStatus() != Animation.Status.STOPPED || var5) {
         this.impl_sync(false);
         this.interpolate(this.calculateFraction(var1, var3));
      }

   }

   static {
      DEFAULT_INTERPOLATOR = Interpolator.EASE_BOTH;
   }
}
