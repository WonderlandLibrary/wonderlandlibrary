package com.masterof13fps.features.modules.impl.world;

import com.masterof13fps.Wrapper;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "FastPlace", category = Category.WORLD, description = "Lets you place blocks real fast")
public class FastPlace extends Module {

	@Override
	public void onToggle() {

	}

	@Override
	public void onEnable() {

	}

	public void onDisable() {
		Wrapper.mc.rightClickDelayTimer = 6;
	}

	@Override
	public void onEvent(Event event) {
		Wrapper.mc.rightClickDelayTimer = 0;
	}
}