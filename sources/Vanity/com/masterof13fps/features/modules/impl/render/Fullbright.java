package com.masterof13fps.features.modules.impl.render;

import com.masterof13fps.Wrapper;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;

import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "Fullbright", category = Category.RENDER, description = "Makes everything really bright")
public class Fullbright extends Module {

	@Override
	public void onToggle() {

	}

	@Override
	public void onEnable() {
		Wrapper.mc.gameSettings.gammaSetting = 100f;
	}

	@Override
	public void onDisable() {
		Wrapper.mc.gameSettings.gammaSetting = 1f;
	}

	@Override
	public void onEvent(Event event) {

	}
}