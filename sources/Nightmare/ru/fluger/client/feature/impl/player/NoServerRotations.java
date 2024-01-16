// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.player;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventReceivePacket;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.Feature;

public class NoServerRotations extends Feature
{
    public NoServerRotations() {
        super("NoServerRotation", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0440\u043e\u0442\u0430\u0446\u0438\u044e \u0441\u043e \u0441\u0442\u043e\u0440\u043e\u043d\u044b \u0441\u0435\u0440\u0432\u0435\u0440\u0430", Type.Player);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof jq) {
            final jq packet = (jq)event.getPacket();
            packet.d = NoServerRotations.mc.h.v;
            packet.e = NoServerRotations.mc.h.w;
        }
    }
}
