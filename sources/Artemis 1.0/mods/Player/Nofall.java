package artemis.mods.Player;

import java.util.ArrayList;

import artemis.Artemis;
import artemis.Category;
import artemis.module;
import artemis.settings.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Nofall extends module {
	public String NofallMode = Artemis.instance.settingsManager.getSettingByName("Nofall Mode").getValString();;
  public Nofall() {
    super("Nofall", "No fall damage", 49, 10, Category.PLAYER);
  }
  @Override
  public void setup() {
      ArrayList<String> options = new ArrayList<String>();
      options.add("AAC");
      options.add("Vanilla");
      Artemis.instance.settingsManager.rSetting(new Setting("Nofall Mode", this, "Vanilla", options));
  }
  public void onUpdate() {
    if (isToggled()) {
    	 this.setName("Bhop §7" + NofallMode);

         if(NofallMode.equalsIgnoreCase("Vanilla")) {
      if(this.mc.thePlayer.fallDistance > 2.0F) {
      this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true)); 
        }
      }
    if(NofallMode.equalsIgnoreCase("AAC")) {
    	  if(mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isBurning()) {
        	  mc.thePlayer.motionY = -6;
          }
    }
    }
    super.onUpdate();
  }
}
