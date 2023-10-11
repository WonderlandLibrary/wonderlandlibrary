package artemis.mods.Combat;

import org.lwjgl.input.Keyboard;
import artemis.Category;
import artemis.Artemis;
import artemis.module;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoPotion extends module {
  public AutoPotion() {
    super("AutoPotion", "Automatically throws potions when needed", Keyboard.KEY_F, 17, Category.COMBAT);
  }
  private int timer;
  
  public void onDisable() {
    super.onDisable();
  }
  
  public void onUpdate() {
    if (isToggled()) {

			// check health
			if(mc.thePlayer.getHealth() < 6F) {
			//mc.thePlayer.cameraYaw = 90;
			
				for(int i = 1; i < 8; i++) {//Or any Loops
					mc.thePlayer.inventory.currentItem = 1;
					mc.gameSettings.keyBindUseItem.pressed = true;
				   //SomeFishCatching
				 }
	mc.thePlayer.inventory.currentItem = 1;
    
  }
}
    super.onUpdate();
  }
}

  
  
 
