/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class NoServerRotations
extends Feature {
    public NoServerRotations() {
        super("NoServerRotations", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0440\u043e\u0442\u0430\u0446\u0438\u044e \u0441\u043e \u0441\u0442\u043e\u0440\u043e\u043d\u044b \u0441\u0435\u0440\u0432\u0435\u0440\u0430", Type.Player);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            packet.yaw = NoServerRotations.mc.player.rotationYaw;
            packet.pitch = NoServerRotations.mc.player.rotationPitch;
        }
    }
}

