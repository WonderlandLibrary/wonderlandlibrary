package org.alphacentauri.modules;

import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.modules.speed.Speed;
import org.alphacentauri.modules.speed.SpeedBHopAAC1;
import org.alphacentauri.modules.speed.SpeedBHopAAC2;
import org.alphacentauri.modules.speed.SpeedBHopGomme1;
import org.alphacentauri.modules.speed.SpeedBHopNCP1;
import org.alphacentauri.modules.speed.SpeedBHopSpartan1;
import org.alphacentauri.modules.speed.SpeedGroundGomme1;
import org.alphacentauri.modules.speed.SpeedMiniHopAAC1;
import org.alphacentauri.modules.speed.SpeedMiniHopNCP1;
import org.alphacentauri.modules.speed.SpeedY1NCP;
import org.alphacentauri.modules.speed.SpeedY2NCP;
import org.alphacentauri.modules.speed.SpeedYAAC1;
import org.alphacentauri.modules.speed.SpeedYClashMC1;

public class ModuleSpeed extends Module implements EventListener {
   private Property mode = new Property(this, "Mode", ModuleSpeed.Mode.BHOPNCP1);
   private Speed current = null;

   public ModuleSpeed() {
      super("Speed", "Go faster", new String[]{"speed"}, Module.Category.Movement, '\uffff');
   }

   public void onEvent(Event event) {
      if(this.mode.value == ModuleSpeed.Mode.BHOPNCP1) {
         if(!(this.current instanceof SpeedBHopNCP1)) {
            this.current = new SpeedBHopNCP1();
         }
      } else if(this.mode.value == ModuleSpeed.Mode.YAAC1) {
         if(!(this.current instanceof SpeedYAAC1)) {
            this.current = new SpeedYAAC1();
         }
      } else if(this.mode.value == ModuleSpeed.Mode.YNCP1) {
         if(!(this.current instanceof SpeedY1NCP)) {
            this.current = new SpeedY1NCP();
         }
      } else if(this.mode.value == ModuleSpeed.Mode.YNCP2) {
         if(!(this.current instanceof SpeedY2NCP)) {
            this.current = new SpeedY2NCP();
         }
      } else if(this.mode.value == ModuleSpeed.Mode.BHOPAAC2) {
         if(!(this.current instanceof SpeedBHopAAC2)) {
            this.current = new SpeedBHopAAC2();
         }
      } else if(this.mode.value == ModuleSpeed.Mode.BHOPAAC1) {
         if(!(this.current instanceof SpeedBHopAAC1)) {
            this.current = new SpeedBHopAAC1();
         }
      } else if(this.mode.value == ModuleSpeed.Mode.BHOPSpartan1) {
         if(!(this.current instanceof SpeedBHopSpartan1)) {
            this.current = new SpeedBHopSpartan1();
         }
      } else if(this.mode.value == ModuleSpeed.Mode.BHOPGomme1) {
         if(!(this.current instanceof SpeedBHopGomme1)) {
            this.current = new SpeedBHopGomme1();
         }
      } else if(this.mode.value == ModuleSpeed.Mode.YClashMC1) {
         if(!(this.current instanceof SpeedYClashMC1)) {
            this.current = new SpeedYClashMC1();
         }
      } else if(this.mode.value == ModuleSpeed.Mode.MiniHopNCP1) {
         if(!(this.current instanceof SpeedMiniHopNCP1)) {
            this.current = new SpeedMiniHopNCP1();
         }
      } else if(this.mode.value == ModuleSpeed.Mode.MiniHopAAC1) {
         if(!(this.current instanceof SpeedMiniHopAAC1)) {
            this.current = new SpeedMiniHopAAC1();
         }
      } else if(this.mode.value == ModuleSpeed.Mode.GroundGomme1 && !(this.current instanceof SpeedGroundGomme1)) {
         this.current = new SpeedGroundGomme1();
      }

      if(event instanceof EventTick) {
         this.setTag(((ModuleSpeed.Mode)this.mode.value).name());
      }

      if(this.current != null) {
         this.current.onEvent(event);
      }

   }

   public static enum Mode {
      BHOPNCP1,
      YAAC1,
      BHOPAAC1,
      BHOPAAC2,
      YNCP1,
      YNCP2,
      BHOPSpartan1,
      BHOPGomme1,
      YClashMC1,
      MiniHopNCP1,
      MiniHopAAC1,
      GroundGomme1;
   }
}
