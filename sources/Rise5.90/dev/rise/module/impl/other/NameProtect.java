/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;

@ModuleInfo(name = "NameProtect", description = "Hides your name", category = Category.RENDER)
public final class NameProtect extends Module {
    public static boolean enabled;
    // FontRenderer class


    @Override
    protected void onEnable() {
        enabled = true;
    }

    @Override
    protected void onDisable() {
        enabled = false;
    }

}
