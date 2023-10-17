package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;

@ModuleAnnotation(name = "NoBob", category = ModuleCategory.RENDER, clickG = "Cool arm animation idk")
public class NoBob extends Module {


    float oldGamma;

    @EventTarget
    public void onRender(EventPreMotion e) {
        MC.thePlayer.distanceWalkedModified = 0.0f;
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {


    }
}
