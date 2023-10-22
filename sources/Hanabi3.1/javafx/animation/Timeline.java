package javafx.animation;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import com.sun.scenario.animation.shared.TimelineClipCore;
import java.util.Iterator;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Duration;

public final class Timeline extends Animation {
   final TimelineClipCore clipCore = new TimelineClipCore(this);
   private final ObservableList keyFrames = new TrackableObservableList() {
      protected void onChanged(ListChangeListener.Change var1) {
         while(var1.next()) {
            if (!var1.wasPermutated()) {
               Iterator var2 = var1.getRemoved().iterator();

               KeyFrame var3;
               String var4;
               while(var2.hasNext()) {
                  var3 = (KeyFrame)var2.next();
                  var4 = var3.getName();
                  if (var4 != null) {
                     Timeline.this.getCuePoints().remove(var4);
                  }
               }

               var2 = var1.getAddedSubList().iterator();

               while(var2.hasNext()) {
                  var3 = (KeyFrame)var2.next();
                  var4 = var3.getName();
                  if (var4 != null) {
                     Timeline.this.getCuePoints().put(var4, var3.getTime());
                  }
               }

               Duration var5 = Timeline.this.clipCore.setKeyFrames(Timeline.this.getKeyFrames());
               Timeline.this.setCycleDuration(var5);
            }
         }

      }
   };

   public final ObservableList getKeyFrames() {
      return this.keyFrames;
   }

   public Timeline(double var1, KeyFrame... var3) {
      super(var1);
      this.getKeyFrames().setAll((Object[])var3);
   }

   public Timeline(KeyFrame... var1) {
      this.getKeyFrames().setAll((Object[])var1);
   }

   public Timeline(double var1) {
      super(var1);
   }

   public Timeline() {
   }

   Timeline(AbstractPrimaryTimer var1) {
      super(var1);
   }

   void impl_playTo(long var1, long var3) {
      this.clipCore.playTo(var1);
   }

   void impl_jumpTo(long var1, long var3, boolean var5) {
      this.impl_sync(false);
      this.impl_setCurrentTicks(var1);
      this.clipCore.jumpTo(var1, var5);
   }

   void impl_setCurrentRate(double var1) {
      super.impl_setCurrentRate(var1);
      this.clipCore.notifyCurrentRateChanged();
   }

   void impl_start(boolean var1) {
      super.impl_start(var1);
      this.clipCore.start(var1);
   }

   public void stop() {
      if (this.parent != null) {
         throw new IllegalStateException("Cannot stop when embedded in another animation");
      } else {
         if (this.getStatus() == Animation.Status.RUNNING) {
            this.clipCore.abort();
         }

         super.stop();
      }
   }
}
