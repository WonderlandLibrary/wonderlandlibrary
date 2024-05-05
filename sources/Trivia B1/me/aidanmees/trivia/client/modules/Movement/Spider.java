package me.aidanmees.trivia.client.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;

public class Spider extends Module {

	WaitTimer timer = new WaitTimer();
	boolean jumped = false;

	public Spider() {
		super("Spider", Keyboard.KEY_NONE, Category.MOVEMENT,
				"Automatically climbs walls like a real spooder!");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (!mc.thePlayer.isCollidedHorizontally || mc.thePlayer.isOnLadder()) {
			timer.reset();
		}
		
		if (mode("Vanilla")) {
			if (mc.thePlayer.isCollidedHorizontally) {
				mc.thePlayer.jump();
			       
			   } 
			}
		

		super.onUpdate();
	}
	

}
