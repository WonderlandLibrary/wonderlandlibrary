package org.alphacentauri.modules;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventEntityHealthUpdate;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender3D;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.modules.ModuleDamageIndicator.DamageParticle;

public class ModuleDamageIndicator extends Module implements EventListener {
   private ArrayList particles = new ArrayList();

   public ModuleDamageIndicator() {
      super("DamageIndicator", "Show\'s other player\'s health", new String[]{"dmgind", "dmgindicator"}, Module.Category.Render, 223439);
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender3D) {
         float partialTicks = ((EventRender3D)event).getPartialTicks();
         this.particles.forEach((particles) -> {
            particles.render(partialTicks);
         });
      } else if(event instanceof EventTick) {
         this.particles.forEach(DamageParticle::update);
         this.particles = (ArrayList)this.particles.stream().filter((particles) -> {
            return DamageParticle.access$000(particles) <= 10;
         }).collect(Collectors.toCollection(ArrayList::<init>));
      } else if(event instanceof EventEntityHealthUpdate) {
         EntityLivingBase entity = ((EventEntityHealthUpdate)event).getEntity();
         if(!(entity instanceof EntityPlayerSP)) {
            this.particles.add(new DamageParticle(this, entity.posX, entity.posY + 1.0D, entity.posZ, ((EventEntityHealthUpdate)event).getHealthDiffrence()));
         }
      }

   }
}
