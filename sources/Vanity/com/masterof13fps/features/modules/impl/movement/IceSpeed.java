package com.masterof13fps.features.modules.impl.movement;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;

import com.masterof13fps.features.modules.Category;
import net.minecraft.init.Blocks;

@ModuleInfo(name = "IceSpeed", category = Category.MOVEMENT, description = "Lets you walk very fast on (packed-)ice")
public class IceSpeed extends Module {

	@Override
	public void onEvent(Event event) {
		if(event instanceof EventUpdate) {
			Blocks.ice.slipperiness = 0.39F;
			Blocks.packed_ice.slipperiness = 0.39F;
		}
	}

	@Override
	public void onToggle() {

	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {
		Blocks.ice.slipperiness = 0.98F;
		Blocks.packed_ice.slipperiness = 0.98F;
	}
}