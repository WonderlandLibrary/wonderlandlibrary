package team.massacre.impl.module.combat;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import team.massacre.Massacre;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.Representation;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventPacket;
import team.massacre.impl.event.EventUpdate;

public class Velocity extends Module {
   public EnumProperty<Velocity.Mode> mode;
   public DoubleProperty vMotion;
   public DoubleProperty hMotion;

   public Velocity() {
      super("Velocity", 0, Category.COMBAT);
      this.mode = new EnumProperty("Mode", Velocity.Mode.Motion);
      this.vMotion = new DoubleProperty("V-Motion", 100.0D, 0.0D, 100.0D, 5.0D, Representation.DOUBLE);
      this.hMotion = new DoubleProperty("H-Motion", 100.0D, 0.0D, 100.0D, 5.0D, Representation.DOUBLE);
      this.addValues(new Property[]{this.mode, this.vMotion, this.hMotion});
   }

   @Handler
   public void onEvent(EventPacket eventReceivePacket) {
      switch((Velocity.Mode)this.mode.getValue()) {
      case Cancel:
         if (eventReceivePacket.getPacket() instanceof S27PacketExplosion) {
            eventReceivePacket.setCancelled(true);
         }

         if (eventReceivePacket.getPacket() instanceof S12PacketEntityVelocity) {
            eventReceivePacket.setCancelled(true);
         }
      default:
      }
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      this.setSuffix(((Velocity.Mode)this.mode.getValue()).name());
      switch((Velocity.Mode)this.mode.getValue()) {
      case Motion:
         double horizontalMotion = (Double)this.hMotion.getValue() / 100.0D;
         double verticalMotion = (Double)this.vMotion.getValue() / 100.0D;
         if (this.mc.thePlayer.hurtTime > 0 && this.mc.thePlayer.hurtTime <= 6 && !Massacre.INSTANCE.getModuleManager().getModule("Speed").getState()) {
            EntityPlayerSP var10000 = this.mc.thePlayer;
            var10000.motionX *= horizontalMotion;
            var10000 = this.mc.thePlayer;
            var10000.motionZ *= horizontalMotion;
         }
      case Test:
      default:
      }
   }

   public static enum Mode {
      Motion,
      Cancel,
      Test;
   }
}
