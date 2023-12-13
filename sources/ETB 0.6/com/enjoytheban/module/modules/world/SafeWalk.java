package com.enjoytheban.module.modules.world;

import java.awt.Color;

import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

/**
 * Safewalk
 * @author purity
 */

public class SafeWalk extends Module {

	public SafeWalk() {
		super("SafeWalk", new String[] {"eagle", "parkour"}, ModuleType.World);
		setColor(new Color(198,253,191).getRGB());
	}
}
