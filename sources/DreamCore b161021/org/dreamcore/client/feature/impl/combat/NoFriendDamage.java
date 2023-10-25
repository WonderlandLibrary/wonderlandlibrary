package org.dreamcore.client.feature.impl.combat;

import net.minecraft.network.play.client.CPacketUseEntity;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;

public class NoFriendDamage extends Feature {

    public NoFriendDamage() {
        super("NoFriendDamage", "Не даёт ударить друга", Type.Combat);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cpacketUseEntity = (CPacketUseEntity) event.getPacket();
            if (cpacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK) && dreamcore.instance.friendManager.isFriend(mc.objectMouseOver.entityHit.getName())) {
                event.setCancelled(true);
            }
        }
    }
}
