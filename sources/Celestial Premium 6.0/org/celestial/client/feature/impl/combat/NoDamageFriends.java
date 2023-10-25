/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.network.play.client.CPacketUseEntity;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class NoDamageFriends
extends Feature {
    public NoDamageFriends() {
        super("NoDamageFriends", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043d\u0435 \u043d\u0430\u043d\u043e\u0441\u0438\u0442\u044c \u0443\u0440\u043e\u043d \u0434\u0440\u0443\u0437\u044c\u044f\u043c", Type.Combat);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && Celestial.instance.friendManager.isFriend(NoDamageFriends.mc.objectMouseOver.entityHit.getName())) {
            event.setCancelled(true);
        }
    }
}

