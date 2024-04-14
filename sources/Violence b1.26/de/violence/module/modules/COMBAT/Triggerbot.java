package de.violence.module.modules.COMBAT;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.ui.Timings;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;

public class Triggerbot extends Module {
   private VSetting bRaycast = new VSetting("Raycast", this, false);
   private VSetting sCPS = new VSetting("CPS", this, 1.0D, 20.0D, 1.0D, true);
   private Timings delayTimings = new Timings();

   public Triggerbot() {
      super("Triggerbot", Category.COMBAT);
   }

   public void onUpdate() {
      this.nameAddon = "CPS: " + this.sCPS.getCurrent();
      if(this.mc.objectMouseOver != null) {
         Entity onPoint = this.mc.objectMouseOver.entityHit;
         if(onPoint != null || onPoint != null && !onPoint.canAttackWithItem()) {
            boolean ray = false;
            if(this.bRaycast.isToggled() && !this.findRaycast(onPoint).isEmpty()) {
               onPoint = (Entity)this.findRaycast(onPoint).get(0);
               ray = true;
            }

            if(!ray && this.delayTimings.hasReached(this.CPStoDelay())) {
               this.hitEntity(onPoint);
            }
         }
      }

      super.onUpdate();
   }

   private void hitEntity(Entity e) {
      this.mc.clickMouse();
      this.delayTimings.resetTimings();
   }

   private List findRaycast(Entity e) {
      ArrayList arrayList = new ArrayList();
      Iterator var4 = this.mc.theWorld.loadedEntityList.iterator();

      while(var4.hasNext()) {
         Entity rs = (Entity)var4.next();
         if((double)rs.getDistanceToEntity(e) <= 0.5D && rs.isInvisible()) {
            arrayList.add(rs);
         }
      }

      return arrayList;
   }

   private long CPStoDelay() {
      return (long)(this.sCPS.getMax() / this.sCPS.getCurrent() * 40.0D);
   }
}
