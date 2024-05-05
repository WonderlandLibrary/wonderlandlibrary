/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="NoSwing", description="Disabled swing effect when hitting an entity/mining a block.", category=ModuleCategory.RENDER)
public final class NoSwing
extends Module {
    private final BoolValue serverSideValue = new BoolValue("ServerSide", true);

    public final BoolValue getServerSideValue() {
        return this.serverSideValue;
    }
}

