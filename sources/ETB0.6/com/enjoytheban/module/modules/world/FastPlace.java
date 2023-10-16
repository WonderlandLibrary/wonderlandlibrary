package com.enjoytheban.module.modules.world;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventTick;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

public class FastPlace extends Module {

	public FastPlace() {
		super("FastPlace", new String[] {"fplace", "fc"}, ModuleType.World);
        setColor(new Color(226,197,78).getRGB());
	}

	//every tick set the rcdt to 0
	@EventHandler
	private void onTick(EventTick e) {
		mc.rightClickDelayTimer = 0;
	}
}
