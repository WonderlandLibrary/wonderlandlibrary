package artemis.mods.Movement;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Hypixelfly extends module {

  
  public Hypixelfly() {
    super("Fly §7Hiypixel", "Fly but for Hiypixel", Keyboard.KEY_O, 8, Category.MOVEMENT);
  }
  
  public void onDisable() {
    super.onDisable();
  }
  
  public void onUpdate() {
    if (isToggled()) {
    	 double y;
         double y1;
         mc.thePlayer.motionY = 0;
         if(mc.thePlayer.ticksExisted % 3 == 0) {
             y = mc.thePlayer.posY - 1.0E-10D;
             mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true));
         }
         y1 = mc.thePlayer.posY + 1.0E-10D;
         mc.thePlayer.setPosition(mc.thePlayer.posX, y1, mc.thePlayer.posZ);  
      super.onUpdate();
    } 
  }
}