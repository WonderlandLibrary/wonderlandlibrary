package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.events.EventVelocity;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.MathUtils;

public class ModuleVelocity extends Module implements EventListener {
   private Property hmod = new Property(this, "Horizontal", Double.valueOf(0.0D));
   private Property vmod = new Property(this, "Vertical", Double.valueOf(0.0D));

   public ModuleVelocity() {
      super("Velocity", "Get less/more knockback", new String[]{"velocity", "antiknockback"}, Module.Category.Combat, 5849728);
   }

   public void onEvent(Event event) {
      if(event instanceof EventVelocity) {
         EventVelocity velocity = (EventVelocity)event;
         Entity entity = ((EventVelocity)event).getEntity();
         if(velocity.getEntity() instanceof EntityPlayerSP) {
            if(((Double)this.hmod.value).doubleValue() == 0.0D) {
               velocity.setX(entity.motionX);
               velocity.setZ(entity.motionZ);
            } else {
               velocity.setX(velocity.getX() * ((Double)this.hmod.value).doubleValue());
               velocity.setZ(velocity.getZ() * ((Double)this.hmod.value).doubleValue());
            }

            if(((Double)this.vmod.value).doubleValue() == 0.0D) {
               velocity.setY(entity.motionY);
            } else {
               velocity.setY(velocity.getY() * ((Double)this.vmod.value).doubleValue());
            }
         }
      } else if(event instanceof EventTick) {
         this.setTag("h" + MathUtils.round(((Double)this.hmod.value).doubleValue() * 100.0D, 1) + "% v" + MathUtils.round(((Double)this.vmod.value).doubleValue() * 100.0D, 1) + "%");
      }

   }
}
