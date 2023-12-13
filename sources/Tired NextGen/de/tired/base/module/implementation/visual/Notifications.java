package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.Render2DEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;

@ModuleAnnotation(name = "Notifications", category = ModuleCategory.RENDER)
public class Notifications extends Module {



	@EventTarget
	public void onRender(Render2DEvent e) {
	}

	@Override
	public void onState() {

	}

	@Override
	public void onUndo() {

	}
}
