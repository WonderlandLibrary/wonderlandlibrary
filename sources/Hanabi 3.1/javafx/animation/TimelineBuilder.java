package javafx.animation;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class TimelineBuilder extends AnimationBuilder implements Builder {
   private boolean __set;
   private Collection keyFrames;
   private double targetFramerate;

   protected TimelineBuilder() {
   }

   public static TimelineBuilder create() {
      return new TimelineBuilder();
   }

   public void applyTo(Timeline var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.getKeyFrames().addAll(this.keyFrames);
      }

   }

   public TimelineBuilder keyFrames(Collection var1) {
      this.keyFrames = var1;
      this.__set = true;
      return this;
   }

   public TimelineBuilder keyFrames(KeyFrame... var1) {
      return this.keyFrames((Collection)Arrays.asList(var1));
   }

   public TimelineBuilder targetFramerate(double var1) {
      this.targetFramerate = var1;
      return this;
   }

   public Timeline build() {
      Timeline var1 = new Timeline(this.targetFramerate);
      this.applyTo(var1);
      return var1;
   }
}
