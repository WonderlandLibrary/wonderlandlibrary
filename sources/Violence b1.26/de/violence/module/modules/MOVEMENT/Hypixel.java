package de.violence.module.modules.MOVEMENT;

import de.violence.module.Module;
import de.violence.module.ui.Category;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Hypixel extends Module {
   public Hypixel() {
      super("Hypixel", Category.MOVEMENT);
   }

   public void onUpdate() {
      this.mc.thePlayer.motionY = 0.0D;
      if(this.mc.thePlayer.ticksExisted % 3 == 0) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0E-10D, this.mc.thePlayer.posZ, true));
      }

      this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-10D, this.mc.thePlayer.posZ);
      this.mc.thePlayer.onGround = true;
      this.mc.gameSettings.keyBindJump.pressed = false;
      super.onUpdate();
   }
}
