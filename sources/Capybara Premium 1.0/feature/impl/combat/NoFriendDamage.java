package fun.expensive.client.feature.impl.combat;

import fun.rich.client.Rich;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventSendPacket;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import net.minecraft.network.play.client.CPacketUseEntity;

public class NoFriendDamage extends Feature {

    public NoFriendDamage() {
        super("NoFriendDamage", "Не дает ударить друга", FeatureCategory.Combat);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity cpacketUseEntity = (CPacketUseEntity) event.getPacket();
            if (cpacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK) && Rich.instance.friendManager.isFriend(mc.objectMouseOver.entityHit.getName())) {
                event.setCancelled(true);
            }
        }
    }
}
