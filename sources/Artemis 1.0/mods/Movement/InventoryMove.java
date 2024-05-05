package artemis.mods.Movement;

import org.lwjgl.input.Keyboard;

import artemis.Category;
import artemis.module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;

public class InventoryMove extends module{

	public InventoryMove() {
		super("InventoryMove", "Allows you to move while in gui", 0, 345, Category.MOVEMENT);
		
	}

	
	
	public void onUpdate() {
		if(this.isToggled()) {
			if(mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
				if(Keyboard.isKeyDown(200)) {
					EntityPlayerSP thePlayer = mc.thePlayer;
					thePlayer.rotationPitch -= 5.0F;
				}
				if(Keyboard.isKeyDown(208)) {
					EntityPlayerSP thePlayer1 = mc.thePlayer;
					thePlayer1.rotationPitch += 5.0F;
				}
				if(Keyboard.isKeyDown(203)) {
					EntityPlayerSP thePlayer2 = mc.thePlayer;
					thePlayer2.rotationPitch -= 7.0F;
				}
				if(Keyboard.isKeyDown(205)) {
					EntityPlayerSP thePlayer3 = mc.thePlayer;
					thePlayer3.rotationPitch += 7.0F;
				}
				
				
			}
		}
		
	}
	
}
