/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.S2EPacketCloseWindow;

@ModuleInfo(name = "NoGuiClose", description = "Prevents servers from closing opened Guis", category = Category.OTHER)
public final class NoGuiClose extends Module {

    private final BooleanSetting chatOnly = new BooleanSetting("Chat Only", this, true);

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S2EPacketCloseWindow && (mc.currentScreen instanceof GuiChat || !chatOnly.isEnabled()))
            event.setCancelled(true);
    }
}
