package dev.rise.module.impl.movement;

import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;

@ModuleInfo(name = "SafeWalk", description = "Prevents you from walking off blocks", category = Category.MOVEMENT)
public class SafeWalk extends Module {
    private final BooleanSetting dirCheck = new BooleanSetting("Directional Check", this, false);
}
