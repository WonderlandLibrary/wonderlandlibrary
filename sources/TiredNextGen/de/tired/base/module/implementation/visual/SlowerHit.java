package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;

@ModuleAnnotation(name = "SlowerHIT", category = ModuleCategory.RENDER, clickG = "Slower hit animation")
public class SlowerHit extends Module {

    public NumberSetting value = new NumberSetting("value",  this, 1, 1, 100, 1);

    public static SlowerHit getInstance() {
        return ModuleManager.getInstance(SlowerHit.class);
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
