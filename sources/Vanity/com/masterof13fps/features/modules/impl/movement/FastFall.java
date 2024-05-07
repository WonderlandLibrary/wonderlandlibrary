package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.Wrapper;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "FastFall", category = Category.MOVEMENT, description = "Fall extremely fast while you're in air (useful for webs)")
public class FastFall extends Module {

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
		Wrapper.mc.thePlayer.motionY = -30;
	}
}