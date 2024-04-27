package me.aquavit.liquidsense.module.modules.client;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.FloatValue;

@ModuleInfo(name = "NoFOV", description = "Disables FOV changes caused by speed effect, etc.", category = ModuleCategory.CLIENT)
public class NoFOV extends Module {
    public static FloatValue fovValue = new FloatValue("FOV", 1f, 0f, 1.5f);
}
