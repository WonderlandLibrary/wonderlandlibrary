package com.enjoytheban.module.modules.render;

import java.awt.Color;

import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

public class NoRender extends Module {

	public NoRender() {
		super("NoRender", new String[] {"noitems"}, ModuleType.Render);
		setColor(new Color(166,185,123).getRGB());
	}

}
