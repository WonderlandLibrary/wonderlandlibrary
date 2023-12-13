package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.ModuleManager;

@ModuleAnnotation(name = "Shader", category = ModuleCategory.RENDER)
public class Shader extends Module {

    public BooleanSetting blur = new BooleanSetting("Blur", this, true);

    public BooleanSetting chatBlur = new BooleanSetting("chatBlur", this, true, () -> blur.getValue());
    public BooleanSetting scoreBoardBlur = new BooleanSetting("scoreBoardBlur", this, true, () -> blur.getValue());
    public BooleanSetting clickGUIBlur = new BooleanSetting("clickGUIBlur", this, true, () -> blur.getValue());
    public BooleanSetting guiBlur = new BooleanSetting("guiBlur", this, true, () -> blur.getValue());

    public static Shader getInstance() {
        return ModuleManager.getInstance(Shader.class);
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
