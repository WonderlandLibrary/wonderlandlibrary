/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;

@ModuleInfo(name = "AntiCheat", description = "An AntiCheat built into Rise", category = Category.OTHER)
public final class AntiCheat extends Module {
    private final ModeSetting alerts = new ModeSetting("Alerts", this, "Client", "Client", "Server");
}
