package de.violence.module.modules.MOVEMENT;

import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.ui.Line3D;
import de.violence.ui.Location3D;
import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Mouse;

public class Teleport extends Module {
   public Teleport() {
      super("Teleport", Category.MOVEMENT);
   }

   public void onUpdate() {
      BlockPos blockPos = this.mc.objectMouseOver.getBlockPos();
      if(blockPos != null) {
         Material material = this.mc.theWorld.getBlockState(blockPos).getBlock().getMaterial();
         if(Mouse.isButtonDown(0) && material != Material.air) {
            this.teleportTo(blockPos);

            try {
               Mouse.destroy();
               Mouse.create();
            } catch (Exception var4) {
               ;
            }
         }
      }

      super.onUpdate();
   }

   private void teleportTo(BlockPos blockPos) {
      Location3D startLoc = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.1D, this.mc.thePlayer.posZ);
      Location3D endLoc = new Location3D(blockPos.getX(), blockPos.getY() + 1.0D, blockPos.getZ());
      Line3D line3d = new Line3D(startLoc, endLoc);
      Iterator var6 = line3d.getPointsOn(0.005D).iterator();

      while(var6.hasNext()) {
         Location3D pointsOn = (Location3D)var6.next();
         double x = pointsOn.getX();
         double y = pointsOn.getY();
         double z = pointsOn.getZ();
         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
      }

      this.mc.thePlayer.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
   }
}
