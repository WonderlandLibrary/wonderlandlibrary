package com.ohare.client.module.modules.other;

import java.awt.Color;

import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.Printer;
import com.ohare.client.utils.value.impl.BooleanValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

/**
 * made by Xen for OhareWare
 *
 * @since 6/10/2019
 **/
public class AntiFreeze extends Module {
    private BooleanValue safetp = new BooleanValue("SafeTP",true);
    public AntiFreeze() {
        super("AntiFreeze", Category.OTHER, new Color(0xA25B41).getRGB());
        setDescription("Anti freeze screen");
        setRenderlabel("Anti Freeze");
        addValues(safetp);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S2DPacketOpenWindow) {
            S2DPacketOpenWindow packetOpenWindow = (S2DPacketOpenWindow) event.getPacket();
            if (packetOpenWindow.getWindowTitle().getUnformattedText().toLowerCase().contains("frozen")) {
                event.setCanceled(true);
            }
        }

        if (event.getPacket() instanceof S02PacketChat&& safetp.isEnabled()) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            if (packet.getChatComponent().getFormattedText().contains("is currently frozen, you may not attack.")) {
                mc.thePlayer.setPositionAndUpdate(0, -999, 0);
                Printer.print("Person you tried to attack was frozen, teleported you to spawn.");
            }
        }
    }
}
