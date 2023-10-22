package javafx.scene.effect;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import javafx.beans.value.ObservableValue;

abstract class EffectChangeListener extends AbstractNotifyListener {
   protected ObservableValue registredOn;

   public void register(ObservableValue var1) {
      if (this.registredOn != var1) {
         if (this.registredOn != null) {
            this.registredOn.removeListener(this.getWeakListener());
         }

         this.registredOn = var1;
         if (this.registredOn != null) {
            this.registredOn.addListener(this.getWeakListener());
         }

      }
   }
}
