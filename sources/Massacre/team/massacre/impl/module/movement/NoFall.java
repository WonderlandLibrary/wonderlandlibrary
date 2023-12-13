package team.massacre.impl.module.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventUpdate;
import team.massacre.utils.PacketUtil;

public class NoFall extends Module {
   public EnumProperty<NoFall.Mode> mode;

   public NoFall() {
      super("No Fall", 0, Category.PLAYER);
      this.mode = new EnumProperty("Mode", NoFall.Mode.Ground);
      this.addValues(new Property[]{this.mode});
   }

   @Handler
   public void onUpdate(EventUpdate eventUpdate) {
      this.setSuffix(((NoFall.Mode)this.mode.getValue()).name());
      if (this.isBlockUnder()) {
         switch((NoFall.Mode)this.mode.getValue()) {
         case Packet:
            if ((double)this.mc.thePlayer.fallDistance > 2.5D) {
               this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
            break;
         case Ground:
            if ((double)this.mc.thePlayer.fallDistance > 2.5D) {
               eventUpdate.setOnGround(true);
            }
            break;
         case Gaypuppy:
            if (this.mc.thePlayer.fallDistance >= 3.0F) {
               PacketUtil.sendPacketNoEvent(new C03PacketPlayer(true));
               this.mc.thePlayer.fallDistance = 0.0F;
            }
         }
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

   public static enum Mode {
      Ground,
      Packet,
      Gaypuppy;
   }
}
