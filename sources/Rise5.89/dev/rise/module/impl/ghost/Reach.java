/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.ghost;

import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.NumberSetting;

@ModuleInfo(name = "Reach", description = "Allows you to reach further", category = Category.GHOST)
public final class Reach extends Module {
    private final NumberSetting reach = new NumberSetting("Reach", this, 3.0, 3.0, 4.0, 0.01);
}
