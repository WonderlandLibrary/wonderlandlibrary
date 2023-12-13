package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;

@ModuleAnnotation(name = "BlockAnimation", category = ModuleCategory.RENDER)
public class BlockAnimation extends Module {

    public static BlockAnimation getInstance() {
        return ModuleManager.getInstance(BlockAnimation.class);
    }

    public ModeSetting modeSetting = new ModeSetting("Type", this, new String[] {"Tired", "Exhibition", "Spin",  "Massacre"});

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
