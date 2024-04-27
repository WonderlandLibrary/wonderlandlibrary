/*
 * Decompiled with CFR 0.152.
 */
package ad.novoline.module;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;

@ModuleInfo(name="TianKengHelper", description="ee", category=ModuleCategory.RENDER)
public class TianKengHelper
extends Module {
    @EventTarget
    public void OnUpdate(UpdateEvent event) {
        if (mc.getThePlayer().getHealth() <= 3.0f && mc.getThePlayer().getHurtTime() > 0) {
            mc.getThePlayer().sendChatMessage("\u8349\u4f60\u5988");
            mc.getThePlayer().sendChatMessage("/hub");
        }
    }
}

