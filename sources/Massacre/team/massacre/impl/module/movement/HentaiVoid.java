package team.massacre.impl.module.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.impl.event.EventUpdate;
import team.massacre.utils.PacketUtil;

public class HentaiVoid extends Module {
   public DoubleProperty fallDistance = new DoubleProperty("Fall Distance", 3.0D, 1.0D, 10.0D, 1.0D);

   public HentaiVoid() {
      super("Hentai Void", 0, Category.MOVEMENT);
      this.addValues(new Property[]{this.fallDistance});
   }

   @Handler
   public void a(EventUpdate event) {
      if (!this.isBlockUnder() && this.mc.thePlayer.fallDistance >= (float)((Double)this.fallDistance.getValue()).intValue()) {
         PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(event.getPosX(), event.getPosY() + 11.0D * StrictMath.random(), event.getPosZ(), event.getYaw(), event.getPitch(), false));
         this.mc.thePlayer.fallDistance = 0.0F;
      }

   }

   private boolean isBlockUnder() {
      if (this.mc.thePlayer.posY < 0.0D) {
         return false;
      } else {
         for(int offset = 0; offset < (int)this.mc.thePlayer.posY + 2; offset += 2) {
            AxisAlignedBB bb = this.mc.thePlayer.getEntityBoundingBox().offset(0.0D, (double)(-offset), 0.0D);
            if (!this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, bb).isEmpty()) {
               return true;
            }
         }

         return false;
      }
   }
}
