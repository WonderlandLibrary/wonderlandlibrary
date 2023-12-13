package javafx.animation;

import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import com.sun.scenario.animation.shared.TimerReceiver;
import java.security.AccessControlContext;
import java.security.AccessController;

public abstract class AnimationTimer {
   private final AbstractPrimaryTimer timer;
   private final AnimationTimerReceiver timerReceiver = new AnimationTimerReceiver();
   private boolean active;
   private AccessControlContext accessCtrlCtx = null;

   public AnimationTimer() {
      this.timer = Toolkit.getToolkit().getPrimaryTimer();
   }

   AnimationTimer(AbstractPrimaryTimer var1) {
      this.timer = var1;
   }

   public abstract void handle(long var1);

   public void start() {
      if (!this.active) {
         this.accessCtrlCtx = AccessController.getContext();
         this.timer.addAnimationTimer(this.timerReceiver);
         this.active = true;
      }

   }

   public void stop() {
      if (this.active) {
         this.timer.removeAnimationTimer(this.timerReceiver);
         this.active = false;
      }

   }

   private class AnimationTimerReceiver implements TimerReceiver {
      private AnimationTimerReceiver() {
      }

      public void handle(long var1) {
         if (AnimationTimer.this.accessCtrlCtx == null) {
            throw new IllegalStateException("Error: AccessControlContext not captured");
         } else {
            AccessController.doPrivileged(() -> {
               AnimationTimer.this.handle(var1);
               return null;
            }, AnimationTimer.this.accessCtrlCtx);
         }
      }

      // $FF: synthetic method
      AnimationTimerReceiver(Object var2) {
         this();
      }
   }
}
