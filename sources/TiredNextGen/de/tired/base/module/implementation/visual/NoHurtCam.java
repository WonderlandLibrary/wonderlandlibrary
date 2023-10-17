package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;

@ModuleAnnotation(name = "NoHurtCam", category = ModuleCategory.RENDER, clickG = "You wont see any Damage!")
public class NoHurtCam extends Module {

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
