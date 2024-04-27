package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.network.play.server.S2EPacketCloseWindow;

@ModuleInfo(name = "NoCloseGui",description = "Let the server cant close your gui.",category = ModuleCategory.MISC)
public class NoCloseGui extends Module {
    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        if (packetEvent.getPacket() instanceof S2EPacketCloseWindow) {
            packetEvent.cancelEvent();
        }
    }
}
