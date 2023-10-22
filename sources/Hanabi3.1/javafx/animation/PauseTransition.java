package javafx.animation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.util.Duration;

public final class PauseTransition extends Transition {
   private ObjectProperty duration;
   private static final Duration DEFAULT_DURATION = Duration.millis(400.0);

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
                  PauseTransition.this.setCycleDuration(PauseTransition.this.getDuration());
               } catch (IllegalArgumentException var2) {
                  if (this.isBound()) {
                     this.unbind();
                  }

                  this.set(PauseTransition.this.getCycleDuration());
                  throw var2;
               }
            }

            public Object getBean() {
               return PauseTransition.this;
            }

            public String getName() {
               return "duration";
            }
         };
      }

      return this.duration;
   }

   public PauseTransition(Duration var1) {
      this.setDuration(var1);
      this.setCycleDuration(var1);
   }

   public PauseTransition() {
      this(DEFAULT_DURATION);
   }

   public void interpolate(double var1) {
   }
}
