package me.aquavit.liquidsense.module.modules.render;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;

@ModuleInfo(name = "CameraView", description = ":/", category = ModuleCategory.RENDER)
public class CameraView extends Module {
    public static final FloatValue fovValue = new FloatValue("FOV", 1f, 0f, 30f);
    public static final BoolValue clipValue = new BoolValue("CameraClip", false);
}
