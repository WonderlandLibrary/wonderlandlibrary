package me.aidanmees.trivia.client.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", Keyboard.KEY_V, Category.MOVEMENT, "Sprints everytime you walk.");
	}

	@Override
	public void onDisable() {
		mc.thePlayer.setSprinting(false);
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}
	
	@Override
	public void onLivingUpdate() {
		
			if(trivia.getModuleByName("NoSlowdown").isToggled()) {
				if (mc.gameSettings.keyBindForward.isKeyDown()) {
					mc.thePlayer.setSprinting(true);
				} else {
					mc.thePlayer.setSprinting(false);
				}
			}
			else {
				if (mc.gameSettings.keyBindForward.isKeyDown() && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isSneaking()) {
					mc.thePlayer.setSprinting(true);
				} else {
					mc.thePlayer.setSprinting(false);
				}
			
		
			
		}
		super.onLivingUpdate();
	}

	@Override
	public void onLateUpdate() {
		
		super.onLateUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Forwards", "MultiDir" };
	}

}
