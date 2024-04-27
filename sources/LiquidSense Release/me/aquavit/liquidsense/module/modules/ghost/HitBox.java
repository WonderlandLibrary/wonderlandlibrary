package me.aquavit.liquidsense.module.modules.ghost;

import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.FloatValue;

@ModuleInfo(name = "HitBox", description = "Makes hitboxes of targets bigger.", category = ModuleCategory.GHOST)
public class HitBox extends Module {
    public static FloatValue sizeValue = new FloatValue("Size", 0.4F, 0F, 1F);
}
