package artemis.mods.Combat;

import org.lwjgl.input.Keyboard;
import artemis.Category;
import artemis.Artemis;
import artemis.module;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoGap extends module {
	Killaura killaura;
  public AutoGap() {
    super("AutoGap", "Automatically eats Gapples when needed", Keyboard.KEY_NONE, 17, Category.COMBAT);
  }
  
  public void onDisable() {
    super.onDisable();
  }
  
  public void onUpdate() {
    if (isToggled()) {
    	 if (mc.thePlayer.getHealth() <= 6f) {
             if (!mc.thePlayer.isPotionActive(Potion.regeneration)) { 
                  for (int n = 0; n <= 8; n++) {
		if (mc.thePlayer.inventoryContainer.getSlot(n+36).getStack() != null  ? Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(n+36).getStack().getItem()) == 322 : false) {
                           mc.thePlayer.inventory.currentItem = n;
			        mc.gameSettings.keyBindUseItem.pressed = true;
                            }
                         }
                      }
                   }
                   if (mc.thePlayer.isPotionActive(Potion.regeneration)) { 
                    mc.thePlayer.inventory.currentItem = 0;
                    //killaura.setState(true);
                   }
                   if (mc.thePlayer.isUsingItem() == true) {
		        //killaura.setState(true);
			}
                   }
                   /*this.addValues = function(values) {
                      values.add(Health);
                   } */
			
  

    super.onUpdate();
}
  }

  
  
 
