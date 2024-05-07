package com.masterof13fps.features.modules.impl.player;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;

import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "DeathDerp", category = Category.PLAYER, description = "Let you travel into the heaven when dead")
public class DeathDerp extends Module {
	@Override
	public void onToggle() {

	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if(event instanceof EventUpdate) {
			if (mc.thePlayer.getHealth() <= 0.0F) {
				mc.thePlayer.motionY = 0.5D;
			}
		}
	}
}
