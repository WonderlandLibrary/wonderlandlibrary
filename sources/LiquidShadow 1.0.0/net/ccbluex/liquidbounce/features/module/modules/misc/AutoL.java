package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.KillEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.TextValue;

@ModuleInfo(name = "AutoL",description = "Auto L to player you have killed.",category = ModuleCategory.MISC)
public class AutoL extends Module {
    private final TextValue messageValue = new TextValue("message","L %p you are killed by liquidshadow");

    @EventTarget
    public void onKill(KillEvent event) {
        mc.thePlayer.sendChatMessage(messageValue.get().replace("%p",event.getPlayer().getName()));
    }
}
