package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;

@ModuleAnnotation(name = "HotBar", category = ModuleCategory.RENDER, clickG = "Better hotbar design")
public class HotBar extends Module {

    public BooleanSetting smooth = new BooleanSetting("smooth", this, true);

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }

    public static HotBar getInstance() {
        return ModuleManager.getInstance(HotBar.class);
    }

}
