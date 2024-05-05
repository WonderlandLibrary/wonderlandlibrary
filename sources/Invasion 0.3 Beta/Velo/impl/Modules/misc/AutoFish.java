package Velo.impl.Modules.misc;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Util.Other.ChatUtil;
import Velo.impl.Event.EventUpdate;
import net.minecraft.item.ItemFishingRod;

public class AutoFish extends Module {
	
	public AutoFish() {
		super("AutoFish", "AutoFish", Keyboard.KEY_NONE, Category.OTHER);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	
	public void onUpdate(EventUpdate event) {
		try {
			if(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemFishingRod && mc.thePlayer.inventory.getCurrentItem().getItem() != null) {
				if(mc.thePlayer.fishEntity == null) {
					ItemFishingRod rod = new ItemFishingRod();
					mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
				} else {
					
				}
			} else {
				
			}
		} catch(NullPointerException e1) {
			
		}
	}
}
