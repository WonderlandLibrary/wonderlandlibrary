// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.player;

import ru.fluger.client.event.EventTarget;
import java.util.Iterator;
import ru.fluger.client.friend.Friend;
import ru.fluger.client.Fluger;
import ru.fluger.client.event.events.impl.packet.EventReceivePacket;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class AutoTPAccept extends Feature
{
    private final BooleanSetting friendsOnly;
    private final NumberSetting delay;
    private final TimerHelper timerHelper;
    
    public AutoTPAccept() {
        super("AutoTPAccept", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043f\u0440\u0438\u043d\u0438\u043c\u0430\u0435\u0442 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442", Type.Player);
        this.timerHelper = new TimerHelper();
        this.friendsOnly = new BooleanSetting("Friends Only", false, () -> true);
        this.delay = new NumberSetting("Delay", 300.0f, 0.0f, 1000.0f, 100.0f, () -> true);
        this.addSettings(this.friendsOnly, this.delay);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        final in message = (in)e.getPacket();
        if (message.a().d().contains("\u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f")) {
            if (this.friendsOnly.getCurrentValue()) {
                for (final Friend friend : Fluger.instance.friendManager.getFriends()) {
                    if (message.a().d().contains(friend.getName())) {
                        if (!this.timerHelper.hasReached(this.delay.getCurrentValue())) {
                            continue;
                        }
                        AutoTPAccept.mc.h.g("/tpaccept");
                        this.timerHelper.reset();
                    }
                }
            }
            else if (this.timerHelper.hasReached(this.delay.getCurrentValue())) {
                AutoTPAccept.mc.h.g("/tpaccept");
                this.timerHelper.reset();
            }
        }
    }
}
