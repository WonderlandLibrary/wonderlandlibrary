/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.network.play.server.SPacketChat;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.friend.Friend;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AutoTPAccept
extends Feature {
    private final BooleanSetting friendsOnly;
    private final NumberSetting delay;
    private final TimerHelper timerHelper = new TimerHelper();

    public AutoTPAccept() {
        super("AutoTPAccept", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043f\u0440\u0438\u043d\u0438\u043c\u0430\u0435\u0442 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442", Type.Player);
        this.friendsOnly = new BooleanSetting("Friends Only", false, () -> true);
        this.delay = new NumberSetting("Delay", 300.0f, 0.0f, 1000.0f, 100.0f, () -> true);
        this.addSettings(this.friendsOnly, this.delay);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
        SPacketChat message = (SPacketChat)e.getPacket();
        if (message.getChatComponent().getFormattedText().contains("\u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f")) {
            if (this.friendsOnly.getCurrentValue()) {
                for (Friend friend : Celestial.instance.friendManager.getFriends()) {
                    if (!message.getChatComponent().getFormattedText().contains(friend.getName()) || !this.timerHelper.hasReached(this.delay.getCurrentValue())) continue;
                    AutoTPAccept.mc.player.sendChatMessage("/tpaccept");
                    this.timerHelper.reset();
                }
            } else if (this.timerHelper.hasReached(this.delay.getCurrentValue())) {
                AutoTPAccept.mc.player.sendChatMessage("/tpaccept");
                this.timerHelper.reset();
            }
        }
    }
}

