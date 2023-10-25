/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ListSetting;

public class CombatLeave
extends Feature {
    private final ListSetting leaveMode = new ListSetting("Leave Mode", "Lorent Craft", "Lorent Craft");

    public CombatLeave() {
        super("CombatLeave", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043b\u0438\u0432\u043d\u0443\u0442\u044c \u0432\u043e \u0432\u0440\u0435\u043c\u044f \u043f\u0432\u043f \u0431\u0435\u0437 \u043f\u043e\u0442\u0435\u0440\u0438 \u0432\u0435\u0449\u0435\u0439", Type.Combat);
        this.addSettings(this.leaveMode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(this.leaveMode.currentMode);
        if (this.leaveMode.currentMode.equals("Lorent Craft")) {
            int i;
            for (i = 0; i < 125; ++i) {
                CombatLeave.mc.player.connection.sendPacket(new CPacketPlayer.Position(100000.0, 100000.0, 100000.0, CombatLeave.mc.player.ticksExisted % 2 != 0));
            }
            for (i = 0; i < 125; ++i) {
                CombatLeave.mc.player.connection.sendPacket(new CPacketPlayer.Position(100000.0, -100000.0, 100000.0, CombatLeave.mc.player.ticksExisted % 2 != 0));
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (this.leaveMode.currentMode.equals("Lorent Craft")) {
            if (CombatLeave.mc.player != null && CombatLeave.mc.world != null) {
                if (event.getPacket() instanceof CPacketKeepAlive) {
                    event.setCancelled(true);
                }
                if (event.getPacket() instanceof CPacketConfirmTransaction) {
                    event.setCancelled(true);
                }
            } else {
                this.toggle();
                EventManager.unregister(this);
            }
        }
    }
}

