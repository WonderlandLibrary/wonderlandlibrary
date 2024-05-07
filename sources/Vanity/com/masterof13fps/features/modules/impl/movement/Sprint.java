package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.Wrapper;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;

import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "Sprint", category = Category.MOVEMENT, description = "Automatically sprints when moving")
public class Sprint extends Module {

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
			Wrapper.mc.thePlayer.setSprinting(true);
		}
	}
}