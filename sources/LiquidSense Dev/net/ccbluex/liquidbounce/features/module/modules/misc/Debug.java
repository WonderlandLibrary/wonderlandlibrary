package net.ccbluex.liquidbounce.features.module.modules.misc;

import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.network.Packet;

@ModuleInfo(name = "Debug", description = "Debug", category = ModuleCategory.MISC)
public class Debug extends Module {
    public void onPacket(PacketEvent e){
        final Packet<?> packet = e.getPacket();
        System.out.println(e);

    }
}
