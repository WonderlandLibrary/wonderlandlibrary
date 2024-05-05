package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.friend.FriendManager;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod (displayName = "NoFriends")
public class NoFriends extends Module
{
    @EventTarget
    public void onPacketSend(final PacketSendEvent event) {
        if (event.getPacket() instanceof C02PacketUseEntity) {
            final C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
            final Entity target = ClientUtils.mc().objectMouseOver.entityHit;
            final boolean isFriend = FriendManager.isFriend(target.getName());
            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK && isFriend) {
                event.setCancelled(true);
            }
        }
    }
}
