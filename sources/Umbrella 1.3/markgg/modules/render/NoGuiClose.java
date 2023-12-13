/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.render;

import markgg.events.Event;
import markgg.events.listeners.EventPacket;
import markgg.modules.Module;
import markgg.settings.BooleanSetting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.S2EPacketCloseWindow;

public class NoGuiClose
extends Module {
    public BooleanSetting chatOnly = new BooleanSetting("Chat Only", true);

    public NoGuiClose() {
        super("NoGuiClose", 0, Module.Category.RENDER);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventPacket) {
            EventPacket cfr_ignored_0 = (EventPacket)e;
            if (EventPacket.getPacket() instanceof S2EPacketCloseWindow && (this.mc.currentScreen instanceof GuiChat || !this.chatOnly.isEnabled())) {
                e.setCancelled(true);
            }
        }
    }
}

