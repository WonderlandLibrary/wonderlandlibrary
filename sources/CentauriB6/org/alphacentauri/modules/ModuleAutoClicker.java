package org.alphacentauri.modules;

import java.util.Objects;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.Timer;

public class ModuleAutoClicker extends Module implements EventListener {
   private Property minCPS = new Property(this, "MinimalCPS", Integer.valueOf(9));
   private Property maxCPS = new Property(this, "MaximalCPS", Integer.valueOf(12));
   private Timer timer = new Timer();

   public ModuleAutoClicker() {
      super("AutoClicker", "Clicks automatically at set speed", new String[]{"autoclicker"}, Module.Category.Combat, 2898216);
   }

   public void setEnabledSilent(boolean enabled) {
      this.timer.reset();
      super.setEnabledSilent(enabled);
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         if(((Integer)this.minCPS.value).intValue() > ((Integer)this.maxCPS.value).intValue()) {
            this.minCPS.value = this.maxCPS.value;
         }

         int cps;
         if(Objects.equals(this.maxCPS.value, this.minCPS.value)) {
            cps = ((Integer)this.maxCPS.value).intValue();
         } else {
            cps = AC.getRandom().nextInt(((Integer)this.maxCPS.value).intValue() - ((Integer)this.minCPS.value).intValue()) + ((Integer)this.minCPS.value).intValue();
         }

         if(cps > 999) {
            cps = 999;
         }

         int ms = 1000 / cps;

         while(this.timer.hasMSPassed((long)ms)) {
            AC.getMC().clickMouse();
            this.timer.subtract(ms);
         }
      }

   }
}
