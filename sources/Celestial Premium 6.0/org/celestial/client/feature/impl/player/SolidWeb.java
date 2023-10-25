/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventWebSolid;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class SolidWeb
extends Feature {
    public SolidWeb() {
        super("SolidWeb", "\u0414\u0435\u043b\u0430\u0435\u0442 \u043f\u0430\u0443\u0442\u0438\u043d\u0443 \u043f\u043e\u043b\u043d\u043e\u0446\u0435\u043d\u043d\u044b\u043c \u0431\u043b\u043e\u043a\u043e\u043c", Type.Misc);
    }

    @EventTarget
    public void onWebSolid(EventWebSolid event) {
        event.setCancelled(!SolidWeb.mc.gameSettings.keyBindJump.pressed);
    }
}

