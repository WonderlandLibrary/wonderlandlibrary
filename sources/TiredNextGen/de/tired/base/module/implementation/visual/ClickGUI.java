package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;
import de.tired.Tired;

@ModuleAnnotation(name = "ClickGUI", category = ModuleCategory.RENDER, clickG = "ClickGui idk lol")
public class ClickGUI extends Module {

	public static ClickGUI getInstance() {
		return ModuleManager.getInstance(ClickGUI.class);
	}

	@Override
	public void onState() {
		if (MC.thePlayer == null) return;
		MC.displayGuiScreen(Tired.INSTANCE.clickGUINew);
		unableModule();

	}

	@Override
	public void onUndo() {

	}
}
