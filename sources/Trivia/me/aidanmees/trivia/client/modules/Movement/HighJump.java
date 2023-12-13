package me.aidanmees.trivia.client.modules.Movement;

import me.aidanmees.trivia.client.events.UpdateEvent;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;

public class HighJump extends Module {

	

	public HighJump() {
		super("HighJump", 0, Category.MOVEMENT, "Makes you jump high on AAC.");
	}

	@Override
	public void onEnable() {
	
		
		super.onEnable();
	}

	@Override
	public void onToggle() {
		
		super.onToggle();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (!mc.thePlayer.onGround) {
			mc.thePlayer.motionY += 0.05;
		}


		super.onUpdate();
	}

	

	
	@Override
	public void onDisable() {
		
		super.onDisable();
	}

}