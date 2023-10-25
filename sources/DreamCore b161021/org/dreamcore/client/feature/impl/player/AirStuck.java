/* 3eLeHyy#0089 */

package org.dreamcore.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventReceivePacket;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;

public class AirStuck extends Feature {

    public AirStuck() {
        super("AirStuck", "Вы зависаете в воздухе", Type.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.player.motionX = 0;
        mc.player.motionY = 0;
        mc.player.motionZ = 0;
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            event.setCancelled(true);
        }
    }


    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer.Position) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketEntityAction) {
            event.setCancelled(true);
        }
    }
}
