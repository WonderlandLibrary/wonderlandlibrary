package de.tired.base.module.implementation.misc;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.network.play.server.S2EPacketCloseWindow;

@ModuleAnnotation(name = "ServerActions", category = ModuleCategory.MISC)
public class StopServerActions extends Module {


    @EventTarget
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof S2EPacketCloseWindow)
            e.setCancelled(true);
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
