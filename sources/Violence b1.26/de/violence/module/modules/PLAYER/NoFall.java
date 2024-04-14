package de.violence.module.modules.PLAYER;

import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.ui.Line3D;
import de.violence.ui.Location3D;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class NoFall extends Module {
   public VSetting mode = new VSetting("Mode", this, Arrays.asList(new String[]{"AAC 3.3.2", "AAC 3.3.5"}), "AAC 3.3.2");
   double startY = 0.0D;

   public NoFall() {
      super("NoFall", Category.PLAYER);
   }

   public void onUpdate() {
      this.nameAddon = this.mode.getActiveMode();
      if(this.mode.getActiveMode().equalsIgnoreCase("AAC 3.3.5")) {
         if(this.mc.thePlayer.isInLava() || this.mc.thePlayer.isInWater()) {
            return;
         }

         if(this.mc.thePlayer.onGround && this.mc.thePlayer.motionY < 0.0D) {
            this.mc.thePlayer.motionY = -9.899999618530273D;
            return;
         }
      } else if(this.mode.getActiveMode().equalsIgnoreCase("AAC 3.3.2")) {
         this.onAAC321();
      } else if(this.mc.thePlayer.fallDistance > 2.0F) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
      }

      super.onUpdate();
   }

   public void onAAC321() {
      if(this.mc.thePlayer.fallDistance > 2.0F) {
         this.startY += -this.mc.thePlayer.motionY;
         if(this.startY > 2.5D) {
            Line3D line = new Line3D(new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ), 0.0D, -90.0D, 99.0D);
            Iterator var3 = line.getPointsOn(0.5D).iterator();

            while(var3.hasNext()) {
               Location3D point = (Location3D)var3.next();
               BlockPos pos = new BlockPos(point.getX(), point.getY(), point.getZ());
               if(this.mc.theWorld.getBlockState(pos).getBlock() != Blocks.air) {
                  double top = this.mc.theWorld.getBlockState(pos).getBlock().getBlockBoundsMaxY() + (double)pos.getY();
                  if(this.mc.thePlayer.posY - top < 11.0D) {
                     this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, top, this.mc.thePlayer.posZ, true));
                  }
                  break;
               }
            }

            this.startY = 0.0D;
         }
      } else {
         this.startY = 99.0D;
      }

   }
}
