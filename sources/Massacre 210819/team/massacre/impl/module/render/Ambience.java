package team.massacre.impl.module.render;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.impl.event.EventPacket;
import team.massacre.impl.event.EventUpdate;

public class Ambience extends Module {
   public DoubleProperty time = new DoubleProperty("Time", 12000.0D, 0.0D, 24000.0D, 1000.0D);

   public Ambience() {
      super("Ambience", 0, Category.RENDER);
      this.addValues(new Property[]{this.time});
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      this.setSuffix(((Double)this.time.getValue()).toString());
      this.mc.theWorld.setWorldTime(((Double)this.time.getValue()).longValue());
   }

   @Handler
   public void onPacket(EventPacket eventPacket) {
      if (eventPacket.getPacket() instanceof S03PacketTimeUpdate) {
         eventPacket.setCancelled(true);
      }

   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }
}
