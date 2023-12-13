/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.player;

import me.wintware.client.Main;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventSendPacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.network.play.client.CPacketUseEntity;

public class NoDamageTeam
extends Module {
    public NoDamageTeam() {
        super("NoDamageTeam", Category.Player);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && Main.instance.friendManager.isFriend(NoDamageTeam.mc.objectMouseOver.entityHit.getName())) {
            event.setCancelled(true);
        }
    }
}

